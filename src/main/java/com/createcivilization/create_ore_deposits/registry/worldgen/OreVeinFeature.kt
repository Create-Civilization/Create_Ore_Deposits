package com.createcivilization.create_ore_deposits.registry.worldgen

import com.createcivilization.create_ore_deposits.config.Config
import com.createcivilization.create_ore_deposits.util.logW
import net.minecraft.core.BlockPos
import net.minecraft.core.BlockPos.MutableBlockPos
import net.minecraft.core.SectionPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.chunk.BulkSectionAccess
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.synth.SimplexNoise
import java.util.ArrayList
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs
import kotlin.math.cbrt
import kotlin.math.max
import kotlin.math.sqrt

/*
 * Port of Create 6.0.10's `LayeredOreFeature.place()` + `LayeredOreFeature.canPlaceOre()`,
 * driven by the `striated_ores_overworld` path in `AllPlacedFeatures#STRIATED_ORES_OVERWORLD`
 * -> `AllConfiguredFeatures#STRIATED_ORES_OVERWORLD`
 * -> `new LayeredOreConfiguration(overworldLayerPatterns, 32, 0)`.
 *
 * Create's real vein shape builds a sphere-like volume, slices it with a random
 * gradient vector, offsets those slice boundaries with simplex noise, then applies a second radial
 * noise pass before placing the chosen layer block. I want to keep that single-deposit shape and
 * only change the outer cluster behavior so one rare origin can place several Create-style deposits.
 *
 * The size stays fixed at Create's `LayeredOreConfiguration(..., 32, 0)` default instead of becoming
 * another config value. Cluster count and spacing are already configurable here, and keeping size fixed
 * avoids adding a third tuning axis when the goal is to stay visually close to Create's own deposits.
 */
class OreVeinFeature : Feature<OreVeinConfiguration>(OreVeinConfiguration.CODEC) {

	override fun place(context: FeaturePlaceContext<OreVeinConfiguration>): Boolean {
		val config: OreVeinConfiguration = context.config()
		val deposit = OreVeinDeposits.byBlock(config.state.block)
		val tierConfig = Config.SERVER.ORE_VEINS.byBlock(config.state.block).forTier(config.tier)
		val random = context.random()
		val minDeposits = minOf(tierConfig.minDepositsPerCluster, tierConfig.maxDepositsPerCluster).coerceAtLeast(1)
		val maxDeposits = maxOf(tierConfig.minDepositsPerCluster, tierConfig.maxDepositsPerCluster).coerceAtLeast(minDeposits)
		val desiredDeposits = if (minDeposits == maxDeposits) minDeposits else random.nextInt(maxDeposits - minDeposits + 1) + minDeposits
		val clusterOrigins = resolveClusterOrigins(context.origin(), config.state.block, config.tier, desiredDeposits, tierConfig, random)
		var placedDeposits = 0

		clusterOrigins.forEach { depositOrigin ->
			if (placeLayeredDeposit(context.level(), depositOrigin, deposit.layerPatterns, random)) {
				placedDeposits++
			}
		}

		return placedDeposits > 0
	}

	private fun resolveClusterOrigins(
		clusterOrigin: BlockPos,
		block: net.minecraft.world.level.block.Block,
		tier: OreVeinTier,
		depositCount: Int,
		tierConfig: Config.Server.OreVeins.Tier,
		random: RandomSource
	): List<BlockPos> {
		val originChunkX = clusterOrigin.x shr 4
		val originChunkZ = clusterOrigin.z shr 4
		val layout = OreVeinPlacementModifier.regionLayoutForChunk(originChunkX, originChunkZ, tierConfig)
		val spreadPlan = resolveSpreadPlan(block, tier, depositCount, tierConfig.chunksBetweenDeposits, originChunkX, originChunkZ, layout, random)

		return spreadPlan.offsets.mapIndexed { index, offset ->
			val chunkX = originChunkX + offset.first
			val chunkZ = originChunkZ + offset.second
			if (index == 0) {
				clusterOrigin
			} else {
				BlockPos(chunkX * 16 + random.nextInt(16), clusterOrigin.y, chunkZ * 16 + random.nextInt(16))
			}
		}
	}

