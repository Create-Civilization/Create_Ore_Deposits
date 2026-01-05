package com.createcivilization.create_ore_deposits.registry.worldgen

import com.createcivilization.create_ore_deposits.util.asResource

import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.PlacementModifier

// Note: You don't have to use 'companion object' under a class if there's no other members in the class;  Instead, just make the entire class an object.
// - Mavity
object PlacedFeatures {

	val IRON_ORE_PLACED_KEY: ResourceKey<PlacedFeature> = registerKey("iron_ore_placed")

	private fun registerKey(name: String): ResourceKey<PlacedFeature> = ResourceKey.create(
		Registries.PLACED_FEATURE,
		name.asResource()
	)

	fun bootstrap(context: BootstrapContext<PlacedFeature>) {
		val configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE)
		register(
			context,
			IRON_ORE_PLACED_KEY,
			configuredFeatures.getOrThrow(ConfiguredFeatures.IRON_ORE_DEPOSIT_KEY),
			OrePlacement.commonOrePlacement(
				12,
				HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))
			)
		)
	}

	// Note: If you're dealing w/ lists, you can lower from MutableList to List if you don't need to modify the contents.
	// - Mavity
	private fun register(
		context: BootstrapContext<PlacedFeature>,
		key: ResourceKey<PlacedFeature>,
		configuration: Holder<ConfiguredFeature<*, *>>,
		modifiers: List<PlacementModifier>
	) = context.register(key, PlacedFeature(configuration, listOf(*modifiers.toTypedArray())))
}