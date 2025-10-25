package com.createcivilization.create_ore_deposits.registry.blockentities.entries

import com.createcivilization.create_ore_deposits.registry.block.entries.DepositDrillBlock
import com.createcivilization.create_ore_deposits.registry.blockentities.CreateOreDepositsBlockEntities
import com.simibubi.create.content.contraptions.TranslatingContraption

import com.simibubi.create.content.contraptions.piston.LinearActuatorBlockEntity
import com.simibubi.create.content.contraptions.piston.PistonContraption
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

class DepositDrillBlockEntity(
	pos: BlockPos,
	state: BlockState
) : LinearActuatorBlockEntity(CreateOreDepositsBlockEntities.DEPOSIT_DRILL, pos, state) {

	override fun disassemble() {
		TODO("Not yet implemented")
	}

	override fun assemble() {
		if (level?.getBlockState(worldPosition)?.block !is DepositDrillBlock) return

		val direction: Direction = blockState.getValue(BlockStateProperties.FACING)


		// Collect Construct
		val contraption = PistonContraption(direction, movementSpeed < 0)
		if (!contraption.assemble(level, worldPosition)) return

		val positive: Direction = Direction.get(Direction.AxisDirection.POSITIVE, direction.axis);
		val movementDirection: Direction = getSpeed() > 0 ^ direction.getAxis() != Axis.Z ? positive : positive.getOpposite();

		BlockPos anchor = contraption.anchor.relative(direction, contraption.initialExtensionProgress);
		if (ContraptionCollider.isCollidingWithWorld(level, contraption, anchor.relative(movementDirection),
				movementDirection))
			return;

		// Check if not at limit already
		extensionLength = contraption.extensionLength;
		float resultingOffset = contraption.initialExtensionProgress + Math.signum(movementSpeed) * .5f;
		if (resultingOffset <= 0 || resultingOffset >= extensionLength) return

		// Run
		running = true;
		offset = contraption.initialExtensionProgress;
		sendData();
		clientOffsetDiff = 0;

		BlockPos startPos = BlockPos.ZERO.relative(direction, contraption.initialExtensionProgress);
		contraption.removeBlocksFromWorld(level, startPos);
		movedContraption = ControlledContraptionEntity.create(getLevel(), this, contraption);
		resetContraptionToOffset();
		forceMove = true;
		level.addFreshEntity(movedContraption);

		AllSoundEvents.CONTRAPTION_ASSEMBLE.playOnServer(level, worldPosition);

		if (contraption.containsBlockBreakers())
			award(AllAdvancements.CONTRAPTION_ACTORS);
	}

	override fun getExtensionRange(): Int = 15

	override fun getInitialOffset(): Int = 0

	override fun getMovementModeSlot(): ValueBoxTransform {
		TODO("Not yet implemented")
	}

	override fun toMotionVector(speed: Float): Vec3 {}

	override fun toPosition(offset: Float): Vec3 = this.blockPos.let {
		Vec3((it.x + offset).toDouble(), (it.y + offset).toDouble(), (it.z + offset).toDouble())
	}
}

class DrillBlockContraption(
	extensionLength: Int,
	initialExtensionProgress: Int,
	orientation: Direction,
	pistonExtensionCollisionBox: AABB,
	retract: boolean
) : TranslatingContraption() {

}