	private fun resolveSpreadPlan(
		block: net.minecraft.world.level.block.Block,
		tier: OreVeinTier,
		desiredDepositCount: Int,
		configuredSpacingChunks: Int,
		originChunkX: Int,
		originChunkZ: Int,
		layout: OreVeinPlacementModifier.RegionLayout,
		random: RandomSource
	): SpreadPlan {
		var effectiveSpacingChunks = max(1, configuredSpacingChunks)
		var offsets = buildChunkOffsetCandidates(
			depositCount = desiredDepositCount,
			chunksBetweenDeposits = effectiveSpacingChunks,
			originChunkX = originChunkX,
			originChunkZ = originChunkZ,
			layout = layout,
			random = random
		)

		while (effectiveSpacingChunks > 1 && offsets.size < desiredDepositCount) {
			effectiveSpacingChunks--
			offsets = buildChunkOffsetCandidates(
				depositCount = desiredDepositCount,
				chunksBetweenDeposits = effectiveSpacingChunks,
				originChunkX = originChunkX,
				originChunkZ = originChunkZ,
				layout = layout,
				random = random
			)
		}

		val effectiveDepositCount = minOf(desiredDepositCount, offsets.size)
		if (effectiveSpacingChunks != configuredSpacingChunks || effectiveDepositCount != desiredDepositCount) {
			warnIncompatibleRegionConfig(block, tier, desiredDepositCount, configuredSpacingChunks, effectiveDepositCount, effectiveSpacingChunks)
		}

		return SpreadPlan(offsets.take(effectiveDepositCount))
	}

	private fun buildChunkOffsetCandidates(
		depositCount: Int,
		chunksBetweenDeposits: Int,
		originChunkX: Int,
		originChunkZ: Int,
		layout: OreVeinPlacementModifier.RegionLayout,
		random: RandomSource
	): List<Pair<Int, Int>> {
		if (depositCount <= 1) {
			return listOf(0 to 0)
		}

		val chunkStep = max(1, chunksBetweenDeposits)
		val candidates = mutableListOf<Pair<Int, Int>>()
		val maxOffsetNegativeX = originChunkX - layout.minDepositChunkX
		val maxOffsetPositiveX = layout.maxDepositChunkX - originChunkX
		val maxOffsetNegativeZ = originChunkZ - layout.minDepositChunkZ
		val maxOffsetPositiveZ = layout.maxDepositChunkZ - originChunkZ
		val maxRing = max(
			max(maxOffsetNegativeX, maxOffsetPositiveX),
			max(maxOffsetNegativeZ, maxOffsetPositiveZ)
		) / chunkStep

		for (ring in 1..maxRing) {
			for (chunkOffsetZ in -ring..ring) {
				for (chunkOffsetX in -ring..ring) {
					if (max(abs(chunkOffsetX), abs(chunkOffsetZ)) != ring) {
						continue
					}

					val offsetX = chunkOffsetX * chunkStep
					val offsetZ = chunkOffsetZ * chunkStep
					val candidateChunkX = originChunkX + offsetX
					val candidateChunkZ = originChunkZ + offsetZ
					if (candidateChunkX !in layout.minDepositChunkX..layout.maxDepositChunkX) {
						continue
					}
					if (candidateChunkZ !in layout.minDepositChunkZ..layout.maxDepositChunkZ) {
						continue
					}

					candidates += offsetX to offsetZ
				}
			}
		}

		shuffle(candidates, random)
		return listOf(0 to 0) + candidates.take(depositCount - 1)
	}

	private fun warnIncompatibleRegionConfig(
		block: net.minecraft.world.level.block.Block,
		tier: OreVeinTier,
		requestedDeposits: Int,
		configuredSpacingChunks: Int,
		effectiveDeposits: Int,
		effectiveSpacingChunks: Int
	) {
		val warningKey = "${BuiltInRegistries.BLOCK.getKey(block)}|${tier.getSerializedName()}"
		if (!WARNED_REGION_MISCONFIGS.add(warningKey)) {
			return
		}

		logW(
			"Clamped ore vein cluster spread for ${BuiltInRegistries.BLOCK.getKey(block)} ${tier.getSerializedName()}: " +
				"requested $requestedDeposits deposits at spacing $configuredSpacingChunks chunks, " +
				"but region size ${Config.Server.OreVeins.CLUSTER_REGION_SIZE_CHUNKS} only fits " +
				"$effectiveDeposits deposits at spacing $effectiveSpacingChunks."
		)
	}

	private fun placeLayeredDeposit(
		level: WorldGenLevel,
		origin: BlockPos,
		patternPool: List<LayeredDepositPattern>,
		random: RandomSource
	): Boolean {
		if (patternPool.isEmpty()) {
			return false
		}

		val radius = LAYERED_DEPOSIT_SIZE * 0.5f
		val radiusBound = Mth.ceil(radius) - 1
		if (!areTouchedChunksAccessible(level, origin, radiusBound)) {
			return false
		}

		if (origin.y >= level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, origin.x, origin.z)) {
			return false
		}

		val layerPattern = patternPool[random.nextInt(patternPool.size)]
		val resolvedLayers = resolveLayers(layerPattern, random)
		if (resolvedLayers.isEmpty()) {
			return false
		}

