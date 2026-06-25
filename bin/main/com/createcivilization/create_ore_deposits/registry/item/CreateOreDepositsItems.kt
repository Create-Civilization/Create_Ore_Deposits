package com.createcivilization.create_ore_deposits.registry.item

import com.createcivilization.create_ore_deposits.CreateOreDeposits.REGISTRATE
import com.createcivilization.create_ore_deposits.registry.tag.CreateOreDepositsTags

import com.tterrag.registrate.util.entry.ItemEntry

import net.minecraft.world.item.Item

object CreateOreDepositsItems {

	@JvmField
	@Suppress("unused")
	val DIAMOND_DRILL_TIP: ItemEntry<Item> = REGISTRATE.item("diamond_drill_tip", ::Item)
		.properties { it.durability(20000).stacksTo(1).setNoRepair() }
		.tag(CreateOreDepositsTags.DRILL_TIP)
		.defaultModel()
		.register()

	// Iron items
	@JvmField
	val UNREFINED_IRON_ORE: ItemEntry<Item> = REGISTRATE.item("unrefined_iron_ore", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val UNREFINED_IRON_ORE_POWDER: ItemEntry<Item> = REGISTRATE.item("unrefined_iron_ore_powder", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val IRON_ORE_POWDER: ItemEntry<Item> = REGISTRATE.item("iron_ore_powder", ::Item)
		.defaultModel()
		.register()

	// Gold items
	@JvmField
	val UNREFINED_GOLD_ORE: ItemEntry<Item> = REGISTRATE.item("unrefined_gold_ore", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val UNREFINED_GOLD_ORE_POWDER: ItemEntry<Item> = REGISTRATE.item("unrefined_gold_ore_powder", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val GOLD_ORE_POWDER: ItemEntry<Item> = REGISTRATE.item("gold_ore_powder", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val GOLD_DUST: ItemEntry<Item> = REGISTRATE.item("gold_dust", ::Item)
		.defaultModel()
		.register()

	// Copper items
	@JvmField
	val UNREFINED_COPPER_ORE: ItemEntry<Item> = REGISTRATE.item("unrefined_copper_ore", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val UNREFINED_COPPER_ORE_POWDER: ItemEntry<Item> = REGISTRATE.item("unrefined_copper_ore_powder", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val COPPER_ORE_POWDER: ItemEntry<Item> = REGISTRATE.item("copper_ore_powder", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val ZINC_POWDER: ItemEntry<Item> = REGISTRATE.item("zinc_powder", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val COPPER_DUST: ItemEntry<Item> = REGISTRATE.item("copper_dust", ::Item)
		.defaultModel()
		.register()
}