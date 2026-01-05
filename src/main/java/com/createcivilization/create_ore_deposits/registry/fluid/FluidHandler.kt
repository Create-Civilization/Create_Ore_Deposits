package com.createcivilization.create_ore_deposits.registry.fluid

import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.Fluid

import net.neoforged.neoforge.common.util.INBTSerializable as NBTSerialisable
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler as FluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction
import net.neoforged.neoforge.fluids.capability.templates.FluidTank

// Delegate what?  Delegate why?!  WHY DID I NOT WRITE DOWN WHAT I MEANT?!
// TODO: Delegate?
class FluidHandler(
	capacity: Int,
	private var allowedFluids: MutableSet<Fluid>?
) : FluidHandler, NBTSerialisable<CompoundTag> {

	private val tank: FluidTank = FluidTank(capacity, ::isAllowed)

	@Suppress("NOTHING_TO_INLINE")
	inline fun isAllowed(fluidStack: FluidStack): Boolean = this.isAllowed(fluidStack.fluid)

	fun isAllowed(fluid: Fluid?): Boolean = allowedFluids == null || allowedFluids!!.contains(fluid)

	override fun getTanks(): Int = 1

	override fun getFluidInTank(i: Int): FluidStack = this.tank.getFluid()

	override fun getTankCapacity(i: Int): Int = this.tank.getCapacity()

	override fun isFluidValid(i: Int, fluidStack: FluidStack): Boolean = isAllowed(fluidStack)

	override fun fill(fluidStack: FluidStack, fluidAction: FluidAction): Int =
		if (isAllowed(fluidStack)) tank.fill(fluidStack, fluidAction) else 0

	override fun drain(fluidStack: FluidStack, fluidAction: FluidAction): FluidStack = tank.drain(fluidStack, fluidAction)

	override fun drain(i: Int, fluidAction: FluidAction): FluidStack = tank.drain(i, fluidAction)

	override fun serializeNBT(provider: HolderLookup.Provider): CompoundTag {
		val nbt = CompoundTag()
		tank.writeToNBT(provider, nbt)
		allowedFluids?.let { fluids: MutableSet<Fluid> ->
			val fluidListTag = ListTag()
			for (fluid: Fluid in fluids) fluidListTag += StringTag.valueOf(BuiltInRegistries.FLUID.getKey(fluid).toString())
			nbt.put("AllowedFluids", fluidListTag)
		}
		return nbt
	}

	override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
		tank.readFromNBT(provider, nbt)
		allowedFluids = mutableSetOf()
		val fluidListTag: ListTag = nbt.getList("AllowedFluids", StringTag.TAG_STRING.toInt())
		for (i: Int in 0..<fluidListTag.size) {
			val id: String = fluidListTag.getString(i)
			allowedFluids?.add(BuiltInRegistries.FLUID.get(ResourceLocation.parse(id)))
		}
	}
}