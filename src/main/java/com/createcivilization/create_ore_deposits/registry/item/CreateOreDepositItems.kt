package com.createcivilization.create_ore_deposits.registry.item

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositBlocks
import com.createcivilization.create_ore_deposits.util.Item
import com.createcivilization.create_ore_deposits.util.ItemProvider
import com.createcivilization.create_ore_deposits.util.KotlinDeferredRegister
import com.createcivilization.create_ore_deposits.util.Registries
import com.createcivilization.create_ore_deposits.util.getValue

import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item

import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object CreateOreDepositItems {

	internal val ITEM_PROVIDER: KotlinDeferredRegister<Item> = KotlinDeferredRegister(
		Registries.ITEM,
		CreateOreDeposits.MOD_ID
	)

	private val _TEST_ITEM: ItemProvider = ITEM_PROVIDER.register("test", ::Item)
	val TEST_ITEM: Item by _TEST_ITEM

	private val _EXAMPLE_DEPOSIT_ITEM: ItemProvider = ITEM_PROVIDER.register("example_deposit") { _ ->
		BlockItem(CreateOreDepositBlocks.EXAMPLE_DEPOSIT, Item.Properties())
	}
	val EXAMPLE_DEPOSIT_ITEM: Item by _EXAMPLE_DEPOSIT_ITEM

	init { ITEM_PROVIDER.register(MOD_BUS) }
}