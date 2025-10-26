package com.createcivilization.create_ore_deposits.registry.fluids.entries

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.Item
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.VoxelShape

class Slag: Fluid() {

	override fun getBucket(): Item {
		TODO("Not yet implemented")
	}

	override fun canBeReplacedWith(
		p0: FluidState,
		p1: BlockGetter,
		p2: BlockPos,
		p3: Fluid,
		p4: Direction
	): Boolean {
		TODO("Not yet implemented")
	}

	override fun getFlow(
		p0: BlockGetter,
		p1: BlockPos,
		p2: FluidState
	): Vec3 {
		TODO("Not yet implemented")
	}

	override fun getTickDelay(p0: LevelReader): Int {
		TODO("Not yet implemented")
	}

	override fun getExplosionResistance(): Float {
		TODO("Not yet implemented")
	}

	override fun getHeight(
		p0: FluidState,
		p1: BlockGetter,
		p2: BlockPos
	): Float {
		TODO("Not yet implemented")
	}

	override fun getOwnHeight(p0: FluidState): Float {
		TODO("Not yet implemented")
	}

	override fun createLegacyBlock(p0: FluidState): BlockState {
		TODO("Not yet implemented")
	}

	override fun isSource(p0: FluidState): Boolean {
		TODO("Not yet implemented")
	}

	override fun getAmount(p0: FluidState): Int {
		TODO("Not yet implemented")
	}

	override fun getShape(
		p0: FluidState,
		p1: BlockGetter,
		p2: BlockPos
	): VoxelShape {
		TODO("Not yet implemented")
	}
}