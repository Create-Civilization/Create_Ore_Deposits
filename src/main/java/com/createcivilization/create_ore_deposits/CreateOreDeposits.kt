package com.createcivilization.create_ore_deposits

import com.createcivilization.create_ore_deposits.util.logI

import com.simibubi.create.foundation.data.CreateRegistrate

import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.server.ServerStartingEvent

@Mod(CreateOreDeposits.MOD_ID)
class CreateOreDeposits(modEventBus: IEventBus, modContainer: ModContainer) {

	init {
		REGISTRATE.registerEventListeners(modEventBus)
		modEventBus.addListener(this::commonSetup)
		NeoForge.EVENT_BUS.register(this)
	}

	private fun commonSetup(event: FMLCommonSetupEvent?) {
		// Some common setup code
	}

	@SubscribeEvent
	fun onServerStarting(event: ServerStartingEvent?) {
		// Do something when the server starts
		logI("CREATE ORE DEPOSIT GO BRRRRR")
	}

	companion object {

		const val MOD_ID: String = "create_ore_deposits"

		val REGISTRATE: CreateRegistrate = CreateRegistrate.create(MOD_ID)
	}
}