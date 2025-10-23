package com.createcivilization.create_ore_deposits.registry.block

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.util.Registries

import net.minecraft.world.level.block.Block

import net.neoforged.neoforge.registries.DeferredRegister

import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object CreateOreDepositBlocks {

	internal val BLOCK_PROVIDER: DeferredRegister<Block> = DeferredRegister.create(
		Registries.BLOCK,
		CreateOreDeposits.MOD_ID
	)

	init { BLOCK_PROVIDER.register(MOD_BUS) }
}