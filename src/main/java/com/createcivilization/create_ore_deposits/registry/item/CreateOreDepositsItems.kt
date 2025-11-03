package com.createcivilization.create_ore_deposits.registry.item

import com.createcivilization.create_ore_deposits.CreateOreDeposits.REGISTRATE
import com.tterrag.registrate.util.entry.ItemEntry
import net.minecraft.world.item.Item

object CreateOreDepositsItems {

	// Iron Stuff
	val UNREFINED_RAW_IRON_ORE_ITEM: ItemEntry<Item> = REGISTRATE.item("unrefined_raw_iron_ore", ::Item).register()
	val CRUSHED_UNREFINED_RAW_IRON_ORE_ITEM: ItemEntry<Item> = REGISTRATE.item("crushed_unrefined_raw_iron_ore", ::Item).register()

	// Gold Stuff
	val UNREFINED_RAW_GOLD_ORE_ITEM: ItemEntry<Item> = REGISTRATE.item("unrefined_raw_gold_ore", ::Item).register()
	val CRUSHED_UNREFINED_RAW_GOLD_ORE_ITEM: ItemEntry<Item> = REGISTRATE.item("crushed_unrefined_raw_gold_ore", ::Item).register()

	// Copper Stuff
	val UNREFINED_RAW_COPPER_ORE_ITEM: ItemEntry<Item> = REGISTRATE.item("unrefined_raw_copper_ore", ::Item).register()
	val CRUSHED_UNREFINED_RAW_COPPER_ORE_ITEM: ItemEntry<Item> = REGISTRATE.item("crushed_unrefined_raw_copper_ore", ::Item).register()

	// Zinc Stuff
	val UNREFINED_RAW_ZINC_ORE_ITEM: ItemEntry<Item> = REGISTRATE.item("unrefined_raw_zinc_ore", ::Item).register()
	val CRUSHED_UNREFINED_RAW_ZINC_ORE_ITEM: ItemEntry<Item> = REGISTRATE.item("crushed_unrefined_raw_zinc_ore", ::Item).register()

	// Netherite Stuff
	val UNREFINED_NETHERITE_ORE_ITEM: ItemEntry<Item> = REGISTRATE.item("unrefined_netherite_ore", ::Item).register()
	val CRUSHED_UNREFINED_NETHERITE_ORE_ITEM: ItemEntry<Item> = REGISTRATE.item("crushed_unrefined_netherite_ore", ::Item).register()
	val CRUSHED_NETHERITE_ORE_ITEM: ItemEntry<Item> = REGISTRATE.item("crushed_netherite_ore", ::Item).register()

	// Quartz
	val UNREFINED_QUARTZ_ITEM: ItemEntry<Item> = REGISTRATE.item("unrefined_quartz_ore", ::Item).register()
	val QUARTZ_FRAGMENT_ITEM: ItemEntry<Item> = REGISTRATE.item("quartz_fragment", ::Item).register()

	// Redstone
	val UNREFINED_REDSTONE_ITEM: ItemEntry<Item> = REGISTRATE.item("unrefined_redstone_ore", ::Item).register()
	val REDSTONE_CHUNK_ITEM: ItemEntry<Item> = REGISTRATE.item("redstone_chunk", ::Item).register()

	//Cast TEMP THIS WILL BE BOOFED

	val CAST_ITEM: ItemEntry<Item> = REGISTRATE.item("cast", ::Item).register()
}