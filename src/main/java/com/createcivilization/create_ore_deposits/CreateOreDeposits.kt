@file:Suppress("UnusedExpression") // Calling the objects implicitly invokes their initialisers
package com.createcivilization.create_ore_deposits

import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositBlocks
import com.createcivilization.create_ore_deposits.registry.blockentities.CreateOreDepositBlockEntities
import com.createcivilization.create_ore_deposits.registry.item.CreateOreDepositItems
import com.createcivilization.create_ore_deposits.util.logI

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.event.server.ServerStartingEvent

import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(CreateOreDeposits.MOD_ID)
object CreateOreDeposits {

	init {
		MOD_BUS.addListener(this::commonSetup)
		FORGE_BUS.register(this)
		CreateOreDepositItems
		CreateOreDepositBlocks
		CreateOreDepositBlockEntities
	}

	private fun commonSetup(event: FMLCommonSetupEvent) {
		// Some common setup code
	}

	@SubscribeEvent
	fun onServerStarting(event: ServerStartingEvent) {
		// Do something when the server starts
		logI("CREATE ORE DEPOSIT GO BRRRRR")
	}

	const val MOD_ID: String = "create_ore_deposits"
}