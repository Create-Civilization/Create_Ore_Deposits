package com.createcivilization.create_ore_deposits.registry.block

import com.createcivilization.create_ore_deposits.CreateOreDeposits.REGISTRATE
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlock
import com.simibubi.create.AllBlocks
import com.simibubi.create.foundation.data.SharedProperties
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour

object CreateOreDepositsBlocks {

	val EXAMPLE_DEPOSIT: BlockEntry<Block> = REGISTRATE.block("example_deposit", ::Block)
		.initialProperties(SharedProperties::stone) // Specify your custom tab
		.simpleItem()
		.register()


	val DRILL_BLOCK: BlockEntry<DepositDrillBlock> = REGISTRATE
		.block("deposit_drill") { DepositDrillBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()) }
		.simpleItem()
		.register()

	//Deposits
	val IRON_ORE_DEPOSIT: BlockEntry<Block> = REGISTRATE
		.block("iron_ore_deposit") {Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE))}
		.simpleItem()
		.register()

	val GOLD_ORE_DEPOSIT: BlockEntry<Block> = REGISTRATE
		.block("gold_ore_deposit") { Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_ORE))}
		.simpleItem()
		.register()

	val COPPER_ORE_DEPOSIT: BlockEntry<Block> = REGISTRATE
		.block("copper_ore_deposit") {Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE))}
		.simpleItem()
		.register()

	val REDSTONE_ORE_DEPOSIT: BlockEntry<Block> = REGISTRATE
		.block("redstone_ore_deposit") {Block(BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_ORE))}
		.simpleItem()
		.register()

	val LAPIS_ORE_DEPOSIT: BlockEntry<Block> = REGISTRATE
		.block("lapis_ore_deposit") {Block(BlockBehaviour.Properties.ofFullCopy(Blocks.LAPIS_ORE))}
		.simpleItem()
		.register()

	val DIAMOND_ORE_DEPOSIT: BlockEntry<Block> = REGISTRATE
		.block("diamond_ore_deposit") { Block(BlockBehaviour.Properties.ofFullCopy(Blocks.LAPIS_ORE))}
		.simpleItem()
		.register()

	val EMERALD_ORE_DEPOSIT: BlockEntry<Block> = REGISTRATE
		.block("emerald_ore_deposit") { Block(BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_ORE))}
		.simpleItem()
		.register()

	val QUARTZ_ORE_DEPOSIT: BlockEntry<Block> = REGISTRATE
		.block("quartz_ore_deposit") { Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_QUARTZ_ORE))}
		.simpleItem()
		.register()

	val NETHERITE_ORE_DEPOSIT: BlockEntry<Block> = REGISTRATE
		.block("netherite_ore_deposit") {Block(BlockBehaviour.Properties.ofFullCopy(Blocks.ANCIENT_DEBRIS))}
		.simpleItem()
		.register()

	val ZINC_ORE_DEPOSIT: BlockEntry<Block> = REGISTRATE
		.block("zinc_ore_deposit") { Block(BlockBehaviour.Properties.ofFullCopy(AllBlocks.ZINC_ORE.get()))}
		.simpleItem()
		.register()

	//End Deposits
	}