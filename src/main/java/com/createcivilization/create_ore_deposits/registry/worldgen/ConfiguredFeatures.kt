package com.createcivilization.create_ore_deposits.registry.worldgen

import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositsBlocks
import com.createcivilization.create_ore_deposits.util.asResource

import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest

data object ConfiguredFeatures {

	@JvmField
	val IRON_ORE_DEPOSIT_KEY: ResourceKey<ConfiguredFeature<*, *>> = registerKey("iron_ore_deposit")

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
		val stoneReplaceables: RuleTest = TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES)
		val oreConfiguration: List<OreConfiguration.TargetBlockState> = listOf(
			OreConfiguration.target(stoneReplaceables, CreateOreDepositsBlocks.IRON_ORE_DEPOSIT.defaultState)
		)

		register(context, IRON_ORE_DEPOSIT_KEY, Feature.ORE, OreConfiguration(oreConfiguration, 9))
	}
}