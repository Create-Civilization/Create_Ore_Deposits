package com.createcivilization.create_ore_deposits.registry.block

import com.createcivilization.create_ore_deposits.CreateOreDeposits.REGISTRATE
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlock
import com.createcivilization.create_ore_deposits.registry.tag.CreateOreDepositsTags
import com.simibubi.create.AllTags
import com.simibubi.create.api.behaviour.movement.MovementBehaviour.movementBehaviour
import com.simibubi.create.api.stress.BlockStressValues
import com.simibubi.create.content.kinetics.drill.DrillMovementBehaviour
import com.simibubi.create.foundation.data.BlockStateGen
import com.simibubi.create.foundation.data.ModelGen.customItemModel

import com.simibubi.create.foundation.data.SharedProperties
import com.simibubi.create.foundation.data.TagGen.axeOrPickaxe

import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue

object CreateOreDepositsBlocks {

	val EXAMPLE_DEPOSIT: BlockEntry<Block> = REGISTRATE.block("example_deposit", ::Block)
		.initialProperties(SharedProperties::stone) // Specify your custom tab
		.simpleItem()
		.register()


	val DRILL_BLOCK: BlockEntry<DepositDrillBlock> = REGISTRATE.block("deposit_drill", ::DepositDrillBlock)
		.initialProperties(SharedProperties::stone)
		.properties { it.mapColor(MapColor.PODZOL).noOcclusion() }
		.transform(axeOrPickaxe())
		.onRegister(movementBehaviour(DrillMovementBehaviour()))
		.onRegister { b -> BlockStressValues.IMPACTS.register(b) { 4.0 } }
		.item()
		.transform(customItemModel())
		.register()

	// TEMP LOOT VALUES, CHANGE LATER.
	// Deposits
	val COAL_ORE_DEPOSIT: BlockEntry<Block> = registerDeposit("coal_ore_deposit", Blocks.COAL_ORE, Items.COAL, 3f, 0.8f)
	val IRON_ORE_DEPOSIT: BlockEntry<Block> = registerDepositSingleRoll("iron_ore_deposit", Blocks.IRON_ORE, Items.RAW_IRON, 0.8f)
	val GOLD_ORE_DEPOSIT: BlockEntry<Block> = registerDepositSingleRoll("gold_ore_deposit", Blocks.GOLD_ORE, Items.RAW_GOLD, 0.6f)
	val COPPER_ORE_DEPOSIT: BlockEntry<Block> = registerDepositSingleRoll("copper_ore_deposit", Blocks.COPPER_ORE, Items.RAW_COPPER, 0.8f)

//	val REDSTONE_ORE_DEPOSIT: BlockEntry<Block> = registerDeposit("redstone_ore_deposit", Blocks.REDSTONE_ORE, Items.REDSTONE_ORE)
	val LAPIS_ORE_DEPOSIT: BlockEntry<Block> = registerDepositGuaranteed("lapis_ore_deposit", Blocks.LAPIS_ORE, Items.LAPIS_LAZULI)
	val DIAMOND_ORE_DEPOSIT: BlockEntry<Block> = registerDepositSingleRoll("diamond_ore_deposit", Blocks.DIAMOND_ORE, Items.DIAMOND, 0.2f)
	val EMERALD_ORE_DEPOSIT: BlockEntry<Block> = registerDepositSingleRoll("emerald_ore_deposit", Blocks.EMERALD_ORE, Items.EMERALD, 0.1f)
	val QUARTZ_ORE_DEPOSIT: BlockEntry<Block> = registerDeposit("quartz_ore_deposit", Blocks.NETHER_QUARTZ_ORE, Items.QUARTZ, 2f, 0.9f)
	val NETHERITE_ORE_DEPOSIT: BlockEntry<Block> = registerDepositSingleRoll("netherite_ore_deposit", Blocks.ANCIENT_DEBRIS, Items.NETHERITE_SCRAP, 1f)
//	val ZINC_ORE_DEPOSIT: BlockEntry<Block> = registerDeposit("zinc_ore_deposit", AllBlocks.ZINC_ORE.get(), AllItems.RAW_ZINC)
	//End Deposits

	fun registerDepositGuaranteed(
		blockName: String, block: Block, ore: Item
	): BlockEntry<Block> =
		registerDepositSingleRoll(blockName, block, ore, 1f)

	fun registerDepositSingleRoll(
		blockName: String, block: Block, ore: Item, chance: Float
	): BlockEntry<Block> =
		registerDeposit(blockName, block, ore, 1f, chance)

	fun registerDeposit(
		blockName: String, block: Block, ore: Item, rolls: Float, chance: Float
	): BlockEntry<Block> = REGISTRATE
		.block(blockName) { Block(BlockBehaviour.Properties.ofFullCopy(block)) }
		.simpleItem()
		.tag(CreateOreDepositsTags.DEPOSIT)
		.loot({ lootTables, depositBlock -> lootTables.add(
			depositBlock,
			LootTable.lootTable()
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(rolls))
						.add(
							LootItem.lootTableItem(ore)
								.`when`(LootItemRandomChanceCondition.randomChance(chance))
						)
				)
		)})
		.register()
}