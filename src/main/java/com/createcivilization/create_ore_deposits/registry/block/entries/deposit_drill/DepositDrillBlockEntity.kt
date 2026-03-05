package com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill

import com.createcivilization.create_ore_deposits.config.Config
import com.createcivilization.create_ore_deposits.registry.datamap.CreateOreDepositsDataMaps.DEPOSIT_DATA
import com.createcivilization.create_ore_deposits.registry.datamap.CreateOreDepositsDataMaps.LUBRICANT_FACTOR_DATA
import com.createcivilization.create_ore_deposits.registry.fluid.CreateOreDepositsFluids
import com.createcivilization.create_ore_deposits.registry.fluid.FluidHandler
import com.createcivilization.create_ore_deposits.registry.tag.CreateOreDepositsTags
import com.createcivilization.create_ore_deposits.util.translate
import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity
import com.simibubi.create.foundation.item.TooltipHelper
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
import net.minecraft.nbt.NbtUtils
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler
import kotlin.math.roundToInt

private const val MIN_LERP = 0.5

class DepositDrillBlockEntity(
	type: BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : BlockBreakingKineticBlockEntity(type, pos, blockState) {

	private var drillOffset: Float = 0f
	private var lerpedOffset: LerpedFloat = LerpedFloat.linear().startWithValue(MIN_LERP)
	private var lastBlock: Block? = null
	private var temperature: Float = Config.SERVER.DEPOSIT_DRILL.baseTemperature
	private var maxAttempts: Int = 0
	private var remainingAttempts: Int = 0
	private var drillTickCounter: Int = 0
	private var currentDepositPos: BlockPos? = null

	private val itemHandler = ItemStackHandler()
	private val drillTipHandler = ItemStackHandler()

	private val lubricantHandler = FluidHandler(
		1000,
		mutableSetOf(
			CreateOreDepositsFluids.LUBRICANT.get(),
			CreateOreDepositsFluids.LUBRICANT.get().source
		)
	)

	private val coolantHandler = FluidHandler(
		1000,
		mutableSetOf(
			Fluids.WATER,
			Fluids.FLOWING_WATER
		)
	)

	override fun tick() {
		super.tick()

		val movementSpeed = getMovementSpeed()
		val canMove = getTargetBlock() == Blocks.AIR || movementSpeed < 0

		lerpedOffset.forceNextSync()
		if (canMove) {
			drillOffset = (movementSpeed + drillOffset).coerceAtLeast(0f)
			setLerpedOffset(drillOffset)
		} else {
			setLerpedOffset(drillOffset.roundToInt())
		}

		updateTemperature()
		onBreakTick()
		damageTip()
	}

	override fun lazyTick() {
		super.lazyTick()
		setChanged()
		sendData()
		if (getMovementSpeed() != 0f) lubricantHandler.drain(1, FluidAction.EXECUTE)
	}

	override fun canBreak(stateToBreak: BlockState, blockHardness: Float): Boolean {
		if (isDeposit(stateToBreak)) return false
		return super.canBreak(stateToBreak, blockHardness)
	}

	override fun getBreakSpeed(): Float = super.getBreakSpeed()

	override fun getBreakingPos(): BlockPos = if (canMine()) getTargetPos() else BlockPos.ZERO

	/**
	 * Handles per-tick extraction from deposit blocks. Unlike normal blocks
	 * (which break all at once), deposits yield drops over many ticks based
	 * on [maxAttempts] and the current extraction interval.
	 */
	fun onBreakTick() {
		if (level?.isClientSide != false) return
		if (!canMine()) return

		val targetPos = getTargetPos()
		val blockState = getTargetBlockState() ?: return

		if (!isDeposit(blockState)) {
			currentDepositPos = null
			return
		}

		if (currentDepositPos != targetPos) {
			clearDestroyProgress()
			currentDepositPos = targetPos
			maxAttempts = blockState.blockHolder.getData(DEPOSIT_DATA)?.maxAttempts ?: 0
			remainingAttempts = maxAttempts
			drillTickCounter = 0
		}

		if (remainingAttempts <= 0) return

		drillTickCounter++
		val extractInterval = calculateExtractionInterval()

		if (drillTickCounter >= extractInterval) {
			drillTickCounter = 0
			remainingAttempts--

			val serverLevel = level as? ServerLevel ?: return
			for (stack in getSimulatedDrops(blockState, serverLevel, targetPos)) {
				itemHandler.insertItem(0, stack, false)
			}

			updateDestroyProgress(targetPos)

			if (remainingAttempts <= 0) {
				level?.destroyBlockProgress(blockPos.hashCode(), targetPos, -1)
				level?.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3)
				currentDepositPos = null
			}
		}
	}

	fun canMine(): Boolean {
		val tip = drillTipHandler.getStackInSlot(0)
		if (tip.isEmpty) return false

		val inventory = itemHandler.getStackInSlot(0)
		if (inventory.isEmpty) return true

		val notFull = inventory.count != itemHandler.getSlotLimit(0)
		return notFull && getTargetBlock() == lastBlock
	}

	fun calculateExtractionInterval(): Int = 1025 - (speed * 4).roundToInt()

	fun getSimulatedDrops(state: BlockState, serverLevel: ServerLevel, pos: BlockPos): List<ItemStack> {
		return Block.getDrops(state, serverLevel, pos, null, null, ItemStack.EMPTY)
	}

	override fun onBlockBroken(stateToBreak: BlockState) {
		lastBlock = getTargetBlock()
		BlockHelper.destroyBlock(level, breakingPos, 1f) { drops: ItemStack ->
			if (!isDeposit(stateToBreak)) {
				itemHandler.insertItem(0, drops, false)
			}
		}
	}

	fun clearDestroyProgress() {
		currentDepositPos?.let { pos ->
			level?.destroyBlockProgress(blockPos.hashCode(), pos, -1)
		}
	}

	fun updateDestroyProgress(targetPos: BlockPos) {
		if (maxAttempts <= 0) return
		val attemptsUsed = maxAttempts - remainingAttempts
		val stage = ((attemptsUsed.toFloat() / maxAttempts) * 10f).toInt().coerceIn(0, 9)
		level?.destroyBlockProgress(blockPos.hashCode(), targetPos, stage)
	}


	//TODO: Issue where temps are jumping around in the tooltip.
	fun updateTemperature() {
		val baseCooling = Config.SERVER.DEPOSIT_DRILL.baseCooling
		val baseTemperature = Config.SERVER.DEPOSIT_DRILL.baseTemperature

		val targetState = getTargetBlockState()
		val isMining = targetState != null && isDeposit(targetState) && canMine() && speed > 0

		val heatGen = if (isMining) speed * getBlockHardness(targetState) else 0.0f

		val dissipation = (baseCooling + getLubricantFactor() + getCoolingFactor()).coerceAtLeast(0.1f)
		val equilibriumTemp = baseTemperature + (heatGen / dissipation)
		val approachRate = (0.02f * dissipation).coerceIn(0.01f, 1.0f)

		temperature += (equilibriumTemp - temperature) * approachRate
	}

	private fun damageTip() {
		val itemStack = drillTipHandler.getStackInSlot(0)
		if (itemStack.isEmpty || !itemStack.tags.anyMatch(CreateOreDepositsTags.DRILL_TIP::equals)) return

		val excessTemp = (temperature - Config.SERVER.DEPOSIT_DRILL.baseTemperature).coerceAtLeast(0f)

		val damage = when {
			excessTemp < 20f  -> 0
			excessTemp < 50f  -> 1
			excessTemp < 100f -> ((excessTemp - 50f) / 25f).toInt() + 1
			else              -> ((excessTemp - 100f) / 20f + 3f).toInt().coerceAtMost(8)
		}

		if (damage < 1) return

		val world = level as? ServerLevel ?: return
		itemStack.hurtAndBreak(damage, world, null) {
			drillTipHandler.setStackInSlot(0, ItemStack.EMPTY)
			notifyUpdate()
		}
	}

	override fun calculateStressApplied(): Float {
		// SU = 128 * (4 - lubeFactor) * hardness
		val lubricantFactor = getLubricantFactor()
		val hardness = getBlockHardness(getTargetBlockState())
		return 128 * (4 - lubricantFactor) * hardness
	}

	fun getBlockHardness(blockState: BlockState?): Float {
		return blockState?.blockHolder?.getData(DEPOSIT_DATA)?.hardness ?: 1.0f
	}

	fun getLubricantFactor(): Float {
		return lubricantHandler.getFluidInTank(1)
			.fluidHolder
			.getData(LUBRICANT_FACTOR_DATA)
			?.lubeFactor ?: 0.0f
	}

	fun getCoolingFactor(): Float {
		return coolantHandler.getFluidInTank(0)
			.fluidHolder
			.getData(LUBRICANT_FACTOR_DATA)
			?.lubeFactor ?: 0.0f
	}

	private fun isDeposit(state: BlockState?): Boolean = state?.`is`(CreateOreDepositsTags.DEPOSIT) ?: false

	fun getTargetBlock(): Block? = getTargetBlockState()?.block

	fun getTargetBlockState(): BlockState? = level?.getBlockState(getTargetPos())

	fun getTargetPos(): BlockPos {
		val tip = getDrillTipPos()
		val stateAtTip = level?.getBlockState(tip)

		return if (stateAtTip?.let(::isDeposit) == true) {
			getFurthestConnectedDeposit(level!!, tip)
		} else {
			tip
		}
	}

	fun getDrillTipPos(): BlockPos = blockPos.offset(0, -lerpedOffset.value.toInt() - 1, 0)

	private fun getFurthestConnectedDeposit(level: Level, start: BlockPos): BlockPos {
		val startState = level.getBlockState(start)
		return getFurthestConnectedBlock(level, start) { it == startState }
	}

	private fun getFurthestConnectedBlock(
		level: Level,
		start: BlockPos,
		filter: (BlockState) -> Boolean
	): BlockPos {
		val visited = mutableSetOf(start)
		val queue = ArrayDeque<BlockPos>()
		queue += start
		var last = start

		while (queue.isNotEmpty()) {
			val current = queue.removeFirst()
			last = current

			for (dir in Direction.entries) {
				val neighbor = current.relative(dir)
				if (neighbor !in visited && filter(level.getBlockState(neighbor))) {
					visited += neighbor
					queue += neighbor
				}
			}
		}

		return last
	}

	fun getInterpolatedOffset(partialTicks: Float): Float =
		lerpedOffset.getValue(partialTicks).coerceAtLeast(3 / 16f)

	fun getMovementSpeed(): Float {
		var movementSpeed = convertToLinear(getSpeed())
		if (level!!.isClientSide) movementSpeed *= ServerSpeedProvider.get()
		return movementSpeed
	}

	private fun setLerpedOffset(value: Number) {
		lerpedOffset.setValue(value.toDouble().coerceAtLeast(MIN_LERP))
	}

	override fun read(compound: CompoundTag, registries: HolderLookup.Provider, clientPacket: Boolean) {
		val nbt = compound.getCompound("DepositDrill")

		maxAttempts = nbt.getInt("MaxAttempts")
		remainingAttempts = nbt.getInt("RemainingAttempts")
		drillTickCounter = nbt.getInt("DrillTickCount")
		drillOffset = nbt.getFloat("DrillOffset")
		temperature = nbt.getFloat("Temperature")

		if (nbt.contains("LastBlock"))
			lastBlock = BuiltInRegistries.BLOCK.get(NBTHelper.readResourceLocation(nbt, "LastBlock"))
		if (nbt.contains("CurrentDepositPos"))
			currentDepositPos = NBTHelper.readBlockPos(nbt, "CurrentDepositPos")

		itemHandler.deserializeNBT(registries, nbt.getCompound("ItemHandler"))
		drillTipHandler.deserializeNBT(registries, nbt.getCompound("DrillTipHandler"))
		lubricantHandler.deserializeNBT(registries, nbt.getCompound("Lubricant"))
		coolantHandler.deserializeNBT(registries, nbt.getCompound("Coolant"))

		super.read(compound, registries, clientPacket)
	}

	override fun write(compound: CompoundTag, registries: HolderLookup.Provider, clientPacket: Boolean) {
		val nbt = CompoundTag()

		nbt.putInt("MaxAttempts", maxAttempts)
		nbt.putInt("RemainingAttempts", remainingAttempts)
		nbt.putInt("DrillTickCount", drillTickCounter)
		nbt.putFloat("DrillOffset", drillOffset)
		nbt.putFloat("Temperature", temperature)

		lastBlock?.let {
			NBTHelper.writeResourceLocation(nbt, "LastBlock", BuiltInRegistries.BLOCK.getKey(it))
		}
		currentDepositPos?.let {
			nbt.put("CurrentDepositPos", NbtUtils.writeBlockPos(it))
		}

		nbt.put("ItemHandler", itemHandler.serializeNBT(registries))
		nbt.put("DrillTipHandler", drillTipHandler.serializeNBT(registries))
		nbt.put("Lubricant", lubricantHandler.serializeNBT(registries))
		nbt.put("Coolant", coolantHandler.serializeNBT(registries))

		compound.put("DepositDrill", nbt)
		super.write(compound, registries, clientPacket)
	}

	override fun addToGoggleTooltip(tooltip: MutableList<Component>, isPlayerSneaking: Boolean): Boolean {
		translate("tooltip.drill.header").forGoggles(tooltip)

		// Currently Drilling
		val targetBlock = getTargetBlockState()?.block
		if (targetBlock != null && targetBlock != Blocks.AIR) {
			translate("tooltip.drill.drilling", Component.translatable(targetBlock.descriptionId))
				.style(ChatFormatting.GRAY)
				.forGoggles(tooltip)
		}

		// Breaking Progress
		if (isDeposit(getTargetBlockState())) {
			val attemptsUsed = maxAttempts - remainingAttempts
			val stage = ((attemptsUsed.toFloat() / maxAttempts) * 10f).toInt().coerceIn(0, 10)
			val bar = TooltipHelper.makeProgressBar(10, stage)
			translate("tooltip.drill.progress")
				.add(Component.literal(bar))
				.forGoggles(tooltip)
		}

		// Drill Tip
		val tipHandler = getDrillTipItemHandler()
		if (!tipHandler.getStackInSlot(0).isEmpty) {
			val tipStack: ItemStack = tipHandler.getStackInSlot(0)

			translate("tooltip.drill.tip.contains", Component.translatable(tipStack.item.descriptionId))
				.style(ChatFormatting.GREEN)
				.forGoggles(tooltip)

			if (tipStack.maxDamage > 0) {
				val currentDurability = tipStack.maxDamage - tipStack.damageValue
				val percentage = (currentDurability * 100) / tipStack.maxDamage
				translate("tooltip.drill.tip.durability", currentDurability, tipStack.maxDamage, percentage)
					.style(ChatFormatting.YELLOW)
					.forGoggles(tooltip)
			}
		}

		// Lubricant
		val lubeInTank = lubricantHandler.getFluidInTank(0)
		if (!lubeInTank.isEmpty) {
			translate("tooltip.drill.contains.lube",
				Component.translatable(lubeInTank.descriptionId),
				lubeInTank.amount)
				.style(ChatFormatting.GOLD)
				.forGoggles(tooltip)
		}

		// Coolant
		val coolantInTank = coolantHandler.getFluidInTank(0)
		if (!coolantInTank.isEmpty) {
			translate("tooltip.drill.contains.coolant",
				Component.translatable(coolantInTank.descriptionId),
				coolantInTank.amount)
				.style(ChatFormatting.AQUA)
				.forGoggles(tooltip)
		}

		// Heat
		translate("tooltip.drill.heat", temperature.toInt())
			.style(ChatFormatting.RED)
			.forGoggles(tooltip)

		return super.addToGoggleTooltip(tooltip, isPlayerSneaking)
	}

	fun getItemHandler(direction: Direction): IItemHandler? {
		val outputSide = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).counterClockWise
		return if (direction == outputSide) itemHandler else null
	}

	fun getDrillTipItemHandler(): IItemHandler = drillTipHandler

	fun getFluidHandler(direction: Direction): FluidHandler? = when {
		direction.axis == Direction.Axis.Y
			&& direction.axisDirection == Direction.AxisDirection.POSITIVE -> lubricantHandler
		direction == blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).opposite -> coolantHandler
		else -> null
	}
}