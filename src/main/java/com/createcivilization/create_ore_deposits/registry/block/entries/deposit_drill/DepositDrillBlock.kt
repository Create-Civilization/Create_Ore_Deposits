package com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill

import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositsBlockEntities

import com.simibubi.create.content.kinetics.base.HorizontalAxisKineticBlock
import com.simibubi.create.foundation.block.IBE

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionResult
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

import java.util.function.Function

class DepositDrillBlock(properties: Properties) : HorizontalAxisKineticBlock(properties), IBE<DepositDrillBlockEntity> {

	override fun getBlockEntityClass(): Class<DepositDrillBlockEntity> = DepositDrillBlockEntity::class.java

	override fun getBlockEntityType(): BlockEntityType<out DepositDrillBlockEntity> =
		CreateOreDepositsBlockEntities.DEPOSIT_DRILL.get()

	override fun getRenderShape(pState: BlockState): RenderShape = RenderShape.MODEL

	override fun hasShaftTowards(world: LevelReader, pos: BlockPos, state: BlockState, face: Direction): Boolean =
		face.axis == state.getValue(HORIZONTAL_AXIS)

	override fun onBlockEntityUse(
		world: BlockGetter,
		pos: BlockPos,
		action: Function<DepositDrillBlockEntity, InteractionResult>
	): InteractionResult =
		if (!world.getBlockEntity(pos)!!.level!!.isClientSide) InteractionResult.SUCCESS
		else super.onBlockEntityUse(world, pos, action)

	// This code is required if you make it HorizontalKineticBlock
	// Switch Horizontal_axis to Horizontal_facing
//	override fun getRotationAxis(blockState: BlockState): Direction.Axis {
//		return (blockState.getValue(HORIZONTAL_FACING)).clockWise.axis
//	}
}