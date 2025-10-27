package com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill

import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositsBlockEntities

import com.simibubi.create.content.kinetics.base.KineticBlockEntity

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState


class DepositDrillBlockEntity(
	type : BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : KineticBlockEntity(type, pos, blockState) {

	fun getInterpolatedOffset(pt: Float) : Float {
		return 0f
	}
}