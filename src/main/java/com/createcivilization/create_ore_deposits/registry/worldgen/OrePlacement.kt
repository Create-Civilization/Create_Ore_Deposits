package com.createcivilization.create_ore_deposits.registry.worldgen

import net.minecraft.world.level.levelgen.placement.*

// Note: (Neo)Forge only ever had 'p' prefixes on parameters in Minecraft's source due to potential issues with automatic decompilation.
// The prefixing was specifically because of that, it is not good practice and shouldn't be used elsewhere.
// In fact, due to the announcement of the removal of obfuscation in MC's releases, NeoForge/Parchment/Mojomaps will no longer contain 'p' prefixes in such versions.
// - Mavity
object OrePlacement {

	fun orePlacement(
		countPlacement: PlacementModifier,
		heightRange: PlacementModifier
	): List<PlacementModifier> = listOf(
		countPlacement,
		InSquarePlacement.spread(),
		heightRange,
		BiomeFilter.biome()
	)

	fun commonOrePlacement(
		count: Int,
		heightRange: PlacementModifier
	): List<PlacementModifier> = orePlacement(CountPlacement.of(count), heightRange)

	fun rareOrePlacement(
		chance: Int,
		heightRange: PlacementModifier
	): List<PlacementModifier> = orePlacement(RarityFilter.onAverageOnceEvery(chance), heightRange)
}