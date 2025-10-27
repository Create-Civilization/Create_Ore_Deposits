package com.createcivilization.create_ore_deposits.registry.fluid

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.CreateOreDeposits.REGISTRATE
import com.createcivilization.create_ore_deposits.registry.fluid.entries.MoltenIronFluidBlock
import com.createcivilization.create_ore_deposits.registry.fluid.entries.SlagFluidBlock
import com.tterrag.registrate.util.entry.FluidEntry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.fluids.BaseFlowingFluid




object CreateOreDepositsFluids {

	fun fluidRL(name: String, flowing: Boolean): ResourceLocation = CreateOreDeposits.rl("block/fluid/$name/${name}_${if (flowing) "flow" else "still"}");

	//Lubricant Textures
	val LUBRICANT_STILL: ResourceLocation get() = fluidRL("lubricant", false);
	val LUBRICANT_FLOWING: ResourceLocation get() = fluidRL("lubricant", true);

	//Slag Textures
	val SLAG_STILL: ResourceLocation get() = fluidRL("slag", false);
	val SLAG_FLOWING: ResourceLocation get() = fluidRL("slag", true);

	//Molten Iron Textures
	val MOLTEN_IRON_STILL: ResourceLocation get() = fluidRL("molten_iron", false);
	val MOLTEN_IRON_FLOWING: ResourceLocation get() = fluidRL("molten_iron", true);

	val LUBRICANT: FluidEntry<BaseFlowingFluid.Flowing?>? = REGISTRATE.fluid("lubricant",LUBRICANT_STILL,LUBRICANT_FLOWING)
		.properties { b -> b.viscosity(1500)
				.density(500)
		}
		.fluidProperties { p -> p.levelDecreasePerBlock(2)
				.tickRate(25)
				.slopeFindDistance(3)
				.explosionResistance(100f)
		}.register()

	val SLAG: FluidEntry<BaseFlowingFluid.Flowing?>? =
		REGISTRATE.fluid("slag", SLAG_STILL, SLAG_FLOWING)
			.properties{ _ -> BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA)}.fluidProperties{p -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(0f)}
			.block{f, p -> SlagFluidBlock(f, p)}.build()
			.register()

	val MOLTEN_IRON: FluidEntry<BaseFlowingFluid.Flowing?>? =
		REGISTRATE.fluid("molten_iron", MOLTEN_IRON_STILL, MOLTEN_IRON_FLOWING)
			.properties { _ -> BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA)}
			.fluidProperties{p -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(0f)}
			.block{f,p -> MoltenIronFluidBlock(f,p)}.build()
			.register()
}