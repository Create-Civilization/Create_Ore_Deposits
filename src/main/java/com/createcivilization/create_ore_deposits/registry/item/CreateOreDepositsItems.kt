package com.createcivilization.create_ore_deposits.registry.item

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.util.Item
import com.createcivilization.create_ore_deposits.util.ItemProvider
import com.createcivilization.create_ore_deposits.util.KotlinDeferredRegister

import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item

import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object CreateOreDepositsItems {

	@JvmField
	internal val ITEM_PROVIDER: KotlinDeferredRegister<Item> = KotlinDeferredRegister(
		Registries.ITEM,
		CreateOreDeposits.MOD_ID
	)

	private val _TEST_ITEM: ItemProvider = ITEM_PROVIDER.register("test", ::Item)
	val TEST_ITEM: Item get() = _TEST_ITEM()


	//Iron Stuff
	private val _UNREFINED_IRON_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("unrefined_iron_ore", ::Item);
	val UNREFINED_IRON_ORE_ITEM: Item get() = _UNREFINED_IRON_ORE_ITEM();

	private val _UNREFINED_IRON_ORE_POWDER_ITEM: ItemProvider = ITEM_PROVIDER.register("unrefined_iron_ore_powder", ::Item);
	val UNREFINED_IRON_ORE_POWDER_ITEM: Item get() = _UNREFINED_IRON_ORE_POWDER_ITEM();

	private val _IRON_ORE_POWDER_ITEM: ItemProvider = ITEM_PROVIDER.register("iron_ore_powder", ::Item);
	val IRON_ORE_POWDER_ITEM: Item get() = _IRON_ORE_POWDER_ITEM();


	init { ITEM_PROVIDER.register(MOD_BUS) }
}