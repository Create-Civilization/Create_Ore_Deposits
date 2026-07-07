package com.createcivilization.create_ore_deposits.registry.worldgen

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.placement.PlacementModifier

data object OrePlacement {

	fun clusterPlacement(block: Block, tier: OreVeinTier): List<PlacementModifier> = listOf(
		OreVeinPlacementModifier(block, tier)
	)
}
