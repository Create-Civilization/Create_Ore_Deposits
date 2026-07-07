package com.createcivilization.create_ore_deposits.registry.worldgen

import com.createcivilization.create_ore_deposits.config.Config
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.placement.PlacementContext
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.minecraft.world.level.levelgen.placement.PlacementModifierType
import kotlin.math.ceil
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.stream.Stream

/*
 * Create 6.0.10 places `create:striated_ores_overworld` through one rarity-filtered placed feature
 * (`AllPlacedFeatures#STRIATED_ORES_OVERWORLD` / `data/create/worldgen/placed_feature/striated_ores_overworld.json`),
 * then lets its custom feature handle the actual deposit shape from that rare origin.
 *
 * This modifier only handles the world-scale question of "does a cluster exist here at all?" through
 * `averageChunksPerCluster` and the optional family gate. The separate `chunksBetweenDeposits` value
 * lives in the feature and only spreads layered deposits inside a cluster that already won placement.
 */
class OreVeinPlacementModifier(
	private val block: Block,
	private val tier: OreVeinTier
) : PlacementModifier() {

	override fun getPositions(context: PlacementContext, random: RandomSource, pos: BlockPos): Stream<BlockPos> {
		val chunkX = pos.x shr 4
		val chunkZ = pos.z shr 4
		val regionX = Math.floorDiv(chunkX, Config.Server.OreVeins.CLUSTER_REGION_SIZE_CHUNKS)
		val regionZ = Math.floorDiv(chunkZ, Config.Server.OreVeins.CLUSTER_REGION_SIZE_CHUNKS)
		val worldSeed = context.level.seed
		val winner = selectWinnerForRegion(
			worldSeed = worldSeed,
			regionX = regionX,
			regionZ = regionZ,
			minGenY = context.getMinGenY(),
			maxGenY = context.getMinGenY() + context.getGenDepth() - 1,
			targetDeposits = OreVeinDeposits.DEPOSITS,
			useFamilyGate = Config.SERVER.ORE_VEINS.enableRegionFamilyGate
		) ?: return Stream.empty()

		if (winner.block != block || winner.tier != tier) {
			return Stream.empty() // not our block+tier's turn to spawn this region, some other ore/tier won it
		}
		if ((winner.origin.x shr 4) != chunkX || (winner.origin.z shr 4) != chunkZ) {
			return Stream.empty() // winner exists but its origin lives in a different chunk than the one we're being asked about
		}

		return Stream.of(winner.origin)
	}

	override fun type(): PlacementModifierType<*> = CreateOreDepositsPlacementModifiers.ORE_VEIN.get()

	internal data class RegionLayout(
// where the CLUSTER ORIGIN is allowed to land, inset enough that the whole spread of deposits
// around it stays inside this region and never crosses into the next one over
		val minChunkX: Int,
		val maxChunkX: Int,
		val minChunkZ: Int,
		val maxChunkZ: Int,
		val minOriginChunkX: Int,
		val maxOriginChunkX: Int,
		val minOriginChunkZ: Int,
		val maxOriginChunkZ: Int,
		val minDepositChunkX: Int,
		val maxDepositChunkX: Int,
		val minDepositChunkZ: Int,
		val maxDepositChunkZ: Int
	)

	companion object {
		/*
		 * Old per-chunk rarity was 1 / averageChunksPerCluster.
		 * A region contains REGION_SIZE^2 chunks, so the equivalent per-region probability is:
		 *   P(region spawns) = min(1, regionAreaChunks / averageChunksPerCluster)
		 * We preserve the existing hash-mod pattern by converting that to a denominator:
		 *   regionSpawnDenominator = max(1, ceil(averageChunksPerCluster / regionAreaChunks))
		 */
		internal fun regionSpawnDenominator(averageChunksPerCluster: Int): Int {
			if (averageChunksPerCluster <= 0) {
				return 0
			}

			val regionAreaChunks = Config.Server.OreVeins.CLUSTER_REGION_SIZE_CHUNKS * Config.Server.OreVeins.CLUSTER_REGION_SIZE_CHUNKS
			return ceil(averageChunksPerCluster / regionAreaChunks.toDouble()).toInt().coerceAtLeast(1)
		}

		internal fun regionLayoutForChunk(
			chunkX: Int,
			chunkZ: Int,
			tierConfig: Config.Server.OreVeins.Tier
		): RegionLayout = regionLayout(
			Math.floorDiv(chunkX, Config.Server.OreVeins.CLUSTER_REGION_SIZE_CHUNKS),
			Math.floorDiv(chunkZ, Config.Server.OreVeins.CLUSTER_REGION_SIZE_CHUNKS),
			tierConfig
		)

		internal fun regionLayout(
			regionX: Int,
			regionZ: Int,
			tierConfig: Config.Server.OreVeins.Tier
		): RegionLayout {
			val regionSizeChunks = Config.Server.OreVeins.CLUSTER_REGION_SIZE_CHUNKS
			val paddingChunks = Config.Server.OreVeins.CLUSTER_REGION_PADDING_CHUNKS
			val minChunkX = regionX * regionSizeChunks
			val maxChunkX = minChunkX + regionSizeChunks - 1
			val minChunkZ = regionZ * regionSizeChunks
			val maxChunkZ = minChunkZ + regionSizeChunks - 1
			val maxDeposits = maxOf(tierConfig.minDepositsPerCluster, tierConfig.maxDepositsPerCluster).coerceAtLeast(1)
			val configuredSpreadRadiusChunks = ceil(maxDeposits * tierConfig.chunksBetweenDeposits / 2.0).toInt()
			/*
			 * Deposit offsets are placed around the cluster origin in chunk units. To guarantee they stay inside
			 * their own region with one full empty chunk left before the region edge, keep the origin inset by:
			 *   ceil(maxDepositsPerCluster * chunksBetweenDeposits / 2) + 1
			 * Example: 9 deposits at 2 chunks apart => ceil(9 * 2 / 2) + 1 = 10 chunks inset.
			 * The clamp keeps at least one origin chunk available even if the config asks for more than the region can fit.
			 */
			val originInsetChunks = (configuredSpreadRadiusChunks + paddingChunks)
				.coerceAtMost((regionSizeChunks - 1) / 2)

// just the ore+tier pair, picked before we even know if it can spawn — see selectReservedWinnerForRegion
			return RegionLayout(
				minChunkX = minChunkX,
				maxChunkX = maxChunkX,
				minChunkZ = minChunkZ,
				maxChunkZ = maxChunkZ,
				minOriginChunkX = minChunkX + originInsetChunks,
				maxOriginChunkX = maxChunkX - originInsetChunks,
				minOriginChunkZ = minChunkZ + originInsetChunks,
				maxOriginChunkZ = maxChunkZ - originInsetChunks,
				minDepositChunkX = minChunkX + paddingChunks,
				maxDepositChunkX = maxChunkX - paddingChunks,
				minDepositChunkZ = minChunkZ + paddingChunks,
				maxDepositChunkZ = maxChunkZ - paddingChunks
			)
		}

		private data class ReservedCandidate(
			val block: Block,
			val blockHash: Long,
			val tier: OreVeinTier
		)
// same idea but after the fact — this one HAS an origin and a sortKey because it already passed the rarity check
		private data class Candidate(
			val block: Block,
			val tier: OreVeinTier,
			val origin: BlockPos,
			val sortKey: Long
		)
		
		/*
		 * Winner selection is deterministic per region and is queried once per placed feature per chunk.
		 * Cache the winner to avoid recomputing it ~30x per chunk, and cap size with FIFO eviction so it
		 * can't grow without bound on long-running servers.
		 */
		private data class RegionKey(
			val worldSeed: Long,
			val regionX: Int,
			val regionZ: Int,
			val minGenY: Int,
			val maxGenY: Int,
			val targetDepositsKey: Long,
			val useFamilyGate: Boolean
		)

		private sealed interface CachedWinner {
			fun resolve(): Candidate?
		}

		private data class CachedWinnerPresent(val winner: Candidate) : CachedWinner {
			override fun resolve(): Candidate = winner
		}

		private data object CachedWinnerMissing : CachedWinner {
			override fun resolve(): Candidate? = null
		}

		private const val REGION_WINNER_CACHE_MAX_ENTRIES = 4096
		private val REGION_WINNER_CACHE: MutableMap<RegionKey, CachedWinner> = ConcurrentHashMap()
		private val REGION_WINNER_CACHE_FIFO: ConcurrentLinkedQueue<RegionKey> = ConcurrentLinkedQueue()

		private fun targetDepositsCacheKey(targetDeposits: List<OreVeinDeposits.DepositVeinDefinition>): Long {
			if (targetDeposits === OreVeinDeposits.DEPOSITS) {
				return 0L
			}

			var result = 0x4D6E2F7B4A65C123L
			targetDeposits.forEach { deposit ->
				result = (result xor deposit.blockHash) * 0x5F58476D1CE4E5B9L
				result = java.lang.Long.rotateLeft(result, 27)
			}
			return result
		}

		private fun trimRegionWinnerCache() {
			while (REGION_WINNER_CACHE.size > REGION_WINNER_CACHE_MAX_ENTRIES) {
				val oldest = REGION_WINNER_CACHE_FIFO.poll() ?: return
				REGION_WINNER_CACHE.remove(oldest)
			}
		}

// used by DepositLocatorCommand to answer "would a cluster spawn at this chunk" without
// actually running worldgen. same maths as getPositions() above, just callable standalone.
		fun wouldClusterSpawnAt(
			worldSeed: Long,
			chunkX: Int,
			chunkZ: Int,
			minGenY: Int,
			maxGenY: Int,
			targetDeposits: List<OreVeinDeposits.DepositVeinDefinition>,
			useFamilyGate: Boolean
		): Triple<OreVeinDeposits.DepositVeinDefinition, OreVeinTier, BlockPos>? {
			val regionX = Math.floorDiv(chunkX, Config.Server.OreVeins.CLUSTER_REGION_SIZE_CHUNKS)
			val regionZ = Math.floorDiv(chunkZ, Config.Server.OreVeins.CLUSTER_REGION_SIZE_CHUNKS)
			val winner = selectWinnerForRegion(worldSeed, regionX, regionZ, minGenY, maxGenY, targetDeposits, useFamilyGate)
				?: return null

			if ((winner.origin.x shr 4) != chunkX || (winner.origin.z shr 4) != chunkZ) {
				return null
			}

			val deposit = targetDeposits.firstOrNull { it.block.get() == winner.block } ?: return null
			return Triple(deposit, winner.tier, winner.origin)
		}

// picks how a region decides its winner — competitive means every ore+tier rolls and best
// sort key wins (ores compete), reserved means one ore+tier gets dibs on the region before
// anyone even rolls (no competition, each region's just reserved for whoever got picked)
		private fun selectWinnerForRegion(
			worldSeed: Long,
			regionX: Int,
			regionZ: Int,
			minGenY: Int,
			maxGenY: Int,
			targetDeposits: List<OreVeinDeposits.DepositVeinDefinition>,
			useFamilyGate: Boolean
		): Candidate? {
			val cacheKey = RegionKey(
				worldSeed = worldSeed,
				regionX = regionX,
				regionZ = regionZ,
				minGenY = minGenY,
				maxGenY = maxGenY,
				targetDepositsKey = targetDepositsCacheKey(targetDeposits),
				useFamilyGate = useFamilyGate
			)

			val cached = REGION_WINNER_CACHE[cacheKey]
			if (cached != null) {
				return cached.resolve()
			}

			val resolved = if (useFamilyGate) {
				selectCompetitiveWinnerForRegion(worldSeed, regionX, regionZ, minGenY, maxGenY, targetDeposits)
			} else {
				selectReservedWinnerForRegion(worldSeed, regionX, regionZ, minGenY, maxGenY, targetDeposits)
			}

			val entry: CachedWinner = resolved?.let(::CachedWinnerPresent) ?: CachedWinnerMissing
			val existing = REGION_WINNER_CACHE.putIfAbsent(cacheKey, entry)
			if (existing == null) {
				REGION_WINNER_CACHE_FIFO.add(cacheKey)
				trimRegionWinnerCache()
				return entry.resolve()
			}

			return existing.resolve()
		}

		private fun selectCompetitiveWinnerForRegion(
			worldSeed: Long,
			regionX: Int,
			regionZ: Int,
			minGenY: Int,
			maxGenY: Int,
			targetDeposits: List<OreVeinDeposits.DepositVeinDefinition>
		): Candidate? = targetDeposits.asSequence()
			.flatMap { deposit ->
				val depositBlock = deposit.block.get()
				OreVeinTier.entries.asSequence().mapNotNull { candidateTier ->
					val tierConfig = Config.SERVER.ORE_VEINS.byBlock(depositBlock).forTier(candidateTier)
					val origin = candidateOrigin(worldSeed, regionX, regionZ, minGenY, maxGenY, deposit.blockHash, candidateTier, tierConfig)
						?: return@mapNotNull null

					Candidate(
						depositBlock,
						candidateTier,
						origin,
						mix(worldSeed, regionX.toLong(), regionZ.toLong(), deposit.blockHash, candidateTier.ordinal.toLong(), 0x2F8E9F3C4D5B6A71L)
					)
				}
			}
			.minByOrNull(Candidate::sortKey)

		private fun selectReservedWinnerForRegion(
			worldSeed: Long,
			regionX: Int,
			regionZ: Int,
			minGenY: Int,
			maxGenY: Int,
			targetDeposits: List<OreVeinDeposits.DepositVeinDefinition>
		): Candidate? {
			val allCandidates = targetDeposits.flatMap { deposit ->
				val depositBlock = deposit.block.get()
				OreVeinTier.entries.map { candidateTier ->
					ReservedCandidate(depositBlock, deposit.blockHash, candidateTier)
				}
			}
			if (allCandidates.isEmpty()) {
				return null
			}

			val reservedIndex = positiveMod(
				mix(worldSeed, regionX.toLong(), regionZ.toLong(), 0x66D11E6C2C0A01F3L),
				allCandidates.size
			)
			val reservedCandidate = allCandidates[reservedIndex]
			val tierConfig = Config.SERVER.ORE_VEINS.byBlock(reservedCandidate.block).forTier(reservedCandidate.tier)
			val origin = candidateOrigin(
				worldSeed,
				regionX,
				regionZ,
				minGenY,
				maxGenY,
				reservedCandidate.blockHash,
				reservedCandidate.tier,
				tierConfig
			) ?: return null

			return Candidate(
				reservedCandidate.block,
				reservedCandidate.tier,
				origin,
				reservedIndex.toLong()
			)
		}

		private fun candidateOrigin(
			worldSeed: Long,
			regionX: Int,
			regionZ: Int,
			minGenY: Int,
			maxGenY: Int,
			candidateBlockHash: Long,
			candidateTier: OreVeinTier,
			tierConfig: Config.Server.OreVeins.Tier
// every mix() call below uses a different hardcoded salt so x, y, z, and the rarity roll
// don't all land on the same value — they're arbitrary, not meaningful numbers, don't overthink them
		): BlockPos? {
			val regionSpawnDenominator = regionSpawnDenominator(tierConfig.averageChunksPerCluster)
			if (regionSpawnDenominator <= 0 || !passesRarity(worldSeed, regionX, regionZ, candidateBlockHash, candidateTier, regionSpawnDenominator)) {
				return null
			}

			val layout = regionLayout(regionX, regionZ, tierConfig)
			val minY = Mth.clamp(minOf(tierConfig.minY, tierConfig.maxY), minGenY, maxGenY)
			val maxY = Mth.clamp(maxOf(tierConfig.minY, tierConfig.maxY), minGenY, maxGenY)
			val originChunkX = layout.minOriginChunkX + positiveMod(
				mix(worldSeed, regionX.toLong(), regionZ.toLong(), candidateBlockHash, candidateTier.ordinal.toLong(), 0x6B8B4567327B23C6L),
				layout.maxOriginChunkX - layout.minOriginChunkX + 1
			)
			val originChunkZ = layout.minOriginChunkZ + positiveMod(
				mix(worldSeed, regionX.toLong(), regionZ.toLong(), candidateBlockHash, candidateTier.ordinal.toLong(), 0x13579BDF2468ACE0L),
				layout.maxOriginChunkZ - layout.minOriginChunkZ + 1
			)
			val x = originChunkX * 16 + positiveMod(
				mix(worldSeed, regionX.toLong(), regionZ.toLong(), candidateBlockHash, candidateTier.ordinal.toLong(), 0x47F2D5C6A1B3098EL),
				16
			)
			val z = originChunkZ * 16 + positiveMod(
				mix(worldSeed, regionX.toLong(), regionZ.toLong(), candidateBlockHash, candidateTier.ordinal.toLong(), 0x13579BDF2468ACE0L),
				16
			)
			val y = if (minY == maxY) {
				minY
			} else {
				minY + positiveMod(
					mix(worldSeed, regionX.toLong(), regionZ.toLong(), candidateBlockHash, candidateTier.ordinal.toLong(), 0x55AA55AA33CC33CCL),
					maxY - minY + 1
				)
			}

			return BlockPos(x, y, z)
		}

		private fun passesRarity(
			worldSeed: Long,
			regionX: Int,
			regionZ: Int,
			candidateBlockHash: Long,
			candidateTier: OreVeinTier,
			regionSpawnDenominator: Int
		): Boolean = positiveMod(
			mix(worldSeed, regionX.toLong(), regionZ.toLong(), candidateBlockHash, candidateTier.ordinal.toLong(), 0x1E3779B97F4A7C15L),
			regionSpawnDenominator
		) == 0

		private fun mix(vararg values: Long): Long {
			var result = 0x4D6E2F7B4A65C123L

			values.forEach { value ->
				result = (result xor value) * 0x5F58476D1CE4E5B9L
				result = java.lang.Long.rotateLeft(result, 27)
				result *= 0x4D6E2F7B4A65C123L
			}

			return result
		}

		private fun positiveMod(value: Long, bound: Int): Int = Math.floorMod(value, bound.toLong()).toInt()

		@JvmField
		val CODEC: MapCodec<OreVeinPlacementModifier> = RecordCodecBuilder.mapCodec { instance ->
			instance.group(
				BuiltInRegistries.BLOCK.byNameCodec().fieldOf("deposit").forGetter(OreVeinPlacementModifier::block),
				OreVeinTier.CODEC.fieldOf("tier").forGetter(OreVeinPlacementModifier::tier)
			).apply(instance, ::OreVeinPlacementModifier)
		}
	}
}
