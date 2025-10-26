package com.createcivilization.create_ore_deposits.registry.fluids

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.util.KotlinDeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.NeoForgeRegistries

class CreateOreDepositsFluids {

	@JvmField
	internal val FLUID_PROVIDER: KotlinDeferredRegister<Fluid> = KotlinDeferredRegister(
		Registries.FLUID,
		CreateOreDeposits.MOD_ID
	)
}