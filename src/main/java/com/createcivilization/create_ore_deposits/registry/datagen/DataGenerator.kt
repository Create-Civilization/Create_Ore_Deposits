package com.createcivilization.create_ore_deposits.registry.datagen

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.util.logI

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

import java.util.concurrent.CompletableFuture

@EventBusSubscriber(modid = CreateOreDeposits.MOD_ID)
object DataGenerator {

	@SubscribeEvent
	fun gatherData(event: GatherDataEvent) {
		logI("GENNING")
		val generator: net.minecraft.data.DataGenerator = event.generator
		val packOutput: PackOutput = generator.packOutput
//		val existingFileHelper = event.existingFileHelper
		val lookupProvider: CompletableFuture<HolderLookup.Provider> = event.lookupProvider

		generator.addProvider(event.includeServer(), DatapackProvider(packOutput, lookupProvider))
		generator.addProvider(event.includeServer(), DataMapProvider(packOutput, lookupProvider))

	}
}