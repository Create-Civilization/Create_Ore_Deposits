package com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill

import com.createcivilization.create_ore_deposits.registry.fluid.FluidHandler
import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity
import com.simibubi.create.foundation.utility.BlockHelper
import com.simibubi.create.foundation.utility.ServerSpeedProvider
import net.createmod.catnip.animation.LerpedFloat
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler
import java.util.function.Consumer
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
	private var lastBlock: Block? = null

	private val itemHandler: IItemHandler = ItemStackHandler()
	private val fluidHandler: FluidHandler = FluidHandler(1000, null) // Put lube here

	override fun tick() {
		super.tick()

		// Compiler inlining will optimize this don't worry.
		val targetBlock = getTargetBlock()
		val targetBlockIsAir = targetBlock == Blocks.AIR
		val itemStack = itemHandler.getStackInSlot(0)
		val itemStackIsFull = itemStack.count != itemHandler.getSlotLimit(0)
		val targetBlockIsTheSameAsLastBlock = targetBlock == lastBlock
		val movementSpeed = getMovementSpeed()

		if ((targetBlockIsAir && (itemStack.isEmpty || (itemStackIsFull && targetBlockIsTheSameAsLastBlock))) || movementSpeed < 0) {
			drillOffset = (movementSpeed + drillOffset).coerceAtLeast(0f)
			lerpedOffset.forceNextSync()
			setLerpedOffset(drillOffset)
		} else {
			lerpedOffset.forceNextSync()
			setLerpedOffset(drillOffset.roundToInt())
		}
	}

	override fun onBlockBroken(stateToBreak: BlockState) {
		lastBlock = getTargetBlock()
		BlockHelper.destroyBlock(level, breakingPos, 1f, Consumer { drops: ItemStack ->
			itemHandler.insertItem(0, drops, false)
		})
	}

	override fun getBreakingPos(): BlockPos {
		return getTargetPos()
	}

	fun setLerpedOffset(value: Number) {
		lerpedOffset.setValue(value.toDouble().coerceAtLeast(min))
	}

	fun getTargetBlock() : Block {
		return level?.getBlockState(getTargetPos())?.block!!
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

	private val facingAxis: Direction.Axis = blockState.getValue(BlockStateProperties.HORIZONTAL_AXIS)

	fun getItemHandler(direction: Direction): IItemHandler? {
		// Checks if the axis is the same as the facing axis and if the axis direction is the opposite of the facing
		// In short, checks if the direction is the opposite of the facing direction
		return if (direction.axis == facingAxis && direction.axisDirection == Direction.AxisDirection.NEGATIVE) itemHandler else null
	}

	fun getFluidHandler(direction: Direction): FluidHandler? {
		// Checks if the axis is the Y axis (up and down) and if its positive (just up) thus from the top
		return if (direction.axis == Direction.Axis.Y && direction.axisDirection == Direction.AxisDirection.POSITIVE) fluidHandler else null
	}

}