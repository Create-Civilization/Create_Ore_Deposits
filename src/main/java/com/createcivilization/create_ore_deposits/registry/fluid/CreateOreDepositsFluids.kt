package com.createcivilization.create_ore_deposits.registry.fluid

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.util.asResource

import com.tterrag.registrate.util.entry.FluidEntry

import net.neoforged.neoforge.fluids.BaseFlowingFluid

object CreateOreDepositsFluids {

	val LUBRICANT: FluidEntry<BaseFlowingFluid.Flowing> = CreateOreDeposits.REGISTRATE
		.fluid("lubricant", "block/fluid/lubricant_still".asResource(), "block/fluid/lubricant_flow".asResource())
		.properties { it.viscosity(1500).density(500) }
		.fluidProperties { it.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(100f) }
		.source { BaseFlowingFluid.Source(it) }
		.register()


	val SLAG: FluidEntry<BaseFlowingFluid.Flowing> = CreateOreDeposits.REGISTRATE
		.fluid("slag", "block/fluid/slag_still".asResource(), "block/fluid/slag_flow".asResource())
		.properties { it.viscosity(1500).density(500) }
		.fluidProperties { it.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(25).explosionResistance(0f) }
		.register()
}