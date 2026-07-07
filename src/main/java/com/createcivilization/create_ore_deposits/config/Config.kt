@file:Suppress("PropertyName")
package com.createcivilization.create_ore_deposits.config

import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositsBlocks
import com.createcivilization.create_ore_deposits.registry.worldgen.OreVeinTier
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.Block
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.common.ModConfigSpec

data object Config {

	class Server(builder: ModConfigSpec.Builder) {

		val DEPOSIT_DRILL: DepositDrill = run {
			builder.push("deposit_drill")
			val config = DepositDrill(builder)
			builder.pop()
			return@run config
		}
		class DepositDrill(builder: ModConfigSpec.Builder) {

			@PublishedApi
			internal val _baseCooling: ModConfigSpec.DoubleValue = builder.defineInRange("baseCooling", 0.001, 0.0, Double.MAX_VALUE)
			inline val baseCooling: Float get() = _baseCooling.get().toFloat()

			@PublishedApi
			internal val _baseTemperature: ModConfigSpec.DoubleValue =
				builder.defineInRange("baseTemperature", 293.0, -Double.MAX_VALUE, Double.MAX_VALUE)
			inline val baseTemperature: Float get() = _baseTemperature.get().toFloat()
		}

		val ORE_VEINS: OreVeins = run {
			builder.push("ore_veins")
			val config = OreVeins(builder)
			builder.pop()
			return@run config
		}

// these two control the grid the whole cluster system is built on, touch with care.
// region size = how big a "tile" is before we roll for a cluster in it.
// padding = how many empty chunks we leave at the edge of a region so two clusters in
// neighbouring regions can never physically touch. if you shrink padding, go re-check
// regionLayout() math in OreVeinPlacementModifier, it assumes this is at least 1.

		class OreVeins(builder: ModConfigSpec.Builder) {
			companion object {
				const val CLUSTER_REGION_SIZE_CHUNKS: Int = 50
				const val CLUSTER_REGION_PADDING_CHUNKS: Int = 1
			}

			@PublishedApi
			internal val _enableRegionFamilyGate: ModConfigSpec.BooleanValue = builder
				.comment(
    			"true = every ore+tier fights over the same region roll, only one wins (rarer overall, but ores compete with each other).",
    			"false = each region gets reserved for one specific ore+tier up front, so ores don't steal each other's spawns.",
    			"config key stayed 'enableChunkFamilyGate' from before we switched to regions, didn't want to break people's existing configs over a rename."
				)
				.worldRestart()
				.define("enableRegionFamilyGate", true)
			inline val enableRegionFamilyGate: Boolean get() = _enableRegionFamilyGate.get()

			val EXAMPLE_DEPOSIT: Deposit = deposit(builder, "example_deposit", ironDefaults())
			val COAL_ORE_DEPOSIT: Deposit = deposit(builder, "coal_ore_deposit", coalDefaults())
			val IRON_ORE_DEPOSIT: Deposit = deposit(builder, "iron_ore_deposit", ironDefaults())
			val GOLD_ORE_DEPOSIT: Deposit = deposit(builder, "gold_ore_deposit", goldDefaults())
			val COPPER_ORE_DEPOSIT: Deposit = deposit(builder, "copper_ore_deposit", copperDefaults())
			val LAPIS_ORE_DEPOSIT: Deposit = deposit(builder, "lapis_ore_deposit", lapisDefaults())
			val DIAMOND_ORE_DEPOSIT: Deposit = deposit(builder, "diamond_ore_deposit", diamondDefaults())
			val EMERALD_ORE_DEPOSIT: Deposit = deposit(builder, "emerald_ore_deposit", emeraldDefaults())
			val QUARTZ_ORE_DEPOSIT: Deposit = deposit(builder, "quartz_ore_deposit", quartzDefaults())
			val NETHERITE_ORE_DEPOSIT: Deposit = deposit(builder, "netherite_ore_deposit", netheriteDefaults())

