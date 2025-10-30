package com.createcivilization.create_ore_deposits

import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels

import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent

@Mod(value = CreateOreDeposits.MOD_ID, dist = [Dist.CLIENT])
class CreateOreDepositsClient(modEventBus: IEventBus) {

	init {
		modEventBus.addListener(::clientInit)
	}

	@Suppress("UnusedExpression") // Calls static initialiser
	fun clientInit(event: FMLClientSetupEvent) {
		DepositDrillBlockModels
//		PonderIndex.addPlugin(CODPonderPlugin())
	}
}