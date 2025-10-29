package com.createcivilization.create_ore_deposits.registry.fluid

import com.createcivilization.create_ore_deposits.util.logI
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.util.INBTSerializable
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction
import net.neoforged.neoforge.fluids.capability.templates.FluidTank
import java.util.Set
import java.util.function.Predicate


class FluidHandler(capacity: Int, allowedFluids: MutableSet<Fluid>?) : IFluidHandler,
	INBTSerializable<CompoundTag> {
	private val tank: FluidTank
	private var allowedFluids: MutableSet<Fluid>?

	init {
		this.tank = FluidTank(capacity, Predicate { fluidStack: FluidStack -> isAllowed(fluidStack.fluid) })
		this.allowedFluids = allowedFluids
	}

	fun isAllowed(fluid: Fluid?): Boolean {
		return allowedFluids == null || allowedFluids!!.contains(fluid)
	}

	override fun getTanks(): Int {
		return 1
	}

	override fun getFluidInTank(i: Int): FluidStack {
		return this.tank.getFluid()
	}

	override fun getTankCapacity(i: Int): Int {
		return this.tank.getCapacity()
	}

	override fun isFluidValid(i: Int, fluidStack: FluidStack): Boolean {
		return isAllowed(fluidStack.fluid)
	}

	override fun fill(fluidStack: FluidStack, fluidAction: FluidAction): Int {
		return if (isAllowed(fluidStack.fluid)) tank.fill(fluidStack, fluidAction) else 0
	}

	override fun drain(fluidStack: FluidStack, fluidAction: FluidAction): FluidStack {
		return tank.drain(fluidStack, fluidAction)
	}

	override fun drain(i: Int, fluidAction: FluidAction): FluidStack {
		return tank.drain(i, fluidAction)
	}

	override fun serializeNBT(provider: HolderLookup.Provider): CompoundTag {
		val nbt = CompoundTag()
		tank.writeToNBT(provider, nbt)
		allowedFluids?.let { fluids ->
			val fluidListTag = ListTag()
			for (fluid in fluids) {
				val id = BuiltInRegistries.FLUID.getKey(fluid).toString()
				fluidListTag.add(StringTag.valueOf(id))
			}
			nbt.put("AllowedFluids", fluidListTag)
		}
		return nbt
	}

	override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
		tank.readFromNBT(provider, nbt)
		allowedFluids = mutableSetOf()
		val fluidListTag = nbt.getList("AllowedFluids", StringTag.TAG_STRING.toInt())
		for (i in 0 until fluidListTag.size) {
			val id = fluidListTag.getString(i)
			val fluid = BuiltInRegistries.FLUID.get(ResourceLocation.parse(id))
			allowedFluids?.add(fluid)
		}
	}

//	override fun serializeNBT(provider: HolderLookup.Provider): CompoundTag {
//		val nbt = CompoundTag()
//		this.tank.writeToNBT(provider, nbt)
//		if (allowedFluids != null) {
//			nbt.putInt("size", allowedFluids!!.size)
//			var i = 0
//			for (fluid in allowedFluids) {
//				nbt.putString((i++).toString(), BuiltInRegistries.FLUID.getKey(fluid).toString())
//			}
//		}
//		return nbt
//	}
//
//	override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
//		logI("compound: $nbt")
//		this.tank.readFromNBT(provider, nbt)
//		val size = nbt.getInt("size")
//		val fluidList: MutableList<Fluid> = ArrayList()
//		for (i in 0 until size) {
//			fluidList.add(BuiltInRegistries.FLUID.get(ResourceLocation.parse(nbt.getString(i.toString()))))
//		}
//		this.allowedFluids = Set.copyOf(fluidList)
//	}
}