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
	private val _UNREFINED_RAW_IRON_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("unrefined_raw_iron_ore", ::Item)
	val UNREFINED_RAW_IRON_ORE_ITEM: Item get() = _UNREFINED_RAW_IRON_ORE_ITEM()

	private val _CRUSHED_UNREFINED_IRON_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("crushed_unrefined_iron_ore", ::Item)
	val CRUSHED_UNREFINED_IRON_ORE_ITEM: Item get() = _CRUSHED_UNREFINED_IRON_ORE_ITEM()


	//Gold Stuff
	private val _UNREFINED_RAW_GOLD_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("unrefined_raw_gold_ore", ::Item)
	val UNREFINED_RAW_GOLD_ORE_ITEM: Item get() = _UNREFINED_RAW_GOLD_ORE_ITEM()

	private val _CRUSHED_UNREFINED_RAW_GOLD_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("crushed_unrefined_raw_gold_ore", ::Item)
	val CRUSHED_UNREFINED_RAW_GOLD_ORE_ITEM: Item get() = _CRUSHED_UNREFINED_RAW_GOLD_ORE_ITEM()


	//Copper Stuff
	private val _UNREFINED_RAW_COPPER_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("unrefined_raw_copper_ore", ::Item)
	val UNREFINED_RAW_COPPER_ORE_ITEM: Item get() = _UNREFINED_RAW_COPPER_ORE_ITEM()

	private val _CRUSHED_UNREFINED_RAW_COPPER_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("crushed_unrefined_raw_copper_ore", ::Item)
	val CRUSHED_UNREFINED_RAW_COPPER_ORE_ITEM: Item get() = _CRUSHED_UNREFINED_RAW_COPPER_ORE_ITEM()

	//Zinc Stuff
	private val _UNREFINED_RAW_ZINC_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("unrefined_raw_zinc_ore", ::Item)
	val UNREFINED_RAW_ZINC_ORE_ITEM: Item get() = _UNREFINED_RAW_ZINC_ORE_ITEM()

	private val _CRUSHED_UNREFINED_RAW_ZINC_ORE_ITEM: ItemProvider = ITEM_PROVIDER.register("crushed_unrefined_raw_zinc_ore", ::Item)
	val CRUSHED_UNREFINED_RAW_ZINC_ORE_ITEM: Item get() = _CRUSHED_UNREFINED_RAW_ZINC_ORE_ITEM()

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