package com.createcivilization.create_ore_deposits.registry.datagen

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.registry.worldgen.BiomeModifiers
import com.createcivilization.create_ore_deposits.registry.worldgen.ConfiguredFeatures
import com.createcivilization.create_ore_deposits.registry.worldgen.PlacedFeatures
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.concurrent.CompletableFuture


class DatapackProvider(
	output: PackOutput,
	registries: CompletableFuture<HolderLookup.Provider>
) : DatapackBuiltinEntriesProvider(
	output,
	registries,
	BUILDER,
	setOf(CreateOreDeposits.MOD_ID)
) {
	companion object {
		val BUILDER: RegistrySetBuilder = RegistrySetBuilder()
			.add<ConfiguredFeature<*, *>>(Registries.CONFIGURED_FEATURE, ConfiguredFeatures::bootstrap)
			.add<PlacedFeature>(Registries.PLACED_FEATURE, PlacedFeatures::bootstrap)
			.add<BiomeModifier>(NeoForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifiers::bootstrap)
	}
}
