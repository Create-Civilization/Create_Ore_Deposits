package com.createcivilization.create_ore_deposits.registry.block

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.util.Block
import com.createcivilization.create_ore_deposits.util.BlockProvider
import com.createcivilization.create_ore_deposits.util.KotlinDeferredRegister
import com.createcivilization.create_ore_deposits.util.Registries
import com.createcivilization.create_ore_deposits.util.getValue

import net.minecraft.world.level.block.Block

import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object CreateOreDepositBlocks {

	internal val BLOCK_PROVIDER: KotlinDeferredRegister<Block> = KotlinDeferredRegister(
		Registries.BLOCK,
		CreateOreDeposits.MOD_ID
	)

	private val _EXAMPLE_DEPOSIT: BlockProvider = BLOCK_PROVIDER.register("example_deposit", ::Block)
	val EXAMPLE_DEPOSIT: Block by _EXAMPLE_DEPOSIT

	init { BLOCK_PROVIDER.register(MOD_BUS) }
}