package com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill

import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity
import com.simibubi.create.foundation.utility.ServerSpeedProvider
import net.createmod.catnip.animation.LerpedFloat
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.roundToInt

// Minimum value for LerpedFloat to not get jumpy
private const val min = 0.5

class DepositDrillBlockEntity(
	type : BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : BlockBreakingKineticBlockEntity(type, pos, blockState) {

	// The Float value distance from the bottom of the drill, should always be positive
	private var drillOffset: Float = 0f
	private var lerpedOffset: LerpedFloat = LerpedFloat.linear().startWithValue(min)

	override fun tick() {
		super.tick()
		val movementSpeed = getMovementSpeed()
		if (level?.getBlockState(getTargetPos())?.block == Blocks.AIR || movementSpeed < 0) {
			drillOffset = (movementSpeed + drillOffset).coerceAtLeast(0f)
			setLerpedOffset(drillOffset)
		} else setLerpedOffset(drillOffset.roundToInt())
	}

	fun setLerpedOffset(value: Number) {
		lerpedOffset.setValue(value.toDouble().coerceAtLeast(min))
	}

	override fun getBreakingPos(): BlockPos {
		return getTargetPos()
	}

	fun getTargetPos() : BlockPos {
		return blockPos.offset(0, (-lerpedOffset.value.toInt() - 1), 0)
	}

	fun getInterpolatedOffset(partialTicks: Float) : Float {
		return lerpedOffset.getValue(partialTicks).coerceAtLeast(3 / 16f)
	}

	fun getMovementSpeed(): Float {
		var movementSpeed = convertToLinear(getSpeed())
		if (level!!.isClientSide) movementSpeed *= ServerSpeedProvider.get()
		return movementSpeed
	}
}