		val gradient = resolveGradient(random)
		val layerDisplacementNoise = SimplexNoise(random)
		val radiusNoise = SimplexNoise(random)
		val mutablePos = MutableBlockPos()
		val bulkSectionAccess = BulkSectionAccess(level)
		var placedAmount = 0

		try {
			for (deltaZBlock in -radiusBound..radiusBound) {
				val deltaZ = deltaZBlock * (1.0f / radius)
				if (deltaZ * deltaZ > 1.0f) {
					continue
				}

				for (deltaXBlock in -radiusBound..radiusBound) {
					val deltaX = deltaXBlock * (1.0f / radius)
					if (deltaZ * deltaZ + deltaX * deltaX > 1.0f) {
						continue
					}

					for (deltaYBlock in -radiusBound..radiusBound) {
						val deltaY = deltaYBlock * (1.0f / radius)
						val distanceSquared = deltaZ * deltaZ + deltaX * deltaX + deltaY * deltaY
						if (distanceSquared > 1.0f || level.isOutsideBuildHeight(origin.y + deltaYBlock)) {
							continue
						}

						val currentX = origin.x + deltaXBlock
						val currentY = origin.y + deltaYBlock
						val currentZ = origin.z + deltaZBlock
						var rampValue = gradient.x * deltaX + gradient.y * deltaY + gradient.z * deltaZ
						rampValue += layerDisplacementNoise.getValue(
							currentX * LAYER_NOISE_FREQUENCY.toDouble(),
							currentY * LAYER_NOISE_FREQUENCY.toDouble(),
							currentZ * LAYER_NOISE_FREQUENCY.toDouble()
						).toFloat() * (MAX_LAYER_DISPLACEMENT / RESOLVED_LAYER_SIZE)

						val layerEntry = selectLayerEntry(resolvedLayers, rampValue)
						if (distanceSquared > layerEntry.radialThresholdMultiplier) {
							continue
						}

						val thresholdNoiseValue = Mth.map(
							radiusNoise.getValue(
								currentX * RADIAL_NOISE_FREQUENCY.toDouble(),
								currentY * RADIAL_NOISE_FREQUENCY.toDouble(),
								currentZ * RADIAL_NOISE_FREQUENCY.toDouble()
							).toFloat(),
							-1.0f,
							1.0f,
							1.0f - MAX_RADIAL_THRESHOLD_REDUCTION,
							1.0f
						)

						if (distanceSquared > layerEntry.radialThresholdMultiplier * thresholdNoiseValue) {
							continue
						}

						val layer = layerEntry.layer ?: continue
						mutablePos.set(currentX, currentY, currentZ)
						if (!level.ensureCanWrite(mutablePos)) {
							continue
						}

						val section = bulkSectionAccess.getSection(mutablePos) ?: continue
						val localX = SectionPos.sectionRelative(currentX)
						val localY = SectionPos.sectionRelative(currentY)
						val localZ = SectionPos.sectionRelative(currentZ)
						val blockState = section.getBlockState(localX, localY, localZ)

						for (targetBlockState in layer.rollTargets(random)) {
							if (!canPlaceLayeredBlock(blockState, { pos -> bulkSectionAccess.getBlockState(pos) }, random, targetBlockState, mutablePos)) {
								continue
							}
							if (targetBlockState.state.isAir) {
								continue
							}

							section.setBlockState(localX, localY, localZ, targetBlockState.state, false)
							placedAmount++
							break
						}
					}
				}
			}
		} finally {
			bulkSectionAccess.close()
		}

