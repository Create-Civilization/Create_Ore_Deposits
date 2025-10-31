package com.createcivilization.create_ore_deposits.registry.block

import com.createcivilization.create_ore_deposits.CreateOreDeposits.REGISTRATE
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlock
import com.createcivilization.create_ore_deposits.registry.tab.CreateOreDepositsTabs
import com.createcivilization.create_ore_deposits.util.BlockProvider
import com.createcivilization.create_ore_deposits.util.ItemProvider
import com.simibubi.create.AllBlocks
import com.simibubi.create.AllTags.AllBlockTags
import com.simibubi.create.foundation.data.AssetLookup
import com.simibubi.create.foundation.data.CreateRegistrate
import com.simibubi.create.foundation.data.SharedProperties
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour

object CreateOreDepositsBlocks {

	val EXAMPLE_DEPOSIT: BlockEntry<Block> = REGISTRATE.block("example_deposit", ::Block)
		.initialProperties(SharedProperties::stone)
		.simpleItem()
		.register()


	internal val DRILL_BLOCK: BlockEntry<DepositDrillBlock> = REGISTRATE
		.block("deposit_drill") { DepositDrillBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()) }
		.simpleItem()
		.register()
}