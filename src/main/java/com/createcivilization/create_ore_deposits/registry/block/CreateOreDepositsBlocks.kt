package com.createcivilization.create_ore_deposits.registry.block

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.registry.block.entries.DepositDrillBlock
import com.createcivilization.create_ore_deposits.util.Block
import com.createcivilization.create_ore_deposits.util.BlockProvider
import com.createcivilization.create_ore_deposits.util.ItemProvider
import com.createcivilization.create_ore_deposits.util.KotlinDeferredRegister
import com.createcivilization.create_ore_deposits.util.makeBlock

import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object CreateOreDepositsBlocks {

	@JvmField
	internal val BLOCK_PROVIDER: KotlinDeferredRegister<Block> = KotlinDeferredRegister(
		Registries.BLOCK,
		CreateOreDeposits.MOD_ID
	)

	private val _EXAMPLE_DEPOSIT: BlockProvider
	val EXAMPLE_DEPOSIT: Block get() = _EXAMPLE_DEPOSIT()
	private val _EXAMPLE_DEPOSIT_ITEM: ItemProvider
	val EXAMPLE_DEPOSIT_ITEM: Item get() = _EXAMPLE_DEPOSIT_ITEM()

	private val _DEPOSIT_DRILL: BlockProvider
	val DEPOSIT_DRILL: Block get() = _DEPOSIT_DRILL()
	private val _DEPOSIT_DRILL_ITEM: ItemProvider
	val DEPOSIT_DRILL_ITEM: Item get() = _DEPOSIT_DRILL_ITEM()

	init {
		val (exampleDeposit, exampleDepositItem) = makeBlock("example_deposit", ::Block) { block ->
			BlockItem(block(), Item.Properties())
		}
		_EXAMPLE_DEPOSIT = exampleDeposit
		_EXAMPLE_DEPOSIT_ITEM = exampleDepositItem

		val (depositDrill, depositDrillItem) = makeBlock("deposit_drill", ::DepositDrillBlock) { block ->
			BlockItem(block(), Item.Properties())
		}
		_DEPOSIT_DRILL = depositDrill
		_DEPOSIT_DRILL_ITEM = depositDrillItem

		BLOCK_PROVIDER.register(MOD_BUS)
	}
}