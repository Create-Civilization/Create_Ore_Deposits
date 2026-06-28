package com.createcivilization.create_ore_deposits.registry.block.entries.cast

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class CastBlock(properties: Properties) : Block(properties) {

	override fun getShape(
		state: BlockState,
		level: BlockGetter,
		pos: BlockPos,
		context: CollisionContext
	): VoxelShape = SHAPE

	companion object {
		private fun voxel(value: Int): Double = value / 16.0

		private val SHAPE: VoxelShape = Shapes.box(voxel(3), voxel(0), voxel(5), voxel(13), voxel(3), voxel(11))
	}
}
