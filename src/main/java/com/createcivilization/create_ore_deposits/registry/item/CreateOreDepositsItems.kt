package com.createcivilization.create_ore_deposits.registry.item

import com.createcivilization.create_ore_deposits.CreateOreDeposits.REGISTRATE
import com.createcivilization.create_ore_deposits.registry.tag.CreateOreDepositsTags
import com.tterrag.registrate.util.entry.ItemEntry
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item

object CreateOreDepositsItems {

	val DIAMOND_DRILL_TIP: ItemEntry<Item> = REGISTRATE.item("diamond_drill_tip", ::Item)
		.properties {it.durability(20).stacksTo(1).setNoRepair()}
		.tag(CreateOreDepositsTags.DRILL_TIP)
		.defaultModel()
		.register()

}