package com.createcivilization.create_ore_deposits.registry.item.deposits

import com.createcivilization.create_ore_deposits.util.BlockProvider
import com.createcivilization.create_ore_deposits.util.ItemProvider
import com.createcivilization.create_ore_deposits.util.makeBlock

import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RedStoneOreBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor

object DepositsRegistry {

	@JvmField
	val IRON_ORE_DEPOSIT: DepositBlockEntry = depositBlock(
		name = "iron_ore_deposit",
		copyFrom = Blocks::IRON_ORE
	)

	@JvmField
	val GOLD_ORE_DEPOSIT: DepositBlockEntry = depositBlock(
		name = "gold_ore_deposit",
		copyFrom = Blocks::GOLD_ORE
	)

	@JvmField
	val COPPER_ORE_DEPOSIT: DepositBlockEntry = depositBlock(
		name = "copper_ore_deposit",
		copyFrom = Blocks::COPPER_ORE
	)

	@JvmField
	val ZINC_ORE_DEPOSIT: DepositBlockEntry = _depositBlock(
		name = "zinc_ore_deposit",
		block = {
			Block(
				// Copied this from [AllBlocks.ZINC_ORE], since copying it via [ofFullCopy] is broken.
				BlockBehaviour.Properties.of()
					.mapColor(MapColor.METAL)
					.requiresCorrectToolForDrops()
					.sound(SoundType.STONE)
			)
		}
	)

	@JvmField
	val REDSTONE_ORE_DEPOSIT: DepositBlockEntry = _depositBlock(
		name = "redstone_ore_deposit",
		block = { RedStoneOreBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_ORE)) }
	)

	@JvmField
	val LAPIS_ORE_DEPOSIT: DepositBlockEntry = depositBlock(
		name = "lapis_ore_deposit",
		copyFrom = Blocks::LAPIS_ORE
	)

	@JvmField
	val DIAMOND_ORE_DEPOSIT: DepositBlockEntry = depositBlock(
		name = "diamond_ore_deposit",
		copyFrom = Blocks::DIAMOND_ORE
	)

	@JvmField
	val EMERALD_ORE_DEPOSIT: DepositBlockEntry = depositBlock(
		name = "emerald_ore_deposit",
		copyFrom = Blocks::EMERALD_ORE
	)

	@JvmField
	val QUARTZ_ORE_DEPOSIT: DepositBlockEntry = depositBlock(
		name = "quartz_ore_deposit",
		copyFrom = Blocks::NETHER_QUARTZ_ORE
	)

	@JvmField
	val NETHERITE_ORE_DEPOSIT: DepositBlockEntry = depositBlock(
		name = "netherite_ore_deposit",
		copyFrom = Blocks::ANCIENT_DEBRIS
	)

	private fun depositBlock(
		name: String,
		copyFrom: BlockProvider,
		item: (BlockProvider) -> BlockItem = this::defaultItem
	): DepositBlockEntry = _depositBlock(
		name = name,
		block = { Block(BlockBehaviour.Properties.ofFullCopy(copyFrom())) },
		item = item
	)

	private fun _depositBlock(
		name: String,
		block: BlockProvider,
		item: (BlockProvider) -> BlockItem = this::defaultItem
	): DepositBlockEntry = makeBlock(name, block, item).let(DepositsRegistry::DepositBlockEntry)

	private fun defaultItem(block: BlockProvider): BlockItem = BlockItem(block(), Item.Properties())

	data class DepositBlockEntry(
		@JvmField val blockProvider: BlockProvider,
		@JvmField val itemProvider: ItemProvider
	) {

		constructor(pair: Pair<BlockProvider, ItemProvider>) : this(pair.first, pair.second)

		val block: Block get() = this.blockProvider()

		val item: Item get() = this.itemProvider()
	}
}