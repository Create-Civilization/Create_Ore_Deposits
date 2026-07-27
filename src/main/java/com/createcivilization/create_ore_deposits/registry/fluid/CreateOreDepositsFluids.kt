package com.createcivilization.create_ore_deposits.registry.fluid

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.util.asResource

import com.tterrag.registrate.util.entry.FluidEntry

import net.neoforged.neoforge.fluids.BaseFlowingFluid

data object CreateOreDepositsFluids {

	@JvmField
	val LUBRICANT: FluidEntry<BaseFlowingFluid.Flowing> = CreateOreDeposits.REGISTRATE
		.fluid("lubricant", "block/fluid/lubricant_still".asResource(), "block/fluid/lubricant_flow".asResource())
		.properties { it.viscosity(1500).density(500) }
		.fluidProperties { it.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(100f) }
		.source(BaseFlowingFluid::Source)
		.register()

	@JvmField
	val SLAG: FluidEntry<BaseFlowingFluid.Flowing> = CreateOreDeposits.REGISTRATE
		.fluid("slag", "block/fluid/slag_still".asResource(), "block/fluid/slag_flow".asResource())
		.properties { it.viscosity(1500).density(500) }
		.fluidProperties { it.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(0f) }
		.source(BaseFlowingFluid::Source)
		.register()

	@JvmField
	val MOLTEN_IRON: FluidEntry<BaseFlowingFluid.Flowing> = CreateOreDeposits.REGISTRATE
		.fluid("molten_iron", "block/fluid/molten_iron_still".asResource(), "block/fluid/molten_iron_flow".asResource())
		.properties { it.viscosity(1500).density(500) }
		.fluidProperties { it.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(0f) }
		.source(BaseFlowingFluid::Source)
		.register()

	@JvmField
	val MOLTEN_GOLD: FluidEntry<BaseFlowingFluid.Flowing> = CreateOreDeposits.REGISTRATE
		.fluid("molten_gold", "block/fluid/molten_gold_still".asResource(), "block/fluid/molten_gold_flow".asResource())
		.properties { it.viscosity(1500).density(500) }
		.fluidProperties { it.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(0f) }
		.source(BaseFlowingFluid::Source)
		.register()

	@JvmField
	val WASTE_SLURRY: FluidEntry<BaseFlowingFluid.Flowing> = CreateOreDeposits.REGISTRATE
		.fluid("waste_slurry", "block/fluid/waste_slurry_still".asResource(), "block/fluid/waste_slurry_flow".asResource())
		.properties { it.viscosity(1500).density(500) }
		.fluidProperties { it.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(0f) }
		.source(BaseFlowingFluid::Source)
		.register()

	@JvmField
	val MOLTEN_COPPER: FluidEntry<BaseFlowingFluid.Flowing> = CreateOreDeposits.REGISTRATE
		.fluid("molten_copper", "block/fluid/molten_copper_still".asResource(), "block/fluid/molten_copper_flow".asResource())
		.properties { it.viscosity(1500).density(500) }
		.fluidProperties { it.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(0f) }
		.source(BaseFlowingFluid::Source)
		.register()

	@JvmField
	val MOLTEN_ZINC: FluidEntry<BaseFlowingFluid.Flowing> = CreateOreDeposits.REGISTRATE
		.fluid("molten_zinc", "block/fluid/molten_zinc_still".asResource(), "block/fluid/molten_zinc_flow".asResource())
		.properties { it.viscosity(1500).density(500) }
		.fluidProperties { it.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(0f) }
		.source(BaseFlowingFluid::Source)
		.register()

	@JvmField
	val MOLTEN_BRASS: FluidEntry<BaseFlowingFluid.Flowing> = CreateOreDeposits.REGISTRATE
		.fluid("molten_brass", "block/fluid/molten_brass_still".asResource(), "block/fluid/molten_brass_flow".asResource())
		.properties { it.viscosity(1500).density(500) }
		.fluidProperties { it.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(0f) }
		.source(BaseFlowingFluid::Source)
		.register()

	@JvmField
	val MOLTEN_STEEL: FluidEntry<BaseFlowingFluid.Flowing> = CreateOreDeposits.REGISTRATE
		.fluid("molten_steel", "block/fluid/molten_steel_still".asResource(), "block/fluid/molten_steel_flow".asResource())
		.properties { it.viscosity(1500).density(500) }
		.fluidProperties { it.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(0f) }
		.source(BaseFlowingFluid::Source)
		.register()

	@JvmField
	val DEBRIS_SLURRY: FluidEntry<BaseFlowingFluid.Flowing> = CreateOreDeposits.REGISTRATE
		.fluid("debris_slurry", "block/fluid/molten_debris_still".asResource(), "block/fluid/molten_debris_flow".asResource())
		.properties { it.viscosity(1500).density(500) }
		.fluidProperties { it.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(0f) }
		.source(BaseFlowingFluid::Source)
		.register()

	@JvmField
	val MOLTEN_NETHERITE: FluidEntry<BaseFlowingFluid.Flowing> = CreateOreDeposits.REGISTRATE
		.fluid("molten_netherite", "block/fluid/molten_netherite_still".asResource(), "block/fluid/molten_netherite_flow".asResource())
		.properties { it.viscosity(1500).density(500) }
		.fluidProperties { it.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(0f) }
		.source(BaseFlowingFluid::Source)
		.register()
}
