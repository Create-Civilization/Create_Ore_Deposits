package com.createcivilization.create_ore_deposits.registry.worldgen

import com.createcivilization.create_ore_deposits.config.Config
import com.createcivilization.create_ore_deposits.util.asResource
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.tags.BiomeTags
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.GenerationStep
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.common.world.BiomeModifiers as NeoBiomeModifiers
import net.neoforged.neoforge.registries.NeoForgeRegistries

data object BiomeModifiers {

	fun bootstrap(context: BootstrapContext<BiomeModifier>) {
		val placedFeatures = context.lookup(Registries.PLACED_FEATURE)
		val biomes = context.lookup(Registries.BIOME)

		OreVeinDeposits.DEPOSITS.forEach { deposit ->
			OreVeinTier.entries.forEach { tier ->
				val placedFeature = HolderSet.direct(placedFeatures.getOrThrow(PlacedFeatures.veinKey(deposit, tier)))

				// Use the overworld tag directly — runtime filtering via config
				// happens in OreVeinPlacementModifier, not here
				val overworldBiomes = biomes.getOrThrow(BiomeTags.IS_OVERWORLD)

				context.register(
					veinKey(deposit, tier),
					NeoBiomeModifiers.AddFeaturesBiomeModifier(
						overworldBiomes,
						placedFeature,
						GenerationStep.Decoration.UNDERGROUND_ORES
					)
				)
			}
		}
	}

	private fun registerKey(name: String): ResourceKey<BiomeModifier> = ResourceKey.create(
		NeoForgeRegistries.Keys.BIOME_MODIFIERS,
		name.asResource()
	)

	private fun resolveBiomeSelectors(
		biomes: net.minecraft.core.HolderGetter<Biome>,
		selectors: List<String>
	): HolderSet<Biome> {
		val resolvedBiomes = linkedSetOf<net.minecraft.core.Holder<Biome>>()
		val effectiveSelectors = selectors.ifEmpty { listOf("#minecraft:is_overworld") }

		effectiveSelectors.forEach { selector ->
			val id = ResourceLocation.parse(selector.removePrefix("#"))

			if (selector.startsWith("#")) {
				biomes.getOrThrow(TagKey.create(Registries.BIOME, id)).forEach { biome ->
					if (biome.`is`(BiomeTags.IS_OVERWORLD)) {
						resolvedBiomes += biome
					}
				}
			} else {
				val biome = biomes.getOrThrow(ResourceKey.create(Registries.BIOME, id))
				if (biome.`is`(BiomeTags.IS_OVERWORLD)) {
					resolvedBiomes += biome
				}
			}
		}

		check(resolvedBiomes.isNotEmpty()) {
			"Biome selectors $effectiveSelectors resolved to no overworld biomes."
		}

		return HolderSet.direct(resolvedBiomes.toList())
	}

	private fun veinKey(
		deposit: OreVeinDeposits.DepositVeinDefinition,
		tier: OreVeinTier
	): ResourceKey<BiomeModifier> = registerKey("${deposit.name}_${tier.getSerializedName()}_vein")
}
