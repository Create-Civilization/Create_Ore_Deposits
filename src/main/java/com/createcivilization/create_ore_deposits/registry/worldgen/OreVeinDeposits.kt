package com.createcivilization.create_ore_deposits.registry.worldgen

import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositsBlocks
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest

// All vein tuning now lives in Config.SERVER.ORE_VEINS so end users can edit biome selectors, rarity, height, and size in one place.
// This object only keeps the static per-deposit registration data that does not need to be user-configurable.

// Create's `AllLayerPatterns` mixes palette stone blocks with filler layers. This addon only has one deposit
// block per ore family, so the layer patterns below keep that deposit block as the dominant stratum while
// still using the same alternating-layer structure that `LayeredOreFeature.place()` expects.

data object OreVeinDeposits {

	data class DepositVeinDefinition(
		val name: String,
		val block: BlockEntry<Block>,
		val blockHash: Long,
		val targets: List<OreConfiguration.TargetBlockState>,
		val layerPatterns: List<LayeredDepositPattern>
	)

	@JvmField
	val EXAMPLE_DEPOSIT: DepositVeinDefinition = deposit("example_deposit", CreateOreDepositsBlocks.EXAMPLE_DEPOSIT)

	@JvmField
	val COAL_ORE_DEPOSIT: DepositVeinDefinition = deposit("coal_ore_deposit", CreateOreDepositsBlocks.COAL_ORE_DEPOSIT)

	@JvmField
	val IRON_ORE_DEPOSIT: DepositVeinDefinition = deposit("iron_ore_deposit", CreateOreDepositsBlocks.IRON_ORE_DEPOSIT)

	@JvmField
	val GOLD_ORE_DEPOSIT: DepositVeinDefinition = deposit("gold_ore_deposit", CreateOreDepositsBlocks.GOLD_ORE_DEPOSIT)

	@JvmField
	val COPPER_ORE_DEPOSIT: DepositVeinDefinition = deposit("copper_ore_deposit", CreateOreDepositsBlocks.COPPER_ORE_DEPOSIT)

	@JvmField
	val LAPIS_ORE_DEPOSIT: DepositVeinDefinition = deposit("lapis_ore_deposit", CreateOreDepositsBlocks.LAPIS_ORE_DEPOSIT)

	@JvmField
	val DIAMOND_ORE_DEPOSIT: DepositVeinDefinition = deposit("diamond_ore_deposit", CreateOreDepositsBlocks.DIAMOND_ORE_DEPOSIT)

	@JvmField
	val EMERALD_ORE_DEPOSIT: DepositVeinDefinition = deposit("emerald_ore_deposit", CreateOreDepositsBlocks.EMERALD_ORE_DEPOSIT)

	@JvmField
	val QUARTZ_ORE_DEPOSIT: DepositVeinDefinition = deposit("quartz_ore_deposit", CreateOreDepositsBlocks.QUARTZ_ORE_DEPOSIT)

	@JvmField
	val NETHERITE_ORE_DEPOSIT: DepositVeinDefinition = deposit("netherite_ore_deposit", CreateOreDepositsBlocks.NETHERITE_ORE_DEPOSIT)

	@JvmField
	val DEPOSITS: List<DepositVeinDefinition> = listOf(
		EXAMPLE_DEPOSIT,
		COAL_ORE_DEPOSIT,
		IRON_ORE_DEPOSIT,
		GOLD_ORE_DEPOSIT,
		COPPER_ORE_DEPOSIT,
		LAPIS_ORE_DEPOSIT,
		DIAMOND_ORE_DEPOSIT,
		EMERALD_ORE_DEPOSIT,
		QUARTZ_ORE_DEPOSIT,
		NETHERITE_ORE_DEPOSIT
	)

	private val DEPOSITS_BY_BLOCK: Map<Block, DepositVeinDefinition> = DEPOSITS.associateBy { it.block.get() }

// fails loud on purpose, missing a deposit mapping here means someone added an ore block
// and forgot to wire it up, better to crash in dev than silently break worldgen
	fun byBlock(block: Block): DepositVeinDefinition = DEPOSITS_BY_BLOCK[block]
		?: error("Unsupported ore deposit block in OreVeinDeposits.byBlock(): $block")

	private fun deposit(
		name: String,
		block: BlockEntry<Block>
	): DepositVeinDefinition {
		val defaultState = block.defaultState
		val blockHash = BuiltInRegistries.BLOCK.getKey(block.get()).toString().hashCode().toLong()
		val depositTargets = listOf(
			OreConfiguration.target(
				TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES),
				defaultState
			),
			OreConfiguration.target(
				TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES),
				defaultState
			)
		)

		return DepositVeinDefinition(
			name,
			block,
			blockHash,
			depositTargets,
			defaultOverworldPatterns(depositTargets)
		)
	}

// 3 slightly different layer recipes per ore so not every single deposit of the same ore looks
// identical — one gets picked at random per deposit in OreVeinFeature.placeLayeredDeposit.
// weight controls how much of the total deposit size that layer tends to take up, size range is
// how many "layer units" wide one band is before it rolls the next layer. these numbers are just
// me eyeballing something that looked like create's own deposits, not derived from anything, so
// feel free to retune if it doesn't look right once you've actually seen it spawn in-game.

	private fun defaultOverworldPatterns(
		depositTargets: List<OreConfiguration.TargetBlockState>
	): List<LayeredDepositPattern> = listOf(
		LayeredDepositPattern(
			listOf(
				layer(listOf(depositTargets), 2, 5, 4),
				layer(listOf(targets(Blocks.TUFF, Blocks.DEEPSLATE)), 2, 3, 2),
				layer(listOf(targets(Blocks.DEEPSLATE, Blocks.TUFF)), 2, 2, 1),
				layer(listOf(targets(Blocks.DIORITE)), 1, 2, 1)
			)
		),
		LayeredDepositPattern(
			listOf(
				layer(listOf(depositTargets), 2, 5, 3),
				layer(listOf(targets(Blocks.TUFF)), 2, 3, 2),
				layer(listOf(targets(Blocks.ANDESITE)), 2, 3, 2),
				layer(listOf(targets(Blocks.CALCITE)), 1, 2, 1)
			)
		),
		LayeredDepositPattern(
			listOf(
				layer(listOf(targets(Blocks.STONE, Blocks.DEEPSLATE)), 1, 1, 1),
				layer(listOf(depositTargets), 2, 4, 3),
				layer(listOf(targets(Blocks.CALCITE)), 1, 1, 2),
				layer(listOf(targets(Blocks.DIORITE)), 1, 1, 1)
			)
		)
	)

	private fun targets(
		stoneBlock: Block,
		deepslateBlock: Block = stoneBlock
	): List<OreConfiguration.TargetBlockState> = listOf(
		OreConfiguration.target(
			TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES),
			stoneBlock.defaultBlockState()
		),
		OreConfiguration.target(
			TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES),
			deepslateBlock.defaultBlockState()
		)
	)

	private fun layer(
		targets: List<List<OreConfiguration.TargetBlockState>>,
		minSize: Int,
		maxSize: Int,
		weight: Int
	): LayeredDepositPattern.Layer = LayeredDepositPattern.Layer(
		targets = targets,
		minSize = minSize,
		maxSize = maxSize,
		weight = weight
	)
}
