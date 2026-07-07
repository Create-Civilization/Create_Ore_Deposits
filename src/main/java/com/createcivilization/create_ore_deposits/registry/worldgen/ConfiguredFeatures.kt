package com.createcivilization.create_ore_deposits.registry.worldgen

import com.createcivilization.create_ore_deposits.util.asResource

import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration

data object ConfiguredFeatures {

	fun registerKey(name: String): ResourceKey<ConfiguredFeature<*, *>> = ResourceKey.create(
		Registries.CONFIGURED_FEATURE,
		name.asResource()
	)

	fun <FC : FeatureConfiguration, F : Feature<FC>> register(
		context: BootstrapContext<ConfiguredFeature<*, *>>,
		key: ResourceKey<ConfiguredFeature<*, *>>,
		feature: F,
		configuredFeature: FC
	): Holder.Reference<ConfiguredFeature<*, *>> = context.register(key, ConfiguredFeature(feature, configuredFeature))

	fun bootstrap(context: BootstrapContext<ConfiguredFeature<*, *>>) {
		OreVeinDeposits.DEPOSITS.forEach { deposit ->
			OreVeinTier.entries.forEach { tier ->
				register(
					context,
					veinKey(deposit, tier),
					CreateOreDepositsFeatures.ORE_VEIN.get(),
					OreVeinConfiguration(
						deposit.block.defaultState,
						deposit.targets,
						tier
					)
				)
			}
		}
	}

	fun veinKey(deposit: OreVeinDeposits.DepositVeinDefinition, tier: OreVeinTier): ResourceKey<ConfiguredFeature<*, *>> =
		registerKey("${deposit.name}_${tier.getSerializedName()}_vein")
}
