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
		private val SHAPE: VoxelShape = Shapes.box(3 / 16.0, 0 / 16.0, 5 / 16.0, 13 / 16.0, 3 / 16.0, 11 / 16.0)
	}
}
