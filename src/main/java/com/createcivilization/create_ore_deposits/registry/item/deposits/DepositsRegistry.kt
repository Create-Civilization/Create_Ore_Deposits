package com.createcivilization.create_ore_deposits.registry.item.deposits

import com.createcivilization.create_ore_deposits.util.Block
import com.createcivilization.create_ore_deposits.util.BlockProvider
import com.createcivilization.create_ore_deposits.util.ItemProvider
import com.createcivilization.create_ore_deposits.util.makeBlock

import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object DepositsRegistry {

	private val _EXAMPLE_DEPOSIT: BlockProvider
	val EXAMPLE_DEPOSIT: Block get() = _EXAMPLE_DEPOSIT()
	private val _EXAMPLE_DEPOSIT_ITEM: ItemProvider
	val EXAMPLE_DEPOSIT_ITEM: Item get() = _EXAMPLE_DEPOSIT_ITEM()

	init {
		val (exampleDeposit, exampleDepositItem) = makeBlock("example_deposit", ::Block) { block ->
			BlockItem(block(), Item.Properties())
		}
		_EXAMPLE_DEPOSIT = exampleDeposit
		_EXAMPLE_DEPOSIT_ITEM = exampleDepositItem
	}
}