@file:Suppress("UnusedExpression") // Calling the objects implicitly invokes their initialisers
package com.createcivilization.create_ore_deposits

import com.createcivilization.create_ore_deposits.config.Config
import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositsBlockEntities
import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositsBlocks
import com.createcivilization.create_ore_deposits.registry.datamap.CreateOreDepositsDataMaps
import com.createcivilization.create_ore_deposits.registry.fluid.CreateOreDepositsFluids
import com.createcivilization.create_ore_deposits.registry.item.CreateOreDepositsItems
import com.createcivilization.create_ore_deposits.registry.tab.CreateOreDepositsTabs
import com.createcivilization.create_ore_deposits.registry.tag.CreateOreDepositsTags
import com.createcivilization.create_ore_deposits.util.logI

import com.simibubi.create.foundation.data.CreateRegistrate

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.event.server.ServerStartingEvent
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent

import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(CreateOreDeposits.MOD_ID)
data object CreateOreDeposits {

	val REGISTRATE: CreateRegistrate = CreateRegistrate.create(MOD_ID)
		.defaultCreativeTab(CreateOreDepositsTabs.BASE_CREATIVE_TAB.key!!)
		.skipErrors(true) // Due to us not having all the textures, models, jsons, etc. yet, this avoids datagen crashing

	init {
		MOD_BUS.addListener(this::commonSetup)
		MOD_BUS.addListener(this::registerDataMapTypes)

		val container: ModContainer = LOADING_CONTEXT.activeContainer!!
		container.registerConfig(ModConfig.Type.SERVER, Config.serverSpec)

		REGISTRATE.registerEventListeners(MOD_BUS)

		FORGE_BUS.register(this)
		CreateOreDepositsItems
		CreateOreDepositsBlocks
		CreateOreDepositsBlockEntities
		CreateOreDepositsFluids
		CreateOreDepositsTags
		CreateOreDepositsDataMaps

		CreateOreDepositsTabs.register(MOD_BUS)
	}

	private fun commonSetup(event: FMLCommonSetupEvent) {
		// Some common setup code
	}

	private fun registerDataMapTypes(event: RegisterDataMapTypesEvent) {
		event.register(CreateOreDepositsDataMaps.HARDNESS_DATA)
		event.register(CreateOreDepositsDataMaps.COOLING_FACTOR_DATA)
	}

	@SubscribeEvent
	fun onServerStarting(event: ServerStartingEvent) {
		logI("Starting server...")

		logI("Server started successfully.")
	}

	const val MOD_ID: String = "create_ore_deposits"
}