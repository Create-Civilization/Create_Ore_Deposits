package com.createcivilization.create_ore_deposits

import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DrillBlockModel
import com.createcivilization.create_ore_deposits.util.logI
import com.createcivilization.create_ore_deposits.util.resource

import net.minecraft.client.model.geom.ModelLayerLocation

import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions

import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(value = CreateOreDeposits.MOD_ID, dist = [Dist.CLIENT])
object CreateOreDepositsClient {

	init {
		MOD_BUS.addListener(::clientInit)
		MOD_BUS.addListener(::onRegisterLayerDefinitions)
	}

	@Suppress("UnusedExpression") // Calls static initialiser
	fun clientInit(event: FMLClientSetupEvent) {
		logI("In client init!")
		DepositDrillBlockModels
//		PonderIndex.addPlugin(CODPonderPlugin())
	}

	fun onRegisterLayerDefinitions(event: RegisterLayerDefinitions) {
		event.registerLayerDefinition(DRILL_LAYER, DrillBlockModel::createModel)
	}

	val DRILL_LAYER: ModelLayerLocation = ModelLayerLocation("drill".resource(), "main")
}