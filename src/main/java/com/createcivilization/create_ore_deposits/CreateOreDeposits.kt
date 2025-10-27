@file:Suppress("UnusedExpression") // Calling the objects implicitly invokes their initialisers
package com.createcivilization.create_ore_deposits

import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositsBlockEntities
import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositsBlocks
import com.createcivilization.create_ore_deposits.registry.item.CreateOreDepositsItems
import com.createcivilization.create_ore_deposits.util.logI
import com.simibubi.create.foundation.data.CreateRegistrate
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.event.server.ServerStartingEvent
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS


@Mod(CreateOreDeposits.MOD_ID)
object CreateOreDeposits {

	val REGISTRATE: CreateRegistrate = CreateRegistrate.create(MOD_ID)

	init {
		MOD_BUS.addListener(this::commonSetup)

		REGISTRATE.registerEventListeners(MOD_BUS)

		FORGE_BUS.register(this)
		CreateOreDepositsItems
		CreateOreDepositsBlocks
		CreateOreDepositsBlockEntities
	}

	private fun commonSetup(event: FMLCommonSetupEvent) {
		// Some common setup code
	}

	@SubscribeEvent
	fun onServerStarting(event: ServerStartingEvent) {
		logI("Starting server...")

		logI("Server started successfully.")
	}

	const val MOD_ID: String = "create_ore_deposits"

	fun asResource(path: String): ResourceLocation {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
	}
}