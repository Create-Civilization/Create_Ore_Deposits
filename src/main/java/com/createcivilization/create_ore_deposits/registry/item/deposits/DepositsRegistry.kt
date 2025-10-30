package com.createcivilization.create_ore_deposits.registry.item.deposits

import com.createcivilization.create_ore_deposits.util.BlockProvider
import com.createcivilization.create_ore_deposits.util.ItemProvider
import com.createcivilization.create_ore_deposits.util.makeBlock

import com.simibubi.create.AllBlocks

import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour

object DepositsRegistry {

	val IRON_ORE_DEPOSIT: DepositBlockEntry = depositBlock(
		name = "iron_ore_deposit",
		copyFrom = Blocks::IRON_ORE
	)

	val GOLD_ORE_DEPOSIT: DepositBlockEntry = depositBlock(
		name = "gold_ore_deposit",
		copyFrom = Blocks::GOLD_ORE
	)

	val COPPER_ORE_DEPOSIT: DepositBlockEntry = depositBlock(
		name = "copper_ore_deposit",
		copyFrom = Blocks::COPPER_ORE
	)

//	val ZINC_ORE_DEPOSIT: DepositBlockEntry = depositBlock(
//		name = "zinc_ore_deposit",
//		copyFrom = AllBlocks.ZINC_ORE::get
//	)

//	val REDSTONE_ORE_DEPOSIT: DepositBlockEntry = depositBlock(
//		name = "redstone_ore_deposit",
//		copyFrom = Blocks::REDSTONE_ORE
//	)

	val LAPIS_ORE_DEPOSIT: DepositBlockEntry = depositBlock(
		name = "lapis_ore_deposit",
		copyFrom = Blocks::LAPIS_ORE
	)

	val DIAMOND_ORE_DEPOSIT: DepositBlockEntry = depositBlock(
		name = "diamond_ore_deposit",
		copyFrom = Blocks::DIAMOND_ORE
	)

	val EMERALD_ORE_DEPOSIT: DepositBlockEntry = depositBlock(
		name = "emerald_ore_deposit",
		copyFrom = Blocks::EMERALD_ORE
	)

	val QUARTZ_ORE_DEPOSIT: DepositBlockEntry = depositBlock(
		name = "quartz_ore_deposit",
		copyFrom = Blocks::NETHER_QUARTZ_ORE
	)

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
	): DepositBlockEntry = makeBlock(name, block, item).toEntry()

	private fun defaultItem(block: BlockProvider): BlockItem = BlockItem(block(), Item.Properties())

	fun Pair<BlockProvider, ItemProvider>.toEntry(): DepositBlockEntry = DepositBlockEntry(this.first, this.second)

	data class DepositBlockEntry(
		@JvmField val blockProvider: BlockProvider,
		@JvmField val itemProvider: ItemProvider
	) {

		val block: Block get() = this.blockProvider()

		val item: Item get() = this.itemProvider()
	}
}