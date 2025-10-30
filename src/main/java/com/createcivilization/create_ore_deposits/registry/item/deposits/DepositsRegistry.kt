package com.createcivilization.create_ore_deposits.registry.item.deposits

import com.createcivilization.create_ore_deposits.util.Block
import com.createcivilization.create_ore_deposits.util.BlockProvider
import com.createcivilization.create_ore_deposits.util.ItemProvider
import com.createcivilization.create_ore_deposits.util.makeBlock

import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object DepositsRegistry {

	val EXAMPLE_DEPOSIT: DepositBlockEntry

	init {
		EXAMPLE_DEPOSIT = makeBlock("example_deposit", ::Block) { block: BlockProvider ->
			BlockItem(block(), Item.Properties())
		}.toEntry()
	}

	fun Pair<BlockProvider, ItemProvider>.toEntry(): DepositBlockEntry = DepositBlockEntry(this.first, this.second)

	data class DepositBlockEntry(
		@JvmField val blockProvider: BlockProvider,
		@JvmField val itemProvider: ItemProvider
	) {

		val block: Block get() = this.blockProvider()

		val item: Item get() = this.itemProvider()
	}
}