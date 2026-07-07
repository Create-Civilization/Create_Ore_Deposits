package com.createcivilization.create_ore_deposits.registry.worldgen

import com.createcivilization.create_ore_deposits.util.asResource

import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.PlacementModifier

data object PlacedFeatures {

	private fun registerKey(name: String): ResourceKey<PlacedFeature> = ResourceKey.create(
		Registries.PLACED_FEATURE, name.asResource()
	)

	fun bootstrap(context: BootstrapContext<PlacedFeature>) {
		val configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE)
		OreVeinDeposits.DEPOSITS.forEach { deposit ->
			OreVeinTier.entries.forEach { tier ->
				register(
					context,
					veinKey(deposit, tier),
					configuredFeatures.getOrThrow(ConfiguredFeatures.veinKey(deposit, tier)),
					OrePlacement.clusterPlacement(deposit.block.get(), tier)
				)
			}
		}
	}

	private fun register(
		context: BootstrapContext<PlacedFeature>,
		key: ResourceKey<PlacedFeature>,
		configuration: Holder<ConfiguredFeature<*, *>>,
		modifiers: List<PlacementModifier>
	) = context.register(key, PlacedFeature(configuration, listOf(*modifiers.toTypedArray())))

	fun veinKey(deposit: OreVeinDeposits.DepositVeinDefinition, tier: OreVeinTier): ResourceKey<PlacedFeature> =
		registerKey("${deposit.name}_${tier.getSerializedName()}_vein")
}
