package com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill

import com.createcivilization.create_ore_deposits.util.translate
import com.createcivilization.create_ore_deposits.registry.tag.CreateOreDepositsTags
import com.createcivilization.create_ore_deposits.registry.fluid.CreateOreDepositsFluids
import com.createcivilization.create_ore_deposits.registry.fluid.FluidHandler

import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity
import com.simibubi.create.foundation.utility.BlockHelper
import com.simibubi.create.foundation.utility.ServerSpeedProvider

import net.createmod.catnip.animation.LerpedFloat
import net.createmod.catnip.nbt.NBTHelper

import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties

import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler

import java.util.function.Predicate

import kotlin.math.roundToInt

// Minimum value for LerpedFloat to not get jumpy
private const val min = 0.5

class DepositDrillBlockEntity(
	type : BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : BlockBreakingKineticBlockEntity(type, pos, blockState) {

	/**
	 * Allows array access syntax on the [itemHandler] property.
	 */
	operator fun ItemStackHandler.get(index: Int): ItemStack = this.getStackInSlot(index)

	// The Float value distance from the bottom of the drill, should always be positive
	private var drillOffset: Float = 0f
	private var lerpedOffset: LerpedFloat = LerpedFloat.linear().startWithValue(min)
	private var lastBlock: Block? = null

	private val itemHandler: ItemStackHandler = ItemStackHandler()
	private val fluidHandler: FluidHandler = FluidHandler(
		1000,
		mutableSetOf(
			CreateOreDepositsFluids.LUBRICANT,
			CreateOreDepositsFluids.LUBRICANT.source
		)
	) // Put lube here

	override fun tick() {
		super.tick()

		// Compiler inlining will optimize this don't worry.
		val targetBlock = getTargetBlock()
		val targetBlockIsAir = targetBlock == Blocks.AIR
		val movementSpeed = getMovementSpeed()
		val canMove = targetBlockIsAir || movementSpeed < 0

		if (canMove) {
			drillOffset = (movementSpeed + drillOffset).coerceAtLeast(0f)
			lerpedOffset.forceNextSync()
			setLerpedOffset(drillOffset)
		} else {
			lerpedOffset.forceNextSync()
			setLerpedOffset(drillOffset.roundToInt())
		}
	}

	override fun lazyTick() {
		super.lazyTick()
		setChanged()
		sendData()
		if (getMovementSpeed() != 0f) fluidHandler.drain(1, IFluidHandler.FluidAction.EXECUTE)
	}

	override fun onBlockBroken(stateToBreak: BlockState) {
		lastBlock = getTargetBlock()
		BlockHelper.destroyBlock(level, breakingPos, 1f) { drops: ItemStack -> itemHandler.insertItem(0, drops, false) }
	}

	override fun getBreakingPos(): BlockPos {
		val inventory = itemHandler[0]
		val inventoryNotFull = inventory.count != itemHandler.getSlotLimit(0)
		val targetBlockIsTheSameAsLastBlock = getTargetBlock() == lastBlock

		val canMine = inventory.isEmpty || (inventoryNotFull && targetBlockIsTheSameAsLastBlock)

		return if (canMine) getTargetPos() else BlockPos.ZERO
	}

	override fun calculateStressApplied(): Float =
		if (fluidHandler.getFluidInTank(0).amount > 1) 512f / 2f else 512f

	override fun read(compound: CompoundTag, registries: HolderLookup.Provider, clientPacket: Boolean) {
		val nbt = compound.getCompound("Deposit_Drill")

		drillOffset = nbt.getFloat("DrillOffset")
		if (nbt.contains("LastBlock")) lastBlock = BuiltInRegistries.BLOCK.get(NBTHelper.readResourceLocation(nbt, "LastBlock"))
		itemHandler.deserializeNBT(registries, nbt.getCompound("ItemHandler"))
		fluidHandler.deserializeNBT(registries, nbt.getCompound("FluidHandler"))

		super.read(compound, registries, clientPacket)
	}

	override fun write(compound: CompoundTag, registries: HolderLookup.Provider, clientPacket: Boolean) {
		val nbt = CompoundTag()
		nbt.putFloat("DrillOffset", drillOffset)
		if (lastBlock != null) NBTHelper.writeResourceLocation(nbt, "LastBlock", BuiltInRegistries.BLOCK.getKey(lastBlock!!))
		nbt.put("ItemHandler", itemHandler.serializeNBT(registries))
		nbt.put("FluidHandler", fluidHandler.serializeNBT(registries))

		compound.put("Deposit_Drill", nbt)
		super.write(compound, registries, clientPacket)
	}

	override fun addToGoggleTooltip(tooltip: MutableList<Component>, isPlayerSneaking: Boolean): Boolean {
		translate("tooltip.drill.header").forGoggles(tooltip)

		val targetBlock = level?.getBlockState(getDrillTipPos())?.block!!
		if (targetBlock != Blocks.AIR)
			translate("tooltip.drill.drilling", Component.translatable(targetBlock.descriptionId))
				.style(ChatFormatting.GRAY)
				.forGoggles(tooltip)

		if (!itemHandler[0].isEmpty)
			translate("tooltip.drill.contains", Component.translatable(itemHandler[0].descriptionId), itemHandler[0].count)
				.style(ChatFormatting.GREEN)
				.forGoggles(tooltip)

		val fluidInTank = fluidHandler.getFluidInTank(0)
		if (!fluidInTank.isEmpty)
			translate("tooltip.drill.contains.lube", Component.translatable(fluidInTank.descriptionId), fluidInTank.amount)
				.style(ChatFormatting.BLUE)
				.forGoggles(tooltip)

		return super.addToGoggleTooltip(tooltip, isPlayerSneaking)
	}

	fun setLerpedOffset(value: Number) {
		lerpedOffset.setValue(value.toDouble().coerceAtLeast(min))
	}

	fun getTargetBlock(): Block = getTargetBlockState()?.block!!

	fun getTargetBlockState(): BlockState? {
		// This now simply returns the block state at the resolved target position.
		return level?.getBlockState(getTargetPos())
	}

	fun getTargetPos(): BlockPos {
		val tip = getDrillTipPos()
		val stateAtTip = level?.getBlockState(tip)

		val blockAtDrillTipIsADepositBlock = stateAtTip?.let(::isBlockStateADeposit) == true

		return if (blockAtDrillTipIsADepositBlock) getFurthestDepositConnectedToDeposit(level!!, tip) else tip
	}

	private fun isBlockStateADeposit(state: BlockState): Boolean =
		state.`is`(CreateOreDepositsTags.DEPOSIT)

	private fun getFurthestDepositConnectedToDeposit(
		level: Level,
		startingDepositPos: BlockPos
	): BlockPos {
		val startingDepositState = level.getBlockState(startingDepositPos)
		val connectedBlocks = getConnectedBlocksWithFilter(level, startingDepositPos, startingDepositState::equals)

		return connectedBlocks.maxByOrNull { blockPos ->
			startingDepositPos.distSqr(blockPos)
		} ?: startingDepositPos // fallback if list is empty
	}

	private fun getConnectedBlocksWithFilter(
		level: Level,
		startingDepositPos: BlockPos,
		filter: Predicate<BlockState>
	): List<BlockPos> {
		val connected = mutableListOf<BlockPos>()
		val visited = mutableListOf<BlockPos>()
		val queue = ArrayDeque<BlockPos>()

		queue += startingDepositPos
		visited += startingDepositPos

		while (queue.isNotEmpty()) {
			val current = queue.removeFirst()

			if (!filter.test(level.getBlockState(current))) continue

			connected.add(current)

			val offsets = setOf(
				BlockPos(1, 0, 0),
				BlockPos(-1, 0, 0),
				BlockPos(0, 1, 0),
				BlockPos(0, -1, 0),
				BlockPos(0, 0, 1),
				BlockPos(0, 0, -1)
			)
			val neighbors = offsets.map { pos -> current.offset(pos) }

			for (neighbor in neighbors) {
				if (neighbor !in visited && filter.test(level.getBlockState(neighbor))) {
					visited.add(neighbor)
					queue.add(neighbor)
				}
			}
		}

		return connected
	}

	fun getDrillTipPos(): BlockPos = blockPos.offset(0, (-lerpedOffset.value.toInt() - 1), 0)

	fun getInterpolatedOffset(partialTicks: Float): Float = lerpedOffset.getValue(partialTicks).coerceAtLeast(3 / 16f)

	fun getMovementSpeed(): Float {
		var movementSpeed = convertToLinear(getSpeed())
		if (level!!.isClientSide) movementSpeed *= ServerSpeedProvider.get()
		return movementSpeed
	}

	private val facingAxis: Direction.Axis = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).axis

	fun getItemHandler(direction: Direction): IItemHandler? {
		//Grabs block state and checks if the right side of the block. If it is we can slap a funnel on it.
		return if (direction == blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).counterClockWise) itemHandler else null
	}

	fun getFluidHandler(direction: Direction): FluidHandler? {
		// Checks if the axis is the Y axis (up and down) and if its positive (just up) thus from the top
		return if (direction.axis == Direction.Axis.Y && direction.axisDirection == Direction.AxisDirection.POSITIVE) fluidHandler else null
	}
}