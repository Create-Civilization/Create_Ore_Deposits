package com.createcivilization.create_ore_deposits

import com.createcivilization.create_ore_deposits.util.resource

import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

object CreateOreDepositsTags {

	val DEPOSIT: TagKey<Block> = BlockTags.create("deposit_ore".resource())
}