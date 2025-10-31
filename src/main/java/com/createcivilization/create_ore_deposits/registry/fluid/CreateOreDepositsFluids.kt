package com.createcivilization.create_ore_deposits.registry.fluid

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.CreateOreDeposits.REGISTRATE
import com.createcivilization.create_ore_deposits.registry.fluid.entries.MoltenMetalFluidBlock
import com.createcivilization.create_ore_deposits.registry.fluid.entries.SlurryWasteBlock
import com.createcivilization.create_ore_deposits.util.resource
import com.simibubi.create.AllFluids.TintedFluidType
import com.tterrag.registrate.builders.FluidBuilder.FluidTypeFactory
import com.tterrag.registrate.util.entry.FluidEntry
import net.createmod.catnip.theme.Color
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.MapColor
import net.neoforged.neoforge.fluids.BaseFlowingFluid
import net.neoforged.neoforge.fluids.FluidStack
import org.joml.Vector3f
import java.util.function.Supplier


object CreateOreDepositsFluids {

	fun fluidRL(name: String, flowing: Boolean): ResourceLocation = ("block/fluid/$name/${name}_${if (flowing) "flow" else "still"}".resource());

	//Lubricant Textures
	val LUBRICANT_STILL: ResourceLocation = fluidRL("lubricant", false);
	val LUBRICANT_FLOWING: ResourceLocation = fluidRL("lubricant", true);

	//Slag Textures
	val SLAG_STILL: ResourceLocation = fluidRL("slag", false);
	val SLAG_FLOWING: ResourceLocation = fluidRL("slag", true);

	//Molten Iron Textures
	val MOLTEN_IRON_STILL: ResourceLocation = fluidRL("molten_iron", false);
	val MOLTEN_IRON_FLOWING: ResourceLocation = fluidRL("molten_iron", true);

	//Molten Gold Textures
	val MOLTEN_GOLD_STILL: ResourceLocation = fluidRL("molten_gold", false);
	val MOLTEN_GOLD_FLOWING: ResourceLocation = fluidRL("molten_gold", true);

	//Molten Copper Textures
	val MOLTEN_COPPER_STILL: ResourceLocation = fluidRL("molten_copper", false);
	val MOLTEN_COPPER_FLOWING: ResourceLocation = fluidRL("molten_copper", true);

	//Molten Zinc Textures
	val MOLTEN_ZINC_STILL: ResourceLocation = fluidRL("molten_zinc", false);
	val MOLTEN_ZINC_FLOWING: ResourceLocation = fluidRL("molten_zinc", true);

	//Molten Netherite Textures
	val MOLTEN_NETHERITE_STILL: ResourceLocation = fluidRL("molten_netherite", false);
	val MOLTEN_NETHERITE_FLOWING: ResourceLocation = fluidRL("molten_netherite", true);

	val LUBRICANT: FluidEntry<BaseFlowingFluid.Flowing> = REGISTRATE.fluid("lubricant",LUBRICANT_STILL,LUBRICANT_FLOWING)
		.properties { b -> b.viscosity(1500)
				.density(500)
		}
		.fluidProperties { p -> p.levelDecreasePerBlock(2)
				.tickRate(25)
				.slopeFindDistance(3)
				.explosionResistance(100f)
		}.register()

	val SLAG: FluidEntry<BaseFlowingFluid.Flowing> =
		REGISTRATE.fluid("slag", SLAG_STILL, SLAG_FLOWING)
			.properties { _ -> BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA)}
			.fluidProperties{p -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(0f)}
			.block{f,p -> MoltenMetalFluidBlock(f,p)}.build()
			.register()

	//Iron Stuff

	val MOLTEN_IRON: FluidEntry<BaseFlowingFluid.Flowing> =
		REGISTRATE.fluid("molten_iron", MOLTEN_IRON_STILL, MOLTEN_IRON_FLOWING)
			.properties { _ -> BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA)}
			.fluidProperties{p -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(0f)}
			.block{f,p -> MoltenMetalFluidBlock(f,p)}.build()
			.register()

	//Gold Stuff

	val MOLTEN_GOLD: FluidEntry<BaseFlowingFluid.Flowing> =
		REGISTRATE.fluid("molten_gold", MOLTEN_GOLD_STILL, MOLTEN_GOLD_FLOWING, )
			.properties { _ -> BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA)}
			.fluidProperties{p -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(0f)}
			.block{f,p -> MoltenMetalFluidBlock(f,p)}.build()
			.register()

	val MOLTEN_COPPER: FluidEntry<BaseFlowingFluid.Flowing> =
		REGISTRATE.fluid("molten_copper", MOLTEN_COPPER_STILL, MOLTEN_COPPER_FLOWING, )
			.properties { _ -> BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA)}
			.fluidProperties{p -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(0f)}
			.block{f,p -> MoltenMetalFluidBlock(f,p)}.build()
			.register()

	val MOLTEN_ZINC: FluidEntry<BaseFlowingFluid.Flowing> =
		REGISTRATE.fluid("molten_zinc", MOLTEN_ZINC_STILL, MOLTEN_ZINC_FLOWING, )
			.properties { _ -> BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA)}
			.fluidProperties{p -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(0f)}
			.block{f,p -> MoltenMetalFluidBlock(f,p)}.build()
			.register()

	val MOLTEN_NETHERITE: FluidEntry<BaseFlowingFluid.Flowing> =
		REGISTRATE.fluid("molten_netherite", MOLTEN_NETHERITE_STILL, MOLTEN_NETHERITE_FLOWING, )
			.properties { _ -> BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA)}
			.fluidProperties{p -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(0f)}
			.block{f,p -> MoltenMetalFluidBlock(f,p)}.build()
			.register()




	//Stolen From Create <3
	class SolidRenderedPlaceableFluidType private constructor(properties: Properties, stillTexture: ResourceLocation, flowingTexture: ResourceLocation) : TintedFluidType(properties, stillTexture, flowingTexture) {
		private var fogColor: Vector3f? = null
		private var fogDistance: Supplier<Float?>? = null

		override fun getTintColor(stack: FluidStack?): Int {
			return NO_TINT
		}

		/*
		 * Removing alpha from tint prevents optifine from forcibly applying biome
		 * colors to modded fluids (this workaround only works for fluids in the solid
		 * render layer)
		 */
		public override fun getTintColor(state: FluidState?, world: BlockAndTintGetter?, pos: BlockPos?): Int {
			return 0x00ffffff
		}

		override fun getCustomFogColor(): Vector3f? {
			return fogColor
		}

		override fun getFogDistanceModifier(): Float {
			return fogDistance!!.get()!!
		}

		companion object {
			fun create(fogColor: Int, fogDistance: Supplier<Float?>): FluidTypeFactory {
				return FluidTypeFactory { p: Properties?, s: ResourceLocation?, f: ResourceLocation? ->
					val fluidType = SolidRenderedPlaceableFluidType(p!!, s!!, f!!)
					fluidType.fogColor = Color(fogColor, false).asVectorF()
					fluidType.fogDistance = fogDistance
					fluidType
				}
			}
		}
	}
}