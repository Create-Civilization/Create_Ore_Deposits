package com.createcivilization.create_ore_deposits.registry.blockentities.entries

import com.createcivilization.create_ore_deposits.registry.block.entries.DepositDrillBlock
import com.simibubi.create.AllBlocks
import com.simibubi.create.AllContraptionTypes
import com.simibubi.create.api.contraption.BlockMovementChecks
import com.simibubi.create.api.contraption.ContraptionType
import com.simibubi.create.content.contraptions.AssemblyException
import com.simibubi.create.content.contraptions.TranslatingContraption
import com.simibubi.create.content.contraptions.piston.PistonExtensionPoleBlock
import com.simibubi.create.infrastructure.config.AllConfigs
import net.createmod.catnip.math.VecHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.WoolCarpetBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.PistonType
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.apache.commons.lang3.tuple.Pair
import java.util.*

class DepositDrillContraption(var orientation: Direction, val retract: Boolean) : TranslatingContraption() {

	var extensionLength: Int = 0
	var initialExtensionProgress: Int = 0

	private var pistonExtensionCollisionBox: AABB? = null

	override fun getType(): ContraptionType = AllContraptionTypes.PISTON.value()

	override fun assemble(world: Level, pos: BlockPos): Boolean {
		if (!collectExtensions(world, pos, orientation)) return false
		val count = blocks.size
		if (!searchMovedStructure(world, anchor, if (retract) orientation.opposite else orientation)) return false
		bounds = if (blocks.size == count) pistonExtensionCollisionBox else bounds.minmax(pistonExtensionCollisionBox)
		startMoving(world)
		return true
	}

	@Throws(AssemblyException::class)
	private fun collectExtensions(world: Level, pos: BlockPos, direction: Direction): Boolean {
		val poles: MutableList<StructureBlockInfo> = ArrayList<StructureBlockInfo>()
		var actualStart = pos
		var nextBlock = world.getBlockState(actualStart.relative(direction))
		var extensionsInFront = 0
		val blockState = world.getBlockState(pos)

		if (!DepositDrillBlock.isDepositDrill(blockState))
			return false

		if (blockState.getValue(DepositDrillBlock.STATE) == DepositDrillBlock.DrillState.EXTENDED) {
			while (
				PistonExtensionPoleBlock.PlacementHelper.get().matchesAxis(nextBlock, direction.axis)
				|| DepositDrillBlock.isPistonHead(nextBlock)
				&& nextBlock.getValue(BlockStateProperties.FACING) == direction
			) {
				actualStart = actualStart.relative(direction)
				poles.add(
					StructureBlockInfo(
						actualStart,
						nextBlock.setValue(BlockStateProperties.FACING, direction),
						null
					)
				)
				extensionsInFront++

				if (DepositDrillBlock.isPistonHead(nextBlock)) break // CHANGE WHEN WE HAVE OUR OWN POLES

				nextBlock = world.getBlockState(actualStart.relative(direction))
				if (extensionsInFront > DepositDrillBlock.maxAllowedPistonPoles()) throw AssemblyException.tooManyPistonPoles()
			}
		}

		if (extensionsInFront == 0) poles.add(
			StructureBlockInfo(
				pos,
				AllBlocks.MECHANICAL_PISTON_HEAD.defaultState
					.setValue(BlockStateProperties.FACING, direction)
					.setValue(BlockStateProperties.PISTON_TYPE, PistonType.DEFAULT),
				null
			)
		)
		else poles.add(
			StructureBlockInfo(
				pos,
				AllBlocks.PISTON_EXTENSION_POLE.defaultState.setValue(BlockStateProperties.FACING, direction),
				null
			)
		)

		var end = pos
		nextBlock = world.getBlockState(end.relative(direction.opposite))
		var extensionsInBack = 0

		while (PistonExtensionPoleBlock.PlacementHelper.get().matchesAxis(nextBlock, direction.axis)) {
			end = end.relative(direction.opposite)
			poles.add(StructureBlockInfo(end, nextBlock.setValue(BlockStateProperties.FACING, direction), null))
			extensionsInBack++
			nextBlock = world.getBlockState(end.relative(direction.opposite))

			if (extensionsInFront + extensionsInBack > DepositDrillBlock.maxAllowedPistonPoles()) throw AssemblyException.tooManyPistonPoles()
		}

		anchor = pos.relative(direction, initialExtensionProgress + 1)
		extensionLength = extensionsInBack + extensionsInFront
		initialExtensionProgress = extensionsInFront
		pistonExtensionCollisionBox = AABB(
			Vec3.atLowerCornerOf(BlockPos.ZERO.relative(direction, -1)),
			Vec3.atLowerCornerOf(BlockPos.ZERO.relative(direction, -extensionLength - 1))
		).expandTowards(1.0, 1.0, 1.0)

		if (extensionLength == 0) throw AssemblyException.noPistonPoles()

		bounds = AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

		for (pole in poles) {
			val relPos = pole.pos().relative(direction, -extensionsInFront)
			val localPos = relPos.subtract(anchor)
			getBlocks()[localPos] = StructureBlockInfo(localPos, pole.state(), null)
			//pistonExtensionCollisionBox = pistonExtensionCollisionBox.union(AABB(localPos));
		}

		return true
	}

