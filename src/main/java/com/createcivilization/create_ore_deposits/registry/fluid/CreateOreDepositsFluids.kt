package com.createcivilization.create_ore_deposits.registry.fluid

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.CreateOreDeposits.REGISTRATE
import com.tterrag.registrate.util.entry.FluidEntry
import net.neoforged.neoforge.fluids.BaseFlowingFluid



/**
 * DO NOT USE NEO'S HEAVILY ABSTRACTED FLUID CLASS! IT WORKS VERY DIFFERENTLY! TOO MUCH OF A HASSLE!
 */
object CreateOreDepositsFluids {


	val LUBRICANT: FluidEntry<BaseFlowingFluid.Flowing> = REGISTRATE.fluid("lubricant",CreateOreDeposits.rl("block/fluid/lubricant_still"),CreateOreDeposits.rl("block/fluid/lubricant_flow"))
		.properties { b -> b.viscosity(1500)
				.density(500)
		}
		.fluidProperties { p -> p.levelDecreasePerBlock(2)
				.tickRate(25)
				.slopeFindDistance(3)
				.explosionResistance(100f)
		}.register()


	val SLAG: FluidEntry<BaseFlowingFluid.Flowing> = REGISTRATE.fluid("slag", CreateOreDeposits.rl("block/fluid/slag_still"), CreateOreDeposits.rl("block/fluid/slag_flow"))
		.properties {b -> b.viscosity(1500).density(500)}.
		fluidProperties {p -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(25).explosionResistance(0f)}
	.register()
}