package com.createcivilization.create_ore_deposits

import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DrillBlockModel
import com.createcivilization.create_ore_deposits.util.resource

import net.minecraft.client.model.geom.ModelLayerLocation

import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions

// Correction on that comment: The MC Dev IDE plugin can't see it, so IntelliJ assumes it's unused. But you're right effectively.
@Suppress("unused") // Kotlin can't see that Event subscriber is using this.
@EventBusSubscriber(modid = CreateOreDeposits.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object ModClientBusEvents {

	@SubscribeEvent
	fun onRegisterLayerDefinitions(event: RegisterLayerDefinitions) {
		event.registerLayerDefinition(DRILL_LAYER, DrillBlockModel::createModel)
	}

	val DRILL_LAYER: ModelLayerLocation = ModelLayerLocation("drill".resource(), "main")
}