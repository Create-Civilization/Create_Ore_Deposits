package com.createcivilization.create_ore_deposits.registry.block.entries

import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositsBlocks
import com.createcivilization.create_ore_deposits.registry.blockentities.CreateOreDepositsBlockEntities
import com.createcivilization.create_ore_deposits.registry.blockentities.entries.DepositDrillBlockEntity

import com.simibubi.create.AllBlocks
import com.simibubi.create.AllShapes
import com.simibubi.create.content.contraptions.piston.PistonExtensionPoleBlock
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock
import com.simibubi.create.foundation.block.IBE
import com.simibubi.create.infrastructure.config.AllConfigs

import net.createmod.catnip.lang.Lang

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.util.StringRepresentable
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class DepositDrillBlock(properties: Properties) : DirectionalAxisKineticBlock(properties), IBE<DepositDrillBlockEntity> {



	constructor() : this(Properties.of())

	init {
		registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(STATE, DrillState.RETRACTED))
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
		builder.add(STATE)
		super.createBlockStateDefinition(builder)
	}

	public override fun neighborChanged(state: BlockState, level: Level, pos: BlockPos, block: Block, fromPos: BlockPos, isMoving: Boolean) {
		val direction = state.getValue(FACING)
		if (fromPos != pos.relative(direction.opposite)) return
		if (!level.isClientSide && !level.blockTicks
				.willTickThisTick(pos, this)
		) level.scheduleTick(pos, this, 1)
	}

	public override fun tick(state: BlockState, worldIn: ServerLevel, pos: BlockPos, r: RandomSource) {
		val direction = state.getValue(FACING)
		val pole = worldIn.getBlockState(pos.relative(direction.opposite))
		if (!AllBlocks.PISTON_EXTENSION_POLE.has(pole)) return
		if (pole.getValue(PistonExtensionPoleBlock.FACING).axis != direction.axis) return
		withBlockEntityDo(worldIn, pos) { be: DepositDrillBlockEntity ->
			if (be.lastExceptionAccess == null) return@withBlockEntityDo
			be.lastExceptionAccess = null
			be.sendData()
		}
	}

	override fun onWrenched(state: BlockState, context: UseOnContext?): InteractionResult? {
		if (state.getValue<DrillState>(STATE) != DrillState.RETRACTED) return InteractionResult.PASS
		return super.onWrenched(state, context)
	}

	enum class DrillState : StringRepresentable {
		RETRACTED, MOVING, EXTENDED;

		override fun getSerializedName(): String = Lang.asId(name)
	}

	override fun playerWillDestroy(worldIn: Level, pos: BlockPos, state: BlockState, player: Player?): BlockState {
		val direction = state.getValue(FACING)
		var pistonHead: BlockPos? = null
		val dropBlocks = player == null || !player.isCreative

		val maxPoles: Int = maxAllowedPistonPoles()
		for (offset in 1..<maxPoles) {
			val currentPos = pos.relative(direction, offset)
			val block = worldIn.getBlockState(currentPos)

			if (isExtensionPole(block) && direction.axis == block.getValue(BlockStateProperties.FACING).axis) continue

			if (isPistonHead(block) && block.getValue(BlockStateProperties.FACING) == direction) pistonHead = currentPos

			break
		}

		if (pistonHead != null) BlockPos.betweenClosedStream(pos, pistonHead)
			.filter { p: BlockPos -> p != pos }
			.forEach { p: BlockPos -> worldIn.destroyBlock(p, dropBlocks) }

		for (offset in 1..<maxPoles) {
			val currentPos = pos.relative(direction.opposite, offset)
			val block = worldIn.getBlockState(currentPos)

			if (isExtensionPole(block) && direction.axis == block.getValue(BlockStateProperties.FACING).axis) {
				worldIn.destroyBlock(currentPos, dropBlocks)
				continue
			}

			break
		}

		return super.playerWillDestroy(worldIn, pos, state, player)
	}

	public override fun getShape(
		state: BlockState,
		worldIn: BlockGetter,
		pos: BlockPos,
		context: CollisionContext
	): VoxelShape = state.getValue(FACING).let {
		when (state.getValue(STATE)) {
			DrillState.EXTENDED -> AllShapes.MECHANICAL_PISTON_EXTENDED.get(it)
			DrillState.MOVING -> AllShapes.MECHANICAL_PISTON.get(it)
			else -> Shapes.block()
		}
	}

	override fun getBlockEntityClass(): Class<DepositDrillBlockEntity> = DepositDrillBlockEntity::class.java

	override fun getBlockEntityType(): BlockEntityType<out DepositDrillBlockEntity> = CreateOreDepositsBlockEntities.DEPOSIT_DRILL

	companion object {

		val STATE: EnumProperty<DrillState> = EnumProperty.create("state", DrillState::class.java)

		fun maxAllowedPistonPoles(): Int = AllConfigs.server().kinetics.maxPistonPoles.get()

		fun isDepositDrill(state: BlockState): Boolean = state.`is`(CreateOreDepositsBlocks.DEPOSIT_DRILL)

		fun isExtensionPole(state: BlockState): Boolean = AllBlocks.PISTON_EXTENSION_POLE.has(state)

		fun isPistonHead(state: BlockState): Boolean = AllBlocks.MECHANICAL_PISTON_HEAD.has(state)
	}
}