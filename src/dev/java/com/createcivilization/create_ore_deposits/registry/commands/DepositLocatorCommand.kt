package com.createcivilization.create_ore_deposits.registry.commands

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.config.Config
import com.createcivilization.create_ore_deposits.registry.worldgen.OreVeinDeposits
import com.createcivilization.create_ore_deposits.registry.worldgen.OreVeinPlacementModifier
import com.createcivilization.create_ore_deposits.registry.worldgen.OreVeinTier
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent
import kotlin.math.sqrt

/*
 * Dev-only debug command for verifying that deposit cluster generation is working.
 *
 * This file lives under `src/dev/java`, and `build.gradle` only attaches the `dev`
 * source set to NeoForge run configs. That keeps the command out of production jars
 * while still making it available in IDE/dev launches.
 */
@Suppress("unused", "DEPRECATION")
@EventBusSubscriber(modid = CreateOreDeposits.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
data object DepositLocatorCommand {

	private const val DEFAULT_RADIUS = 128

	@SubscribeEvent
	fun onRegisterCommands(event: RegisterCommandsEvent) {
		event.dispatcher.register(
			Commands.literal("deposits")
				.requires { src -> src.hasPermission(2) }
				.then(
					Commands.literal("locate")
						.executes { ctx ->
							locate(ctx.source, null, DEFAULT_RADIUS)
							1
						}
						.then(
							Commands.argument("radius", IntegerArgumentType.integer(1, 512))
								.executes { ctx ->
									locate(ctx.source, null, IntegerArgumentType.getInteger(ctx, "radius"))
									1
								}
						)
						.then(
							Commands.argument("deposit", StringArgumentType.word())
								.suggests { _, builder ->
									OreVeinDeposits.DEPOSITS.forEach { deposit -> builder.suggest(deposit.name) }
									builder.buildFuture()
								}
								.executes { ctx ->
									locate(ctx.source, StringArgumentType.getString(ctx, "deposit"), DEFAULT_RADIUS)
									1
								}
								.then(
									Commands.argument("radius", IntegerArgumentType.integer(1, 512))
										.executes { ctx ->
											locate(
												ctx.source,
												StringArgumentType.getString(ctx, "deposit"),
												IntegerArgumentType.getInteger(ctx, "radius")
											)
											1
										}
								)
						)
				)
				.then(
					Commands.literal("stats")
						.executes { ctx ->
							printStats(ctx.source)
							1
						}
				)
		)
	}

	private fun locate(source: CommandSourceStack, depositFilter: String?, radiusChunks: Int) {
		val targetDeposits = resolveDepositFilter(source, depositFilter) ?: return

		val level = source.level
		val worldSeed = level.seed
		val minGenY = level.minBuildHeight
		val maxGenY = level.maxBuildHeight - 1
		val playerPos = source.position
		val playerChunkX = playerPos.x.toInt() shr 4
		val playerChunkZ = playerPos.z.toInt() shr 4
		val useFamilyGate = Config.SERVER.ORE_VEINS.enableRegionFamilyGate
		val diameter = radiusChunks * 2 + 1

		source.sendSystemMessage(Component.literal("Scanning ${diameter}x${diameter} chunk area (${diameter * diameter} chunks)..."))

		val results = mutableListOf<Triple<OreVeinDeposits.DepositVeinDefinition, OreVeinTier, BlockPos>>()

		for (dx in -radiusChunks..radiusChunks) {
			for (dz in -radiusChunks..radiusChunks) {
				val result = OreVeinPlacementModifier.wouldClusterSpawnAt(
					worldSeed,
					playerChunkX + dx,
					playerChunkZ + dz,
					minGenY,
					maxGenY,
					targetDeposits,
					useFamilyGate
				) ?: continue

				results += result
			}
		}

		results.sortBy { (_, _, pos) ->
			val dx = pos.x - playerPos.x
			val dz = pos.z - playerPos.z
			dx * dx + dz * dz
		}

		val filterLabel = if (depositFilter != null) "\"$depositFilter\"" else "all deposit types"
		source.sendSystemMessage(
			Component.literal("Found ${results.size} cluster origin(s) for $filterLabel within $radiusChunks chunks.")
		)

		if (results.isEmpty()) {
			source.sendSystemMessage(
				Component.literal(
					"Nothing found — this is expected if the radius is small relative to averageChunksPerCluster. " +
					"Try /deposits stats to see spacing values, or increase radius (max 512)."
				)
			)
			return
		}

		results.take(8).forEach { (deposit, tier, pos) ->
			val dist = sqrt(
				(pos.x - playerPos.x).let { it * it } + (pos.z - playerPos.z).let { it * it }
			).toInt()
			val label = "[${deposit.name}] [${tier.getSerializedName()}]"
			val coords = "${pos.x} ${pos.y} ${pos.z}"

			val line: MutableComponent = Component.literal("  $label at $coords (~${dist}b away)")
				.withStyle { style ->
					style
						.withClickEvent(ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s $coords"))
						.withUnderlined(true)
				}
			source.sendSystemMessage(line)
		}

		if (results.size > 8) {
			source.sendSystemMessage(Component.literal("  ... and ${results.size - 8} more. Use a smaller radius to narrow results."))
		}
	}

	private fun printStats(source: CommandSourceStack) {
		val useFamilyGate = Config.SERVER.ORE_VEINS.enableRegionFamilyGate

		source.sendSystemMessage(Component.literal("=== Deposit Cluster Config (averageChunksPerCluster = target chunk spacing) ==="))

		OreVeinDeposits.DEPOSITS.forEach { deposit ->
			OreVeinTier.entries.forEach { tier ->
				val cfg = Config.SERVER.ORE_VEINS.byBlock(deposit.block.get()).forTier(tier)
				val spacing = cfg.averageChunksPerCluster
				val depositsPerCluster = "${cfg.minDepositsPerCluster}-${cfg.maxDepositsPerCluster}"
				source.sendSystemMessage(
					Component.literal(
						"  ${deposit.name} [${tier.getSerializedName()}]: " +
							"avg spacing ~$spacing chunks, " +
							"$depositsPerCluster deposits/cluster, " +
							"Y ${cfg.minY}-${cfg.maxY}"
					)
				)
			}
		}

		if (useFamilyGate) {
			source.sendSystemMessage(
				Component.literal(
					"[enableRegionFamilyGate=ON] Only one deposit type wins per region. " +
						"Effective spawn rate per type is reduced by competition between ore+tier candidates - " +
						"this is intentional, not a bug."
				)
			)
		} else {
			source.sendSystemMessage(Component.literal("[enableRegionFamilyGate=OFF] One reserved ore+tier candidate is tested per region instead of competitive region selection."))
		}

		source.sendSystemMessage(
			Component.literal(
				"Tip: run \"/deposits locate <deposit_name> 512\" to confirm a type is generating at all. " +
					"512 chunks covers ~1M chunk area which should contain multiple clusters for any spacing <= 10000."
			)
		)
	}

	private fun resolveDepositFilter(
		source: CommandSourceStack,
		depositFilter: String?
	): List<OreVeinDeposits.DepositVeinDefinition>? {
		if (depositFilter == null) return OreVeinDeposits.DEPOSITS

		val matches = OreVeinDeposits.DEPOSITS.filter { it.name == depositFilter }
		if (matches.isEmpty()) {
			source.sendFailure(
				Component.literal(
					"Unknown deposit: \"$depositFilter\". " +
						"Valid names: ${OreVeinDeposits.DEPOSITS.joinToString { it.name }}"
				)
			)
			return null
		}
		return matches
	}
}
