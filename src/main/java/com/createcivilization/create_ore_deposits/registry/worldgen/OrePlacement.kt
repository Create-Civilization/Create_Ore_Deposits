package com.createcivilization.create_ore_deposits.registry.worldgen

import net.minecraft.world.level.levelgen.placement.*


object OrePlacement {
	fun orePlacement(
		pCountPlacement: PlacementModifier,
		pHeightRange: PlacementModifier
	): MutableList<PlacementModifier> {
		return mutableListOf(
			pCountPlacement,
			InSquarePlacement.spread(),
			pHeightRange,
			BiomeFilter.biome()
		)
	}

	fun commonOrePlacement(pCount: Int, pHeightRange: PlacementModifier): MutableList<PlacementModifier> {
		return orePlacement(CountPlacement.of(pCount), pHeightRange)
	}

	fun rareOrePlacement(pChance: Int, pHeightRange: PlacementModifier): MutableList<PlacementModifier> {
		return orePlacement(RarityFilter.onAverageOnceEvery(pChance), pHeightRange)
	}
}