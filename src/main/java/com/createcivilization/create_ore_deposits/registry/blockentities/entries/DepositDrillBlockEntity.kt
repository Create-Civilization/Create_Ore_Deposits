package com.createcivilization.create_ore_deposits.registry.blockentities.entries

import com.createcivilization.create_ore_deposits.registry.blockentities.CreateOreDepositsBlockEntities

import com.simibubi.create.content.contraptions.piston.LinearActuatorBlockEntity
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3

class DepositDrillBlockEntity(
	pos: BlockPos,
	state: BlockState
) : LinearActuatorBlockEntity(CreateOreDepositsBlockEntities.DEPOSIT_DRILL, pos, state) {

	override fun disassemble() {
		TODO("Not yet implemented")
	}

	override fun assemble() {
		TODO("Not yet implemented")
	}

	override fun getExtensionRange(): Int {
		TODO("Not yet implemented")
	}

	override fun getInitialOffset(): Int {
		TODO("Not yet implemented")
	}

	override fun getMovementModeSlot(): ValueBoxTransform {
		TODO("Not yet implemented")
	}

	override fun toMotionVector(speed: Float): Vec3 {
		TODO("Not yet implemented")
	}

	override fun toPosition(offset: Float): Vec3 {
		TODO("Not yet implemented")
	}
}