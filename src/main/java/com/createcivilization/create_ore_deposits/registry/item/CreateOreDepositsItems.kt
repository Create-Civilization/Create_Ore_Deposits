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
}