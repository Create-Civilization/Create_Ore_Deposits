package com.createcivilization.create_ore_deposits.registry.item

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.CreateOreDeposits.REGISTRATE
import com.createcivilization.create_ore_deposits.util.Item
import com.createcivilization.create_ore_deposits.util.ItemProvider
import com.createcivilization.create_ore_deposits.util.KotlinDeferredRegister
import com.simibubi.create.AllItems
import com.simibubi.create.AllTags
import com.simibubi.create.AllTags.AllItemTags
import com.tterrag.registrate.util.entry.ItemEntry
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
	val UNREFINED_RAW_IRON_ORE_ITEM: ItemEntry<Item> = REGISTRATE.item("unrefined_raw_iron_ore", ::Item).register()
	val CRUSHED_UNREFINED_IRON_ORE_ITEM: ItemEntry<Item> = REGISTRATE.item("crushed_unrefined_iron_ore", ::Item).register()

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


	//Cast TEMP THIS WILL BE BOOFED

	val CAST_ITEM: ItemEntry<Item> = REGISTRATE.item("cast", ::Item).register()

	init { ITEM_PROVIDER.register(MOD_BUS) }
}