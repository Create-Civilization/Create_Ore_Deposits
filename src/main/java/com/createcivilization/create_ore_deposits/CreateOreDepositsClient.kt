package com.createcivilization.create_ore_deposits

import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels
import com.createcivilization.create_ore_deposits.util.logI

import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent

import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(value = CreateOreDeposits.MOD_ID, dist = [Dist.CLIENT])
object CreateOreDepositsClient {

	init {
		MOD_BUS.addListener(::clientInit)
	}

	@Suppress("UnusedExpression") // Calls static initialiser
	fun clientInit(event: FMLClientSetupEvent) {
		logI("In client init!")
		DepositDrillBlockModels
//		PonderIndex.addPlugin(CODPonderPlugin())
	}
}