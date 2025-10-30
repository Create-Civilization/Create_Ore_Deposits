package com.createcivilization.create_ore_deposits.registry.block

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlock
import com.createcivilization.create_ore_deposits.registry.item.deposits.DepositsRegistry
import com.createcivilization.create_ore_deposits.util.KotlinDeferredRegister

import com.tterrag.registrate.util.entry.BlockEntry

import net.minecraft.core.registries.Registries
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

	@Suppress("ObjectPropertyName")
	internal val _DRILL_BLOCK: BlockEntry<DepositDrillBlock> = CreateOreDeposits.REGISTRATE
		.block("deposit_drill") { DepositDrillBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()) }
		.simpleItem()
		.register()
	val DRILL_BLOCK: DepositDrillBlock get() = _DRILL_BLOCK.get()

	init {
		@Suppress("UnusedExpression") // Calls static initialiser
		DepositsRegistry

		BLOCK_PROVIDER.register(MOD_BUS)
	}
}