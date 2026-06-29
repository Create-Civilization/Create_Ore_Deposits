package com.createcivilization.create_ore_deposits.registry.item

import com.createcivilization.create_ore_deposits.CreateOreDeposits.REGISTRATE
import com.createcivilization.create_ore_deposits.registry.tag.CreateOreDepositsTags

import com.tterrag.registrate.util.entry.ItemEntry

import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.Item

object CreateOreDepositsItems {

// Drill Tips	
	@JvmField
	val DIAMOND_DRILL_TIP: ItemEntry<Item> = REGISTRATE.item("diamond_drill_tip", ::Item)
		.properties { it.durability(20000).stacksTo(1).setNoRepair() }
		.tag(CreateOreDepositsTags.DRILL_TIP)
		.tag(CreateOreDepositsTags.DIAMOND_TIP_TIER)
		.defaultModel()
		.register()

	@JvmField
	val STEEL_DRILL_TIP: ItemEntry<Item> = REGISTRATE.item("steel_drill_tip", ::Item)
		.properties { it.durability(20000).stacksTo(1).setNoRepair() }
		.tag(CreateOreDepositsTags.DRILL_TIP)
		.tag(CreateOreDepositsTags.STEEL_TIP_TIER)
		.defaultModel()
		.register()

	@JvmField
	val IRON_DRILL_TIP: ItemEntry<Item> = REGISTRATE.item("iron_drill_tip", ::Item)
		.properties { it.durability(20000).stacksTo(1).setNoRepair() }
		.tag(CreateOreDepositsTags.DRILL_TIP)
		.tag(CreateOreDepositsTags.IRON_TIP_TIER)
		.defaultModel()
		.register()

		@JvmField
	val GOLD_DRILL_TIP: ItemEntry<Item> = REGISTRATE.item("gold_drill_tip", ::Item)
		.properties { it.durability(20000).stacksTo(1).setNoRepair() }
		.tag(CreateOreDepositsTags.DRILL_TIP)
		.tag(CreateOreDepositsTags.GOLD_TIP_TIER)
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

	// Coal and Steel items
	@JvmField
	val STEEL_INGOT: ItemEntry<Item> = REGISTRATE.item("steel_ingot", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val STEEL_HELMET: ItemEntry<ArmorItem> = REGISTRATE.item("steel_helmet") { properties ->
		ArmorItem(
			CreateOreDepositsArmorMaterials.STEEL,
			ArmorItem.Type.HELMET,
			properties.durability(ArmorItem.Type.HELMET.getDurability(CreateOreDepositsArmorMaterials.STEEL_DURABILITY))
		)
	}
		.register()

	@JvmField
	val STEEL_CHESTPLATE: ItemEntry<ArmorItem> = REGISTRATE.item("steel_chestplate") { properties ->
		ArmorItem(
			CreateOreDepositsArmorMaterials.STEEL,
			ArmorItem.Type.CHESTPLATE,
			properties.durability(ArmorItem.Type.CHESTPLATE.getDurability(CreateOreDepositsArmorMaterials.STEEL_DURABILITY))
		)
	}
		.register()

	@JvmField
	val STEEL_LEGGINGS: ItemEntry<ArmorItem> = REGISTRATE.item("steel_leggings") { properties ->
		ArmorItem(
			CreateOreDepositsArmorMaterials.STEEL,
			ArmorItem.Type.LEGGINGS,
			properties.durability(ArmorItem.Type.LEGGINGS.getDurability(CreateOreDepositsArmorMaterials.STEEL_DURABILITY))
		)
	}
		.register()

	@JvmField
	val STEEL_BOOTS: ItemEntry<ArmorItem> = REGISTRATE.item("steel_boots") { properties ->
		ArmorItem(
			CreateOreDepositsArmorMaterials.STEEL,
			ArmorItem.Type.BOOTS,
			properties.durability(ArmorItem.Type.BOOTS.getDurability(CreateOreDepositsArmorMaterials.STEEL_DURABILITY))
		)
	}
		.register()

	@JvmField
	val UNREFINED_COAL_ORE: ItemEntry<Item> = REGISTRATE.item("unrefined_coal_ore", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val COAL_ROCKS: ItemEntry<Item> = REGISTRATE.item("coal_rocks", ::Item)
		.defaultModel()
		.register()

	// Lapis items
	@JvmField
	val UNREFINED_LAPIS_ORE: ItemEntry<Item> = REGISTRATE.item("unrefined_lapis_ore", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val LAPIS_FRAGMENTS: ItemEntry<Item> = REGISTRATE.item("lapis_fragments", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val LAPIS_DOUGH: ItemEntry<Item> = REGISTRATE.item("lapis_dough", ::Item)
		.defaultModel()
		.register()

	// Diamond items
	@JvmField
	val UNREFINED_DIAMOND_ORE: ItemEntry<Item> = REGISTRATE.item("unrefined_diamond_ore", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val DIAMOND_ORE_CHUNKS: ItemEntry<Item> = REGISTRATE.item("diamond_ore_chunks", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val FERROSILICON_POWDER: ItemEntry<Item> = REGISTRATE.item("ferrosilicon_powder", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val QUARTZ_SHARDS: ItemEntry<Item> = REGISTRATE.item("quartz_shards", ::Item)
		.defaultModel()
		.register()

	// Emerald items
	@JvmField
	val UNREFINED_EMERALD_ORE: ItemEntry<Item> = REGISTRATE.item("unrefined_emerald_ore", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val EMERALD_ORE_CHUNKS: ItemEntry<Item> = REGISTRATE.item("emerald_ore_chunks", ::Item)
		.defaultModel()
		.register()

	// Quartz items
	@JvmField
	val UNREFINED_QUARTZ_ORE: ItemEntry<Item> = REGISTRATE.item("unrefined_quartz_ore", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val UNREFINED_QUARTZ_ORE_POWDER: ItemEntry<Item> = REGISTRATE.item("unrefined_quartz_ore_powder", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val QUARTZ_ORE_POWDER: ItemEntry<Item> = REGISTRATE.item("quartz_ore_powder", ::Item)
		.defaultModel()
		.register()

	// Netherite items
	@JvmField
	val UNREFINED_NETHERITE_ORE: ItemEntry<Item> = REGISTRATE.item("unrefined_netherite_ore", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val NETHERITE_DEBRIS: ItemEntry<Item> = REGISTRATE.item("netherite_debris", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val NETH_INTERMEDIATE_1: ItemEntry<Item> = REGISTRATE.item("neth_intermediate_1", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val NETH_INTERMEDIATE_2: ItemEntry<Item> = REGISTRATE.item("neth_intermediate_2", ::Item)
		.defaultModel()
		.register()

	@JvmField
	val NETH_INTERMEDIATE_3: ItemEntry<Item> = REGISTRATE.item("neth_intermediate_3", ::Item)
		.defaultModel()
		.register()
}
