package com.createcivilization.create_ore_deposits.registry.worldgen

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration

data class OreVeinConfiguration(
	val state: BlockState,
	val targets: List<OreConfiguration.TargetBlockState>,
	val tier: OreVeinTier
) : FeatureConfiguration {

	companion object {

		@JvmField
		val CODEC: Codec<OreVeinConfiguration> = RecordCodecBuilder.create { instance ->
			instance.group(
				BlockState.CODEC.fieldOf("state").forGetter(OreVeinConfiguration::state),
				OreConfiguration.TargetBlockState.CODEC.listOf().fieldOf("targets").forGetter(OreVeinConfiguration::targets),
				OreVeinTier.CODEC.fieldOf("tier").forGetter(OreVeinConfiguration::tier)
			).apply(instance, ::OreVeinConfiguration)
		}
	}
}
