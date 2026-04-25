package com.createcivilization.create_ore_deposits.registry.worldgen

import net.minecraft.world.level.levelgen.placement.*

data object OrePlacement {

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