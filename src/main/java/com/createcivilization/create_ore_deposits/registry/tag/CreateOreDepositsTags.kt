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
	val NEEDS_GOLD_TIP: TagKey<Block> = BlockTags.create("needs_gold_tip".asResource())
	@JvmField
	val NEEDS_STEEL_TIP: TagKey<Block> = BlockTags.create("needs_steel_tip".asResource())
	@JvmField
	val NEEDS_DIAMOND_TIP: TagKey<Block> = BlockTags.create("needs_diamond_tip".asResource())
	@JvmField
	val DRILL_TIP: TagKey<Item> = ItemTags.create("drill_tip".asResource())
	@JvmField
	val IRON_TIP_TIER: TagKey<Item> = ItemTags.create("iron_tip_tier".asResource())
	@JvmField
	val GOLD_TIP_TIER: TagKey<Item> = ItemTags.create("gold_tip_tier".asResource())
	@JvmField
	val STEEL_TIP_TIER: TagKey<Item> = ItemTags.create("steel_tip_tier".asResource())
	@JvmField
	val DIAMOND_TIP_TIER: TagKey<Item> = ItemTags.create("diamond_tip_tier".asResource())
}