			private val DEPOSITS_BY_BLOCK: Map<Block, Deposit> by lazy {
				mapOf(
					CreateOreDepositsBlocks.EXAMPLE_DEPOSIT.get() to EXAMPLE_DEPOSIT,
					CreateOreDepositsBlocks.COAL_ORE_DEPOSIT.get() to COAL_ORE_DEPOSIT,
					CreateOreDepositsBlocks.IRON_ORE_DEPOSIT.get() to IRON_ORE_DEPOSIT,
					CreateOreDepositsBlocks.GOLD_ORE_DEPOSIT.get() to GOLD_ORE_DEPOSIT,
					CreateOreDepositsBlocks.COPPER_ORE_DEPOSIT.get() to COPPER_ORE_DEPOSIT,
					CreateOreDepositsBlocks.LAPIS_ORE_DEPOSIT.get() to LAPIS_ORE_DEPOSIT,
					CreateOreDepositsBlocks.DIAMOND_ORE_DEPOSIT.get() to DIAMOND_ORE_DEPOSIT,
					CreateOreDepositsBlocks.EMERALD_ORE_DEPOSIT.get() to EMERALD_ORE_DEPOSIT,
					CreateOreDepositsBlocks.QUARTZ_ORE_DEPOSIT.get() to QUARTZ_ORE_DEPOSIT,
					CreateOreDepositsBlocks.NETHERITE_ORE_DEPOSIT.get() to NETHERITE_ORE_DEPOSIT
				)
			}

// fails loud on purpose, missing a deposit mapping here means someone added an ore block
// and forgot to wire it up, better to crash in dev than silently break worldgen
			fun byBlock(block: Block): Deposit = DEPOSITS_BY_BLOCK[block]
				?: error("Unsupported ore deposit block in Config.SERVER.ORE_VEINS.byBlock(): ${BuiltInRegistries.BLOCK.getKey(block)}")

			private fun deposit(builder: ModConfigSpec.Builder, name: String, defaults: DepositDefaults): Deposit = run {
				builder.push(name)
				val config = Deposit(builder, defaults)
				builder.pop()
				return@run config
			}

			class Deposit(builder: ModConfigSpec.Builder, defaults: DepositDefaults) {

				val LARGE: Tier = tier(builder, "large", defaults.large)
				val MEDIUM: Tier = tier(builder, "medium", defaults.medium)
				val SMALL: Tier = tier(builder, "small", defaults.small)

				fun forTier(tier: OreVeinTier): Tier = when (tier) {
					OreVeinTier.LARGE -> LARGE
					OreVeinTier.MEDIUM -> MEDIUM
					OreVeinTier.SMALL -> SMALL
				}

				private fun tier(builder: ModConfigSpec.Builder, name: String, defaults: TierDefaults): Tier = run {
					builder.push(name)
					val config = Tier(builder, defaults)
					builder.pop()
					return@run config
				}
			}

			data class DepositDefaults(
				val large: TierDefaults,
				val medium: TierDefaults,
				val small: TierDefaults
			)

// one of these per tier per ore. averageChunksPerCluster is NOT "how far apart deposits are",
// that's chunksBetweenDeposits below. this one is jst "how rare is it that a cluster
// exists at all in this area". easy to mix these two up, I did it myself at least twice.

			data class TierDefaults(
				val averageChunksPerCluster: Int,
				val minY: Int,
				val maxY: Int,
				val minDepositsPerCluster: Int,
				val maxDepositsPerCluster: Int,
				val chunksBetweenDeposits: Int,
				val biomeSelectors: List<String> = listOf("#minecraft:is_overworld")
			)

			class Tier(builder: ModConfigSpec.Builder, defaults: TierDefaults) {

				@PublishedApi
				internal val _biomeSelectors: ModConfigSpec.ConfigValue<List<out String>> = builder
					.comment(
						"Biome ids or biome tags allowed for this tier.",
						"Examples: minecraft:plains, #minecraft:is_overworld"
					)
					.worldRestart()
					.defineListAllowEmpty("biomeSelectors", defaults.biomeSelectors) { value ->
						value is String && isBiomeSelector(value)
					}
				inline val biomeSelectors: List<String> get() = _biomeSelectors.get().map { it.toString() }

				@PublishedApi
				internal val _averageChunksPerCluster: ModConfigSpec.IntValue = builder
					.comment(
						"Average chunk spacing between successful cluster origins for this tier.",
						"This controls how rare clusters are in the world, not how far apart deposits inside one cluster are.",
						"1 tries every chunk, 256 tries about once every 256 chunks, and larger numbers are rarer."
					)
					.worldRestart()
					.defineInRange("averageChunksPerCluster", defaults.averageChunksPerCluster, 0, Int.MAX_VALUE)
				inline val averageChunksPerCluster: Int get() = _averageChunksPerCluster.get()

				@PublishedApi
				internal val _minY: ModConfigSpec.IntValue = builder
					.comment("Lowest Y level for cluster origins in this tier.")
					.worldRestart()
					.defineInRange("minY", defaults.minY, -128, 512)
				inline val minY: Int get() = _minY.get()

				@PublishedApi
				internal val _maxY: ModConfigSpec.IntValue = builder
					.comment("Highest Y level for cluster origins in this tier.")
					.worldRestart()
					.defineInRange("maxY", defaults.maxY, -128, 512)
				inline val maxY: Int get() = _maxY.get()

				@PublishedApi
				internal val _minDepositsPerCluster: ModConfigSpec.IntValue = builder
					.comment("Minimum number of individual layered deposits that a successful cluster should try to place.")
					.worldRestart()
					.defineInRange("minDepositsPerCluster", defaults.minDepositsPerCluster, 1, Int.MAX_VALUE)
				inline val minDepositsPerCluster: Int get() = _minDepositsPerCluster.get()

