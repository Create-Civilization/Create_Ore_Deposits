package com.createcivilization.create_ore_deposits.registry.item.deposits

import com.createcivilization.create_ore_deposits.util.BlockProvider
import com.createcivilization.create_ore_deposits.util.ItemProvider
import com.createcivilization.create_ore_deposits.util.makeBlock

import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour

object DepositsRegistry {

	val EXAMPLE_DEPOSIT: DepositBlockEntry = depositBlock(
		name = "example_deposit",
		copyFrom = Blocks.IRON_ORE
	)

	private fun depositBlock(
		name: String,
		copyFrom: Block,
		item: (BlockProvider) -> BlockItem = this::defaultItem
	): DepositBlockEntry = depositBlock(
		name = name,
		block = { Block(BlockBehaviour.Properties.ofFullCopy(copyFrom)) },
		item = item
	)

	private fun depositBlock(
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