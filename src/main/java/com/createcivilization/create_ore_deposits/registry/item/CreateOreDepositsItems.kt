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

	// Iron Stuff
	private val _UNREFINED_IRON_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("unrefined_iron_ore", ::Item)
	val UNREFINED_IRON_ORE_ITEM: Item get() = _UNREFINED_IRON_ORE_ITEM()

	private val _CRUSHED_UNREFINED_IRON_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("crushed_unrefined_iron_ore", ::Item)
	val CRUSHED_UNREFINED_IRON_ORE_ITEM: Item get() = _CRUSHED_UNREFINED_IRON_ORE_ITEM()

	private val _CRUSHED_IRON_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("crushed_iron_ore", ::Item)
	val CRUSHED_IRON_ORE_ITEM: Item get() = _CRUSHED_IRON_ORE_ITEM()

	//Gold Stuff
	private val _UNREFINED_GOLD_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("unrefined_gold_ore", ::Item)
	val UNREFINED_GOLD_ORE_ITEM: Item get() = _UNREFINED_GOLD_ORE_ITEM()

	private val _CRUSHED_UNREFINED_GOLD_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("crushed_unrefined_gold_ore", ::Item)
	val CRUSHED_UNREFINED_GOLD_ORE_ITEM: Item get() = _CRUSHED_UNREFINED_GOLD_ORE_ITEM()

	private val _CRUSHED_GOLD_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("crushed_gold_ore", ::Item)
	val CRUSHED_GOLD_ORE_ITEM: Item get() = _CRUSHED_GOLD_ORE_ITEM()

	//Copper Stuff
	private val _UNREFINED_COPPER_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("unrefined_copper_ore", ::Item)
	val UNREFINED_COPPER_ORE_ITEM: Item get() = _UNREFINED_COPPER_ORE_ITEM()

	private val _CRUSHED_UNREFINED_COPPER_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("crushed_unrefined_copper_ore", ::Item)
	val CRUSHED_UNREFINED_COPPER_ORE_ITEM: Item get() = _CRUSHED_UNREFINED_COPPER_ORE_ITEM()

	private val _CRUSHED_COPPER_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("crushed_copper_ore", ::Item)
	val CRUSHED_COPPER_ORE_ITEM: Item get() = _CRUSHED_COPPER_ORE_ITEM()

	//Zinc Stuff
	private val _UNREFINED_ZINC_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("unrefined_zinc_ore", ::Item)
	val UNREFINED_ZINC_ORE_ITEM: Item get() = _UNREFINED_ZINC_ORE_ITEM()

	private val _CRUSHED_UNREFINED_ZINC_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("crushed_unrefined_zinc_ore", ::Item)
	val CRUSHED_UNREFINED_ZINC_ORE_ITEM: Item get() = _CRUSHED_UNREFINED_ZINC_ORE_ITEM()

	private val _CRUSHED_ZINC_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("crushed_zinc_ore", ::Item)
	val CRUSHED_ZINC_ORE_ITEM: Item get() = _CRUSHED_ZINC_ORE_ITEM()

	//Netherite Stuff
	private val _UNREFINED_NETHERITE_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("unrefined_netherite_ore", ::Item)
	val UNREFINED_NETHERITE_ORE_ITEM: Item get() = _UNREFINED_NETHERITE_ORE_ITEM()

	private val _CRUSHED_UNREFINED_NETHERITE_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("crushed_unrefined_netherite_ore", ::Item)
	val CRUSHED_UNREFINED_NETHERITE_ORE_ITEM: Item get() = _CRUSHED_UNREFINED_NETHERITE_ORE_ITEM()

	private val _CRUSHED_NETHERITE_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("crushed_netherite_ore", ::Item)
	val CRUSHED_NETHERITE_ORE_ITEM: Item get() = _CRUSHED_NETHERITE_ORE_ITEM()

	//Cast TEMP THIS WILL BE BOOFED
	private val _CAST_ITEM: ItemProvider = ITEM_PROVIDER.register("cast", ::Item)
	val CAST_ITEM: Item get() = _CAST_ITEM()

	init { ITEM_PROVIDER.register(MOD_BUS) }
}