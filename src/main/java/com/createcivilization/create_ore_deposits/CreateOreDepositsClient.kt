package com.createcivilization.create_ore_deposits

import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels
import net.createmod.ponder.foundation.PonderIndex
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.common.NeoForge
import java.util.function.Consumer


@Mod(value = CreateOreDeposits.MOD_ID, dist = [Dist.CLIENT])
class CreateOreDepositsClient(modEventBus: IEventBus) {
	init {
		onCtorClient(modEventBus)
	}

	companion object {
		fun onCtorClient(modEventBus: IEventBus) {
			val neoEventBus = NeoForge.EVENT_BUS

			modEventBus.addListener(Consumer { event: FMLClientSetupEvent -> clientInit(event) })
		}

		fun clientInit(event: FMLClientSetupEvent?) {
			DepositDrillBlockModels.init()
		}
	}
}