package com.createcivilization.create_ore_deposits.registry.item

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.registry.fluid.CreateOreDepositsFluids
import com.createcivilization.create_ore_deposits.util.Item
import com.createcivilization.create_ore_deposits.util.ItemProvider
import com.createcivilization.create_ore_deposits.util.KotlinDeferredRegister

import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BucketItem
import net.minecraft.world.item.Item

import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object CreateOreDepositsItems {

	@JvmField
	internal val ITEM_PROVIDER: KotlinDeferredRegister<Item> = KotlinDeferredRegister(
		Registries.ITEM,
		CreateOreDeposits.MOD_ID
	)

	// Iron Stuff
	private val _UNREFINED_IRON_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("unrefined_iron_ore", ::Item)
	val UNREFINED_IRON_ORE_ITEM: Item get() = _UNREFINED_IRON_ORE_ITEM()

	private val _UNREFINED_IRON_ORE_POWDER_ITEM: ItemProvider = ITEM_PROVIDER.register("unrefined_iron_ore_powder", ::Item)
	val UNREFINED_IRON_ORE_POWDER_ITEM: Item get() = _UNREFINED_IRON_ORE_POWDER_ITEM()

	private val _IRON_ORE_POWDER_ITEM: ItemProvider = ITEM_PROVIDER.register("iron_ore_powder", ::Item)
	val IRON_ORE_POWDER_ITEM: Item get() = _IRON_ORE_POWDER_ITEM()

	//Gold Stuff
	private val _UNREFINED_GOLD_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("unrefined_gold_ore", ::Item)
	val UNREFINED_GOLD_ORE_ITEM: Item get() = _UNREFINED_GOLD_ORE_ITEM()

	private val _CRUSHED_UNREFINED_GOLD_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("crushed_unrefined_gold_ore", ::Item)
	val CRUSHED_UNREFINED_GOLD_ORE_ITEM: Item get() = _CRUSHED_UNREFINED_GOLD_ORE_ITEM()

	private val _UNREFINED_GOLD_ORE_POWDER: ItemProvider = ITEM_PROVIDER.register("unrefined_gold_ore_powder", ::Item)
	val UNREFINED_GOLD_ORE_POWDER: Item get() = _UNREFINED_GOLD_ORE_POWDER()

	private val _GOLD_DUST_ITEM: ItemProvider = ITEM_PROVIDER.register("gold_dust", ::Item)
	val GOLD_DUST_ITEM: Item get() = _GOLD_DUST_ITEM()


	//Cast TEMP THIS WILL BE BOOFED
	private val _CAST_ITEM: ItemProvider = ITEM_PROVIDER.register("cast", ::Item)
	val CAST_ITEM: Item get() = _CAST_ITEM()

	init { ITEM_PROVIDER.register(MOD_BUS) }
}