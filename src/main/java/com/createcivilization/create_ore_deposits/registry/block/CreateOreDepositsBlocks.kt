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

	// Deposits
	val IRON_ORE_DEPOSIT: BlockEntry<Block> = registerDeposit("iron_ore_deposit", Blocks.IRON_ORE)
	val GOLD_ORE_DEPOSIT: BlockEntry<Block> = registerDeposit("gold_ore_deposit", Blocks.GOLD_ORE)
	val COPPER_ORE_DEPOSIT: BlockEntry<Block> = registerDeposit("copper_ore_deposit", Blocks.COPPER_ORE)
//	val REDSTONE_ORE_DEPOSIT: BlockEntry<Block> = registerDeposit("redstone_ore_deposit", Blocks.REDSTONE_ORE)
	val LAPIS_ORE_DEPOSIT: BlockEntry<Block> = registerDeposit("lapis_ore_deposit", Blocks.LAPIS_ORE)
	val DIAMOND_ORE_DEPOSIT: BlockEntry<Block> = registerDeposit("diamond_ore_deposit", Blocks.DIAMOND_ORE)
	val EMERALD_ORE_DEPOSIT: BlockEntry<Block> = registerDeposit("emerald_ore_deposit", Blocks.EMERALD_ORE)
	val QUARTZ_ORE_DEPOSIT: BlockEntry<Block> = registerDeposit("quartz_ore_deposit", Blocks.NETHER_QUARTZ_ORE)
	val NETHERITE_ORE_DEPOSIT: BlockEntry<Block> = registerDeposit("netherite_ore_deposit", Blocks.ANCIENT_DEBRIS)
//	val ZINC_ORE_DEPOSIT: BlockEntry<Block> = registerDeposit("zinc_ore_deposit", AllBlocks.ZINC_ORE.get())
	//End Deposits

	fun registerDeposit(blockName: String, block: Block): BlockEntry<Block> {
		return REGISTRATE
			.block(blockName) { Block(BlockBehaviour.Properties.ofFullCopy(block)) }
			.simpleItem()
			.register()
	}

	}