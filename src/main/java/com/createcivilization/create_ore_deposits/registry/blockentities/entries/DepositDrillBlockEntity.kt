package com.createcivilization.create_ore_deposits.registry.blockentities.entries

import com.createcivilization.create_ore_deposits.registry.block.entries.DepositDrillBlock
import com.createcivilization.create_ore_deposits.registry.blockentities.CreateOreDepositsBlockEntities
import com.simibubi.create.AllBlocks
import com.simibubi.create.AllSoundEvents
import com.simibubi.create.content.contraptions.AssemblyException
import com.simibubi.create.content.contraptions.ContraptionCollider
import com.simibubi.create.content.contraptions.ControlledContraptionEntity
import com.simibubi.create.content.contraptions.DirectionalExtenderScrollOptionSlot
import com.simibubi.create.content.contraptions.piston.LinearActuatorBlockEntity
import com.simibubi.create.content.kinetics.base.IRotate
import com.simibubi.create.foundation.advancement.AllAdvancements
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform
import com.simibubi.create.foundation.utility.ServerSpeedProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Mth
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.Vec3
import kotlin.math.sign

class DepositDrillBlockEntity(
	pos: BlockPos,
	state: BlockState
) : LinearActuatorBlockEntity(CreateOreDepositsBlockEntities.DEPOSIT_DRILL, pos, state) {

	var lastExceptionAccess: AssemblyException? by this::lastException

	protected var hadCollisionWithOtherPiston: Boolean = false
	protected var extensionLength: Int = 0

	override fun read(compound: CompoundTag, registries: HolderLookup.Provider, clientPacket: Boolean) {
		extensionLength = compound.getInt("ExtensionLength")
		super.read(compound, registries, clientPacket)
	}

	override fun write(tag: CompoundTag, registries: HolderLookup.Provider, clientPacket: Boolean) {
		tag.putInt("ExtensionLength", extensionLength)
		super.write(tag, registries, clientPacket)
	}

	@Throws(AssemblyException::class)
	public override fun assemble() {
		if (level!!.getBlockState(worldPosition).block !is DepositDrillBlock) return

		val direction = blockState.getValue(BlockStateProperties.FACING)

		// Collect Construct
		val contraption = DepositDrillContraption(direction, getMovementSpeed() < 0)
		if (!contraption.assemble(level!!, worldPosition)) return

		val positive = Direction.get(Direction.AxisDirection.POSITIVE, direction.axis)
		val movementDirection =
			if ((getSpeed() > 0) xor (direction.axis != Direction.Axis.Z)) positive else positive.opposite

		val anchor = contraption.anchor.relative(direction, contraption.initialExtensionProgress)
		if (ContraptionCollider.isCollidingWithWorld(level, contraption, anchor.relative(movementDirection), movementDirection)) return

		// Check if not at limit already
		extensionLength = contraption.extensionLength
		val resultingOffset = contraption.initialExtensionProgress + sign(getMovementSpeed()) * .5f
		if (resultingOffset <= 0 || resultingOffset >= extensionLength) return

		// Run
		running = true
		offset = contraption.initialExtensionProgress.toFloat()
		sendData()
		clientOffsetDiff = 0f

		val startPos = BlockPos.ZERO.relative(direction, contraption.initialExtensionProgress)
		contraption.removeBlocksFromWorld(level, startPos)
		movedContraption = ControlledContraptionEntity.create(getLevel(), this, contraption)
		resetContraptionToOffset()
		forceMove = true
		level!!.addFreshEntity(movedContraption)

		AllSoundEvents.CONTRAPTION_ASSEMBLE.playOnServer(level, worldPosition)

		if (contraption.containsBlockBreakers()) award(AllAdvancements.CONTRAPTION_ACTORS)
	}

	override fun disassemble() {
		if (!running && movedContraption == null) return
		if (!remove) level!!.setBlock(
			worldPosition,
			blockState.setValue(DepositDrillBlock.STATE, DepositDrillBlock.DrillState.EXTENDED),
			3 or 16
		)
		if (movedContraption != null) {
			resetContraptionToOffset()
			movedContraption.disassemble()
			AllSoundEvents.CONTRAPTION_DISASSEMBLE.playOnServer(level, worldPosition)
		}
		running = false
		movedContraption = null
		sendData()

		if (remove) AllBlocks.MECHANICAL_PISTON.get().playerWillDestroy(level, worldPosition, blockState, null)
	}

	override fun collided() {
		super.collided()
		if (!running && getMovementSpeed() > 0) assembleNextTick = true
	}

	override fun getMovementSpeed(): Float {
		var movementSpeed = Mth.clamp(convertToLinear(getSpeed()), -.49f, .49f)
		if (level!!.isClientSide) movementSpeed *= ServerSpeedProvider.get()
		val pistonDirection = blockState.getValue(BlockStateProperties.FACING)
		val movementModifier = pistonDirection.axisDirection.step * (if (pistonDirection.axis == Direction.Axis.Z) -1 else 1)
		movementSpeed = movementSpeed * -movementModifier + clientOffsetDiff / 2f

		val extensionRange = getExtensionRange()
		movementSpeed = Mth.clamp(movementSpeed, 0 - offset, extensionRange - offset)
		if (sequencedOffsetLimit >= 0) movementSpeed = Mth.clamp(movementSpeed.toDouble(), -sequencedOffsetLimit, sequencedOffsetLimit).toFloat()
		return movementSpeed
	}

	override fun getExtensionRange(): Int {
		return extensionLength
	}

	override fun visitNewPosition() {}

	override fun toMotionVector(speed: Float): Vec3 {
		val pistonDirection = blockState.getValue(BlockStateProperties.FACING)
		return Vec3.atLowerCornerOf(pistonDirection.normal)
			.scale(speed.toDouble())
	}

	override fun toPosition(offset: Float): Vec3 =
		Vec3.atLowerCornerOf(blockState.getValue(BlockStateProperties.FACING).normal).scale(offset.toDouble()).add(Vec3.atLowerCornerOf(movedContraption.getContraption().anchor))

	override fun getMovementModeSlot(): ValueBoxTransform = DirectionalExtenderScrollOptionSlot { state: BlockState, d: Direction ->
		val axis = d.axis
		val extensionAxis = state.getValue(BlockStateProperties.FACING).axis
		val shaftAxis = (state.block as IRotate).getRotationAxis(state)
		extensionAxis != axis && shaftAxis != axis
	}

	override fun getInitialOffset(): Int = if (movedContraption == null) 0 else (movedContraption.getContraption() as DepositDrillContraption).initialExtensionProgress
}