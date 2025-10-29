package com.createcivilization.create_ore_deposits

import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

class CreateOreDepositsTags {
	companion object{
		val DEPOSIT: TagKey<Block> = BlockTags.create(CreateOreDeposits.asResource("deposit_ore"))
	}
}