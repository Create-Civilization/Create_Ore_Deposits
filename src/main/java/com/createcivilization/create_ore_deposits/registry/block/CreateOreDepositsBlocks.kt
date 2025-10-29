package com.createcivilization.create_ore_deposits.registry.block

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlock
import com.createcivilization.create_ore_deposits.util.Block
import com.createcivilization.create_ore_deposits.util.BlockProvider
import com.createcivilization.create_ore_deposits.util.ItemProvider
import com.createcivilization.create_ore_deposits.util.KotlinDeferredRegister
import com.createcivilization.create_ore_deposits.util.makeBlock
import com.tterrag.registrate.util.entry.BlockEntry

import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour

import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object CreateOreDepositsBlocks {

	@JvmField
	internal val BLOCK_PROVIDER: KotlinDeferredRegister<Block> = KotlinDeferredRegister(
		Registries.BLOCK,
		CreateOreDeposits.MOD_ID
	)

	private val _EXAMPLE_DEPOSIT: BlockProvider
	val EXAMPLE_DEPOSIT: Block get() = _EXAMPLE_DEPOSIT()
	private val _EXAMPLE_DEPOSIT_ITEM: ItemProvider
	val EXAMPLE_DEPOSIT_ITEM: Item get() = _EXAMPLE_DEPOSIT_ITEM()

	val DRILL_BLOCK: BlockEntry<DepositDrillBlock> = CreateOreDeposits.REGISTRATE.block(
		"deposit_drill"
	) { properties ->
		DepositDrillBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion())
	}
		.simpleItem()
		.register()


	init {
		val (exampleDeposit, exampleDepositItem) = makeBlock("example_deposit", ::Block) { block ->
			BlockItem(block(), Item.Properties())
		}
		_EXAMPLE_DEPOSIT = exampleDeposit
		_EXAMPLE_DEPOSIT_ITEM = exampleDepositItem

		BLOCK_PROVIDER.register(MOD_BUS)
	}
}