	override fun isAnchoringBlockAt(pos: BlockPos): Boolean =
		pistonExtensionCollisionBox?.contains(VecHelper.getCenterOf(pos.subtract(anchor))) ?: false

	@Throws(AssemblyException::class)
	override fun addToInitialFrontier(
		world: Level,
		pos: BlockPos,
		direction: Direction,
		frontier: Queue<BlockPos?>
	): Boolean {
		frontier.clear()
		val retracting = direction != orientation
		if (retracting) return true
		for (offset in 0..AllConfigs.server().kinetics.maxChassisRange.get()) {
			if (offset == 1 && retracting) return true
			val currentPos = pos.relative(orientation, offset + initialExtensionProgress)
			if (retracting && world.isOutsideBuildHeight(currentPos)) return true
			if (!world.isLoaded(currentPos)) throw AssemblyException.unloadedChunk(currentPos)
			val state = world.getBlockState(currentPos)
			if (!BlockMovementChecks.isMovementNecessary(state, world, currentPos)) return true
			if (BlockMovementChecks.isBrittle(state) && state.block !is WoolCarpetBlock) return true
			if (DepositDrillBlock.isPistonHead(state) && state.getValue(BlockStateProperties.FACING) == direction.opposite) return true
			if (!BlockMovementChecks.isMovementAllowed(state, world, currentPos)) if (retracting) return true
			else throw AssemblyException.unmovableBlock(currentPos, state)
			if (retracting && state.pistonPushReaction == PushReaction.PUSH_ONLY) return true
			frontier.add(currentPos)
			if (BlockMovementChecks.isNotSupportive(state, orientation)) return true
		}
		return true
	}

	public override fun addBlock(level: Level?, pos: BlockPos, capture: Pair<StructureBlockInfo, BlockEntity>) {
		super.addBlock(level, pos.relative(orientation, -initialExtensionProgress), capture)
	}

	public override fun toLocalPos(globalPos: BlockPos): BlockPos =
		globalPos.subtract(anchor).relative(orientation, -initialExtensionProgress)

	override fun customBlockPlacement(world: LevelAccessor, pos: BlockPos, state: BlockState): Boolean {
		val pistonPos = anchor.relative(orientation, -1)
		val pistonState = world.getBlockState(pistonPos)
		val be = world.getBlockEntity(pistonPos)
		if (pos == pistonPos) {
			if (be == null || be.isRemoved) return true
			if (!DepositDrillBlock.isExtensionPole(state) && DepositDrillBlock.isDepositDrill(pistonState)) world.setBlock(
				pistonPos,
				pistonState.setValue(DepositDrillBlock.STATE, DepositDrillBlock.DrillState.RETRACTED),
				3 or 16
			)
			return true
		}
		return false
	}

	override fun customBlockRemoval(world: LevelAccessor, pos: BlockPos, state: BlockState): Boolean {
		val pistonPos = anchor.relative(orientation, -1)
		val blockState = world.getBlockState(pos)
		if (pos == pistonPos && DepositDrillBlock.isDepositDrill(blockState)) {
			world.setBlock(
				pos,
				blockState.setValue(DepositDrillBlock.STATE, DepositDrillBlock.DrillState.MOVING),
				66 or 16
			)
			return true
		}
		return false
	}

	override fun readNBT(world: Level?, nbt: CompoundTag, spawnData: Boolean) {
		super.readNBT(world, nbt, spawnData)
		initialExtensionProgress = nbt.getInt("InitialLength")
		extensionLength = nbt.getInt("ExtensionLength")
		orientation = Direction.from3DDataValue(nbt.getInt("Orientation"))
	}

	override fun writeNBT(registries: HolderLookup.Provider, spawnPacket: Boolean): CompoundTag {
		val tag = super.writeNBT(registries, spawnPacket)
		tag.putInt("InitialLength", initialExtensionProgress)
		tag.putInt("ExtensionLength", extensionLength)
		tag.putInt("Orientation", orientation.get3DDataValue())
		return tag
	}
}