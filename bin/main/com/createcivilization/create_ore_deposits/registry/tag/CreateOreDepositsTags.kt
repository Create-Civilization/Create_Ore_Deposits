package com.createcivilization.create_ore_deposits.registry.tag

import com.createcivilization.create_ore_deposits.util.asResource

import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

data object CreateOreDepositsTags {

	@JvmField
	val DEPOSIT: TagKey<Block> = BlockTags.create("deposit_ore".asResource())
	@JvmField
	val DRILL_TIP: TagKey<Item> = ItemTags.create("drill_tip".asResource())
}