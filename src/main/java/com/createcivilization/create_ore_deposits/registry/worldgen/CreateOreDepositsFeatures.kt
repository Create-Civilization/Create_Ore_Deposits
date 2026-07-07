package com.createcivilization.create_ore_deposits.registry.worldgen

import com.createcivilization.create_ore_deposits.CreateOreDeposits.MOD_ID
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.levelgen.feature.Feature
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

data object CreateOreDepositsFeatures {

	private val REGISTER: DeferredRegister<Feature<*>> = DeferredRegister.create(Registries.FEATURE, MOD_ID)

	@JvmField
	val ORE_VEIN: DeferredHolder<Feature<*>, OreVeinFeature> = REGISTER.register("ore_vein", ::OreVeinFeature)

	fun register(modEventBus: IEventBus) = REGISTER.register(modEventBus)
}