		return placedAmount > 0
	}

	private fun areTouchedChunksAccessible(
		level: WorldGenLevel,
		origin: BlockPos,
		radiusBound: Int
	): Boolean {
		val minChunkX = (origin.x - radiusBound) shr 4
		val maxChunkX = (origin.x + radiusBound) shr 4
		val minChunkZ = (origin.z - radiusBound) shr 4
		val maxChunkZ = (origin.z + radiusBound) shr 4

		for (chunkX in minChunkX..maxChunkX) {
			for (chunkZ in minChunkZ..maxChunkZ) {
				if (!isChunkAccessible(level, chunkX, chunkZ)) {
					return false
				}
			}
		}

		return true
	}

	private fun isChunkAccessible(
		level: WorldGenLevel,
		chunkX: Int,
		chunkZ: Int
	): Boolean = runCatching {
		level.getChunk(chunkX, chunkZ)
	}.isSuccess

	private fun resolveLayers(
		layerPattern: LayeredDepositPattern,
		random: RandomSource
	): List<ResolvedLayerEntry> {
		val temporaryLayers = ArrayList<TemporaryLayerEntry>()
		var layerSizeTotal = 0.0f
		var currentLayer: LayeredDepositPattern.Layer? = null

		while (layerSizeTotal < RESOLVED_LAYER_SIZE) {
			val nextLayer = layerPattern.rollNext(currentLayer, random)
			val layerSize = Mth.randomBetween(random, nextLayer.minSize.toFloat(), nextLayer.maxSize.toFloat())
			temporaryLayers += TemporaryLayerEntry(nextLayer, layerSize)
			layerSizeTotal += layerSize
			currentLayer = nextLayer
		}

		val resolvedLayers = ArrayList<ResolvedLayerEntry>(temporaryLayers.size)
		var cumulativeLayerSize = -(layerSizeTotal - RESOLVED_LAYER_SIZE) * random.nextFloat()

		temporaryLayers.forEach { temporaryLayer ->
			val rampStartValue = if (resolvedLayers.isEmpty()) {
				Float.NEGATIVE_INFINITY
			} else {
				cumulativeLayerSize * (2.0f / RESOLVED_LAYER_SIZE) - 1.0f
			}

			cumulativeLayerSize += temporaryLayer.size
			if (cumulativeLayerSize < 0.0f) {
				return@forEach
			}

			resolvedLayers += ResolvedLayerEntry(
				layer = temporaryLayer.layer,
				radialThresholdMultiplier = Mth.randomBetween(random, 0.5f, 1.0f),
				rampStartValue = rampStartValue
			)
		}

		return resolvedLayers
	}

	private fun resolveGradient(random: RandomSource): Gradient {
		var gradientY = Mth.randomBetween(random, -1.0f, 1.0f)
		gradientY = cbrt(gradientY)
		val xzRescale = sqrt(1.0f - gradientY * gradientY)
		val theta = random.nextFloat() * Mth.TWO_PI

		return Gradient(
			x = Mth.cos(theta) * xzRescale,
			y = gradientY,
			z = Mth.sin(theta) * xzRescale
		)
	}

	private fun selectLayerEntry(
		resolvedLayers: List<ResolvedLayerEntry>,
		rampValue: Float
	): ResolvedLayerEntry {
		var layerIndex = Collections.binarySearch(
			resolvedLayers,
			ResolvedLayerEntry(
				layer = null,
				radialThresholdMultiplier = 0.0f,
				rampStartValue = rampValue
			)
		)

		if (layerIndex < 0) {
			layerIndex = -2 - layerIndex
		}

		return resolvedLayers[layerIndex.coerceIn(0, resolvedLayers.lastIndex)]
	}

	private fun canPlaceLayeredBlock(
		existingState: BlockState,
		adjacentStateAccessor: (BlockPos) -> BlockState,
		random: RandomSource,
		targetState: OreConfiguration.TargetBlockState,
		mutablePos: MutableBlockPos
	): Boolean {
		if (!targetState.target.test(existingState, random)) {
			return false
		}

		if (shouldSkipAirCheck(random, DISCARD_CHANCE_ON_AIR_EXPOSURE)) {
			return true
		}

		return !isAdjacentToAir(adjacentStateAccessor, mutablePos)
	}

	private fun shouldSkipAirCheck(random: RandomSource, chance: Float): Boolean = when {
		chance <= 0.0f -> true
		chance >= 1.0f -> false
		else -> random.nextFloat() >= chance
	}

	private fun shuffle(values: MutableList<Pair<Int, Int>>, random: RandomSource) {
		for (index in values.lastIndex downTo 1) {
			val swapIndex = random.nextInt(index + 1)
			val value = values[index]
			values[index] = values[swapIndex]
			values[swapIndex] = value
		}
	}

	private data class Gradient(
		val x: Float,
		val y: Float,
		val z: Float
	)

	private data class TemporaryLayerEntry(
		val layer: LayeredDepositPattern.Layer,
		val size: Float
	)

	private data class ResolvedLayerEntry(
		val layer: LayeredDepositPattern.Layer?,
		val radialThresholdMultiplier: Float,
		val rampStartValue: Float
	) : Comparable<ResolvedLayerEntry> {
		override fun compareTo(other: ResolvedLayerEntry): Int = rampStartValue.compareTo(other.rampStartValue)
	}

	private data class SpreadPlan(
		val offsets: List<Pair<Int, Int>>
	)

	companion object {
		private const val LAYERED_DEPOSIT_SIZE = 32
		private const val DISCARD_CHANCE_ON_AIR_EXPOSURE = 0.0f
		private const val RESOLVED_LAYER_SIZE = LAYERED_DEPOSIT_SIZE + 1
		private const val MAX_LAYER_DISPLACEMENT = 1.75f
		private const val LAYER_NOISE_FREQUENCY = 0.125f
		private const val MAX_RADIAL_THRESHOLD_REDUCTION = 0.25f
		private const val RADIAL_NOISE_FREQUENCY = 0.125f
		private val WARNED_REGION_MISCONFIGS: MutableSet<String> = ConcurrentHashMap.newKeySet()
	}
}
