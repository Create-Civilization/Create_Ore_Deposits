package com.createcivilization.create_ore_deposits.registry.worldgen

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.util.logI
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.PlacementModifier


class PlacedFeatures {
	companion object {
		val IRON_ORE_PLACED_KEY: ResourceKey<PlacedFeature> = registerKey("iron_ore_placed")

		private fun registerKey(name: String): ResourceKey<PlacedFeature> {
			return ResourceKey.create(
				Registries.PLACED_FEATURE,
				ResourceLocation.fromNamespaceAndPath(CreateOreDeposits.MOD_ID, name)
			)
		}

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

		private fun register(
			context: BootstrapContext<PlacedFeature>,
			key: ResourceKey<PlacedFeature>,
			configuration: Holder<ConfiguredFeature<*, *>>,
			modifiers: MutableList<PlacementModifier>
		) {
			context.register(key, PlacedFeature(configuration, listOf(*modifiers.toTypedArray())))
		}
	}
}