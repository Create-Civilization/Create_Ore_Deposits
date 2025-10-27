package com.createcivilization.create_ore_deposits

import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DrillBlockModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions


@EventBusSubscriber(modid = CreateOreDeposits.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object ModClientBusEvents {
	@SubscribeEvent
	fun onRegisterLayerDefinitions(event: RegisterLayerDefinitions) {
		event.registerLayerDefinition(ModModelLayers.DRILL_LAYER, DrillBlockModel::createModel)
	}

	//I dont think we would need that many model layers
	//TODO::Make it in a seperate class
	object ModModelLayers {
		val DRILL_LAYER: ModelLayerLocation = ModelLayerLocation(
			CreateOreDeposits.asResource("drill"), "main"
		)
	}
}