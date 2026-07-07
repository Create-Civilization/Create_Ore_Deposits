package com.createcivilization.create_ore_deposits.registry.worldgen

import net.minecraft.util.RandomSource
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration

// Port of Create 6.0.10's `LayerPattern.rollNext()` and `LayerPattern.Layer.rollBlock()`.
// The weighted "pick any layer except the previous one" behavior is what makes adjacent bands
// change material instead of repeating the same stratum forever.

data class LayeredDepositPattern(
	val layers: List<Layer>
) {
	fun rollNext(previous: Layer?, random: RandomSource): Layer {
		var totalWeight = 0

		layers.forEach { layer ->
			if (layer != previous) {
				totalWeight += layer.weight
			}
		}

		var rolled = random.nextInt(totalWeight)
		layers.forEach { layer ->
			if (layer == previous) {
				return@forEach
			}

			rolled -= layer.weight
			if (rolled < 0) {
				return layer
			}
		}

		return error("LayeredDepositPattern.rollNext() could not resolve a layer from $layers")
// if you ever actually see this, it means every layer got excluded (all weight 0 or something silly),
// which means the pattern itself is broken, not a random edge case worth silently handling
	}

	data class Layer(
		val targets: List<List<OreConfiguration.TargetBlockState>>,
		val minSize: Int,
		val maxSize: Int,
		val weight: Int
	) {
		fun rollTargets(random: RandomSource): List<OreConfiguration.TargetBlockState> = when (targets.size) {
			1 -> targets.first()
			else -> targets[random.nextInt(targets.size)]
		}
	}
}
