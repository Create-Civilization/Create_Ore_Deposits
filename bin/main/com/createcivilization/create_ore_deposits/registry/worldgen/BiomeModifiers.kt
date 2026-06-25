package com.createcivilization.create_ore_deposits.registry.worldgen

import com.createcivilization.create_ore_deposits.util.asResource

import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BiomeTags
import net.minecraft.world.level.levelgen.GenerationStep
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.common.world.BiomeModifiers
import net.neoforged.neoforge.registries.NeoForgeRegistries

data object BiomeModifiers {

	@JvmField
	val ADD_IRON_ORE_DEPOSIT: ResourceKey<BiomeModifier> = registerKey("add_iron_ore_deposit")

	fun bootstrap(context: BootstrapContext<BiomeModifier>) {
		val placedFeatures = context.lookup(Registries.PLACED_FEATURE)
		val biomes = context.lookup(Registries.BIOME)

		context.register(
			ADD_IRON_ORE_DEPOSIT, BiomeModifiers.AddFeaturesBiomeModifier(
				biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
				HolderSet.direct(placedFeatures.getOrThrow(PlacedFeatures.IRON_ORE_PLACED_KEY)),
				GenerationStep.Decoration.UNDERGROUND_ORES
			)
		)

//		Example for individual Biomes!
//		context.register(ADD_BISMUTH_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
//			HolderSet.direct(biomes.getOrThrow(Biomes.PLAINS), biomes.getOrThrow(Biomes.SAVANNA)),
//			HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.BISMUTH_ORE_PLACED_KEY)),
//			GenerationStep.Decoration.UNDERGROUND_ORES)
//		);
	}

	private fun registerKey(name: String): ResourceKey<BiomeModifier> = ResourceKey.create(
		NeoForgeRegistries.Keys.BIOME_MODIFIERS,
		name.asResource()
	)
}