				@PublishedApi
				internal val _maxDepositsPerCluster: ModConfigSpec.IntValue = builder
					.comment("Maximum number of individual layered deposits that a successful cluster should try to place.")
					.worldRestart()
					.defineInRange("maxDepositsPerCluster", defaults.maxDepositsPerCluster, 1, Int.MAX_VALUE)
				inline val maxDepositsPerCluster: Int get() = _maxDepositsPerCluster.get()

				@PublishedApi
				internal val _chunksBetweenDeposits: ModConfigSpec.IntValue = builder
					.comment(
						"how many chunks apart the individual deposits inside ONE cluster are spread.",
						"this has nothing to do with how rare the cluster itself is, see averageChunksPerCluster for that.",
						"if this number times maxDepositsPerCluster is too big for the region size, we auto-shrink it at runtime and log a warning once — see OreVeinFeature.warnIncompatibleRegionConfig."
					)
					.worldRestart()
					.defineInRange("chunksBetweenDeposits", defaults.chunksBetweenDeposits, 1, 64)
				inline val chunksBetweenDeposits: Int get() = _chunksBetweenDeposits.get()

				private fun isBiomeSelector(value: String): Boolean = value.isNotBlank() && runCatching {
					ResourceLocation.parse(value.removePrefix("#"))
				}.isSuccess
			}

			private fun tierDefaults(
				averageChunksPerCluster: Int,
				minY: Int,
				maxY: Int,
				minDepositsPerCluster: Int,
				maxDepositsPerCluster: Int,
				chunksBetweenDeposits: Int,
				biomeSelectors: List<String> = listOf("#minecraft:is_overworld")
			): TierDefaults = TierDefaults(
				averageChunksPerCluster = averageChunksPerCluster,
				minY = minY,
				maxY = maxY,
				minDepositsPerCluster = minDepositsPerCluster,
				maxDepositsPerCluster = maxDepositsPerCluster,
				chunksBetweenDeposits = chunksBetweenDeposits,
				biomeSelectors = biomeSelectors
			)

			private fun uniformRangeDefaults(minY: Int, maxY: Int): DepositDefaults = DepositDefaults(
				large = tierDefaults(2048, minY, maxY, 6, 9, 2),
				medium = tierDefaults(768, minY, maxY, 3, 5, 2),
				small = tierDefaults(256, minY, maxY, 1, 2, 1)
			)

			private fun coalDefaults(): DepositDefaults = DepositDefaults(
				large = tierDefaults(2048, 136, 320, 6, 9, 2),
				medium = tierDefaults(768, 0, 192, 3, 5, 2),
				small = tierDefaults(256, 0, 192, 1, 2, 1)
			)

			private fun ironDefaults(): DepositDefaults = DepositDefaults(
				large = tierDefaults(2048, 80, 384, 6, 9, 2),
				medium = tierDefaults(768, -24, 56, 3, 5, 2),
				small = tierDefaults(256, -64, 72, 1, 2, 1)
			)

			private fun goldDefaults(): DepositDefaults = DepositDefaults(
				large = tierDefaults(2048, -64, 32, 6, 9, 2),
				medium = tierDefaults(768, -64, 32, 3, 5, 2),
				small = tierDefaults(256, -64, -48, 1, 2, 1)
			)

			private fun copperDefaults(): DepositDefaults = uniformRangeDefaults(-16, 112)

			private fun lapisDefaults(): DepositDefaults = DepositDefaults(
				large = tierDefaults(2048, -64, 64, 6, 9, 2),
				medium = tierDefaults(768, -32, 32, 3, 5, 2),
				small = tierDefaults(256, -64, 64, 1, 2, 1)
			)

			private fun diamondDefaults(): DepositDefaults = DepositDefaults(
				large = tierDefaults(2048, -64, 16, 6, 9, 2),
				medium = tierDefaults(768, -64, -4, 3, 5, 2),
				small = tierDefaults(256, -64, 16, 1, 2, 1)
			)

			private fun emeraldDefaults(): DepositDefaults = uniformRangeDefaults(-16, 480)

			private fun quartzDefaults(): DepositDefaults = uniformRangeDefaults(10, 310)

			private fun netheriteDefaults(): DepositDefaults {
// overworld only mod, so netherite reuses diamond's Y range instead of ancient debris's real (nether) range.
// don't "fix" this back to nether Y values, it's on purpose.
				return diamondDefaults()
			}
		}
	}

	@JvmField val SERVER: Server
	internal val serverSpec: ModConfigSpec

	init {
		ModConfigSpec.Builder().configure(::Server).let {
			SERVER = it.left
			serverSpec = it.right
		}
	}
}
