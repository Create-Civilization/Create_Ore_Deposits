package com.createcivilization.create_ore_deposits.registry.fluid

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.registry.fluid.entries.TestFluid
import com.createcivilization.create_ore_deposits.util.KotlinDeferredRegister

import net.minecraft.core.registries.Registries
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid

import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

/**
 * DO NOT USE NEO'S HEAVILY ABSTRACTED FLUID CLASS! IT WORKS VERY DIFFERENTLY! TOO MUCH OF A HASSLE!
 */
object CreateOreDepositsFluids {

	@JvmField
	internal val FLUID_PROVIDER: KotlinDeferredRegister<Fluid> = KotlinDeferredRegister(
		Registries.FLUID,
		CreateOreDeposits.MOD_ID
	)

	private val _TEST_FLUID: () -> FlowingFluid = FLUID_PROVIDER.register("test", TestFluid::Source)
	val TEST_FLUID: FlowingFluid get() = _TEST_FLUID()
	private val _TEST_FLUID_FLOWING: () -> FlowingFluid = FLUID_PROVIDER.register("flowing_test", TestFluid::Flowing)
	val TEST_FLUID_FLOWING: FlowingFluid get() = _TEST_FLUID_FLOWING()

	init {
		FLUID_PROVIDER.register(MOD_BUS)
	}
}