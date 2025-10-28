package com.createcivilization.create_ore_deposits.registry.fluid

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.CreateOreDeposits.REGISTRATE
import com.createcivilization.create_ore_deposits.registry.fluid.entries.MoltenMetalFluidBlock
import com.createcivilization.create_ore_deposits.registry.fluid.entries.SlurryWasteBlock
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

	//Molten Gold Textures
	val MOLTEN_GOLD_STILL: ResourceLocation get() = fluidRL("molten_gold", false);
	val MOLTEN_GOLD_FLOWING: ResourceLocation get() = fluidRL("molten_gold", true);

	//Slurry Textures
	val WASTE_SLURRY_STILL: ResourceLocation get() = fluidRL("waste_slurry", false);
	val WASTE_SLURRY_FLOWING: ResourceLocation get() = fluidRL("waste_slurry", true);

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
			.block{f, p -> MoltenMetalFluidBlock(f, p)}.build()
			.register()

	//Iron Stuff

	val MOLTEN_IRON: FluidEntry<BaseFlowingFluid.Flowing?>? =
		REGISTRATE.fluid("molten_iron", MOLTEN_IRON_STILL, MOLTEN_IRON_FLOWING)
			.properties { _ -> BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA)}
			.fluidProperties{p -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(0f)}
			.block{f,p -> MoltenMetalFluidBlock(f,p)}.build()
			.register()

	//Gold Stuff

	val MOLTEN_GOLD: FluidEntry<BaseFlowingFluid.Flowing?>? =
		REGISTRATE.fluid("molten_gold", MOLTEN_GOLD_STILL, MOLTEN_GOLD_FLOWING)
			.properties { _ -> BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA)}
			.fluidProperties{p -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(0f)}
			.block{f,p -> MoltenMetalFluidBlock(f,p)}.build()
			.register()

	val WASTE_SLURRY: FluidEntry<BaseFlowingFluid.Flowing?>? =
		REGISTRATE.fluid("waste_slurry", WASTE_SLURRY_STILL, WASTE_SLURRY_FLOWING)
			.properties { _ -> BlockBehaviour.Properties.ofFullCopy(Blocks.WATER) }
			.fluidProperties{p -> p.levelDecreasePerBlock(1).tickRate(5).slopeFindDistance(4).explosionResistance(100f)}
			.block{f,p -> SlurryWasteBlock(f,p) }.build()
			.register()
}