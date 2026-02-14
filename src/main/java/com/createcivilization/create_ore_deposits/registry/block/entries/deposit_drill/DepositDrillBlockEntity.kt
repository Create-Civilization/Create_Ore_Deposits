package com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill

import com.createcivilization.create_ore_deposits.config.Config
import com.createcivilization.create_ore_deposits.registry.datamap.CreateOreDepositsDataMaps
import com.createcivilization.create_ore_deposits.registry.datamap.CreateOreDepositsDataMaps.COOLING_FACTOR_DATA
import com.createcivilization.create_ore_deposits.registry.datamap.CreateOreDepositsDataMaps.HARDNESS_DATA
import com.createcivilization.create_ore_deposits.registry.datamap.CreateOreDepositsDataMaps.LUBRICANT_FACTOR_DATA
import com.createcivilization.create_ore_deposits.registry.fluid.CreateOreDepositsFluids
import com.createcivilization.create_ore_deposits.registry.fluid.FluidHandler
import com.createcivilization.create_ore_deposits.registry.tag.CreateOreDepositsTags
import com.createcivilization.create_ore_deposits.util.translate
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
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler
import java.util.function.Predicate
import kotlin.math.pow
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
	private var temperature: Float = 0f

	private val itemHandler: ItemStackHandler = ItemStackHandler()
	private val drillTipHandler: ItemStackHandler = ItemStackHandler()
	private val lubricantHandler: FluidHandler = FluidHandler(
		1000,
		mutableSetOf(
			CreateOreDepositsFluids.LUBRICANT.get(),
			CreateOreDepositsFluids.LUBRICANT.get().source
		)
	)
	private val coolantHandler: FluidHandler = FluidHandler(
		1000,
		mutableSetOf(
			Fluids.WATER,
			Fluids.FLOWING_WATER
		)
	)

	override fun tick() {
		super.tick()

		val movementSpeed: Float = getMovementSpeed()
		val canMove: Boolean = getTargetBlock() == Blocks.AIR || movementSpeed < 0

		if (canMove) {
			drillOffset = (movementSpeed + drillOffset).coerceAtLeast(0f)
			lerpedOffset.forceNextSync()
			setLerpedOffset(drillOffset)
		} else {
			lerpedOffset.forceNextSync()
			setLerpedOffset(drillOffset.roundToInt())
		}

		updateTemperature()
		damageTip()
	}

	// Due to this always being called IMMEDIATELY before adding to the break tick, this allows us to do something every break tick.
	override fun getBreakSpeed(): Float {
		onBreakTick()

		return super.getBreakSpeed()
	}

	/**
	 * Simulates block drops for deposits, allowing deposits to return drops every break tick
	 */
	fun onBreakTick() {
		if (!canMine())
			return
		val blockState = getTargetBlockState() ?: return
		if (!isBlockStateADeposit(blockState))
			return
		val serverLevel = level as? ServerLevel ?: return

		for (stack in getSimulatedDrops(blockState, serverLevel, getTargetPos())) {
			itemHandler.insertItem(0, stack, false)
		}
	}

	fun getSimulatedDrops(state: BlockState, serverLevel: ServerLevel, pos: BlockPos) : List<ItemStack> {
		return Block.getDrops(state, serverLevel, pos, null, null, ItemStack.EMPTY)
	}

	fun canMine(): Boolean {
		val inventory = itemHandler[0]
		val drillTipInventory = drillTipHandler[0]
		val inventoryNotFull = inventory.count != itemHandler.getSlotLimit(0)
		val targetBlockIsTheSameAsLastBlock = getTargetBlock() == lastBlock

		if(drillTipInventory.isEmpty){
			return false
		}

		return inventory.isEmpty || (inventoryNotFull && targetBlockIsTheSameAsLastBlock)
	}

	override fun lazyTick() {
		super.lazyTick()
		setChanged()
		sendData()
		if (getMovementSpeed() != 0f) lubricantHandler.drain(1, FluidAction.EXECUTE)
	}

	override fun onBlockBroken(stateToBreak: BlockState) {
		lastBlock = getTargetBlock()
		BlockHelper.destroyBlock(level, breakingPos, 1f) { drops: ItemStack ->
			// Since onBreakTick() already inserts deposit drops into the inventory, we skip them to not add twice on break
			if (!isBlockStateADeposit(stateToBreak))
				itemHandler.insertItem(0, drops, false)
		}
	}

	override fun getBreakingPos(): BlockPos = if (canMine()) getTargetPos() else BlockPos.ZERO

	override fun calculateStressApplied(): Float {
		val lubeFactor = getLubricantFactor().coerceIn(0f, 1f)
		val baseStress = 512f * getBlockHardness(getTargetBlockState())

		val stressMultiplier = 0.5f + 0.5f * (1f - lubeFactor).pow(0.5f)

		return baseStress * stressMultiplier
	}


	override fun read(compound: CompoundTag, registries: HolderLookup.Provider, clientPacket: Boolean) {
		val nbt: CompoundTag = compound.getCompound("DepositDrill")

		drillOffset = nbt.getFloat("DrillOffset")
		temperature = nbt.getFloat("Temperature")
		if (nbt.contains("LastBlock")) lastBlock = BuiltInRegistries.BLOCK.get(NBTHelper.readResourceLocation(nbt, "LastBlock"))
		itemHandler.deserializeNBT(registries, nbt.getCompound("ItemHandler"))
		drillTipHandler.deserializeNBT(registries, nbt.getCompound("DrillTipHandler"))
		lubricantHandler.deserializeNBT(registries, nbt.getCompound("Lubricant"))
		coolantHandler.deserializeNBT(registries, nbt.getCompound("Coolant"))

		super.read(compound, registries, clientPacket)
	}

	override fun write(compound: CompoundTag, registries: HolderLookup.Provider, clientPacket: Boolean) {
		val nbt = CompoundTag()
		nbt.putFloat("DrillOffset", drillOffset)
		nbt.putFloat("Temperature", temperature)
		if (lastBlock != null) NBTHelper.writeResourceLocation(nbt, "LastBlock", BuiltInRegistries.BLOCK.getKey(lastBlock!!))
		nbt.put("ItemHandler", itemHandler.serializeNBT(registries))
		nbt.put("DrillTipHandler", drillTipHandler.serializeNBT(registries))
		nbt.put("Lubricant", lubricantHandler.serializeNBT(registries))
		nbt.put("Coolant", coolantHandler.serializeNBT(registries))

		compound.put("DepositDrill", nbt)
		super.write(compound, registries, clientPacket)
	}

	override fun addToGoggleTooltip(tooltip: MutableList<Component>, isPlayerSneaking: Boolean): Boolean {
		translate("tooltip.drill.header").forGoggles(tooltip)

		val targetBlock: Block? = level?.getBlockState(getDrillTipPos())?.block
		if (targetBlock != null && targetBlock != Blocks.AIR)
			translate("tooltip.drill.drilling", Component.translatable(targetBlock.descriptionId))
				.style(ChatFormatting.GRAY)
				.forGoggles(tooltip)

		if (!itemHandler[0].isEmpty)
			translate("tooltip.drill.contains", Component.translatable(itemHandler[0].descriptionId), itemHandler[0].count)
				.style(ChatFormatting.GREEN)
				.forGoggles(tooltip)

		val fluidInLubricantTank: FluidStack = lubricantHandler.getFluidInTank(0)
		if (!fluidInLubricantTank.isEmpty)
			translate("tooltip.drill.contains.lube", Component.translatable(fluidInLubricantTank.descriptionId), fluidInLubricantTank.amount)
				.style(ChatFormatting.GOLD)
				.forGoggles(tooltip)

		val fluidInCoolantTank: FluidStack = coolantHandler.getFluidInTank(0)
		if (!fluidInCoolantTank.isEmpty)
			translate("tooltip.drill.contains.coolant", Component.translatable(fluidInCoolantTank.descriptionId), fluidInCoolantTank.amount)
				.style(ChatFormatting.BLUE)
				.forGoggles(tooltip)

		//Temp Prob

		if (!drillTipHandler[0].isEmpty) {
			val tipStack = drillTipHandler[0]
			translate("tooltip.drill.tip.contains", Component.translatable(tipStack.descriptionId))
				.style(ChatFormatting.GREEN)
				.forGoggles(tooltip)

			// Show durability if item is damageable
			if (tipStack.isDamageableItem) {
				val maxDurability = tipStack.maxDamage
				val currentDamage = tipStack.damageValue
				val remainingDurability = maxDurability - currentDamage
				val durabilityPercent = (remainingDurability.toFloat() / maxDurability * 100).roundToInt()

				val durabilityColor = when {
					durabilityPercent > 66 -> ChatFormatting.GREEN
					durabilityPercent > 33 -> ChatFormatting.YELLOW
					durabilityPercent > 10 -> ChatFormatting.GOLD
					else -> ChatFormatting.RED
				}

				translate("tooltip.drill.tip.durability", remainingDurability, maxDurability, durabilityPercent)
					.style(durabilityColor)
					.forGoggles(tooltip)
			}
		}


		translate("tooltip.drill.heat", String.format("%.2f", temperature))
			.style(ChatFormatting.RED)
			.forGoggles(tooltip)

		return super.addToGoggleTooltip(tooltip, isPlayerSneaking)
	}

	fun getBlockHardness(blockState: BlockState?): Float {
		val hardnessData: CreateOreDepositsDataMaps.HardnessData =
			blockState?.blockHolder?.getData(HARDNESS_DATA) ?: return 0.0f
		return hardnessData.hardness
	}

	fun getLubricantFactor(): Float {
		val lubricantFactorData: CreateOreDepositsDataMaps.LubricantFactorData =
			lubricantHandler.getFluidInTank(1).fluidHolder.getData(LUBRICANT_FACTOR_DATA) ?: return 0.0f
		return lubricantFactorData.lubeFactor
	}

	fun getCoolingFactor(): Float {
		val coolingFactorData: CreateOreDepositsDataMaps.CoolingFactorData =
			coolantHandler.getFluidInTank(1).fluidHolder.getData(COOLING_FACTOR_DATA) ?: return 0.0f
		return coolingFactorData.coolingFactor
	}

	fun updateTemperature() {
		val blockHardness = getBlockHardness(getTargetBlockState())
		val coolingFactor = getCoolingFactor()

		val baseCooling = Config.SERVER.DEPOSIT_DRILL.baseCooling
		val baseTemperature: Float = Config.SERVER.DEPOSIT_DRILL.baseTemperature
		val dampening = Config.SERVER.DEPOSIT_DRILL.dampening
		val scale = Config.SERVER.DEPOSIT_DRILL.scale
		val rpm: Float = if (this.speed < 0f) 0f else this.speed

		val heating = blockHardness * rpm.pow(scale) * dampening
		val effectiveCooling = baseCooling + (coolingFactor * 50f)
		val cooling = effectiveCooling * (temperature - baseTemperature) * dampening

		temperature = (temperature + heating - cooling).coerceAtLeast(baseTemperature)
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
		val tip: BlockPos = getDrillTipPos()
		val stateAtTip: BlockState? = level?.getBlockState(tip)

		val blockAtDrillTipIsADepositBlock: Boolean = stateAtTip?.let(::isBlockStateADeposit) == true

		return if (blockAtDrillTipIsADepositBlock) getFurthestDepositConnectedToDeposit(level!!, tip) else tip
	}

	private fun damageTip() {
		val itemStack = drillTipHandler[0]
		if (itemStack.isEmpty || !itemStack.tags.anyMatch(CreateOreDepositsTags.DRILL_TIP::equals)) return

		val baseTemp = Config.SERVER.DEPOSIT_DRILL.baseTemperature // ~293K
		val excessTemp = (temperature - baseTemp).coerceAtLeast(0f)

		val damage = when {
			excessTemp < 20f -> 0  // Safe: < 313K
			excessTemp < 50f -> 1  // Caution: 313-343K
			excessTemp < 100f -> ((excessTemp - 50f) / 25f).toInt() + 1 // Danger: 343-393K
			else -> ((excessTemp - 100f) / 20f + 3f).toInt().coerceAtMost(8) // Critical: > 393K
		}

		if (damage < 1) return

		val world = level
		if (world !is ServerLevel) return
		itemStack.hurtAndBreak(damage, world, null) {
			drillTipHandler.setStackInSlot(0, ItemStack.EMPTY)
			notifyUpdate()
		}
	}


	private fun isBlockStateADeposit(state: BlockState): Boolean = state.`is`(CreateOreDepositsTags.DEPOSIT)

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

	// FIXME: Why is this unused?  What was it meant for?  - Mavity
	private val facingAxis: Direction.Axis = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).axis

	fun getItemHandler(direction: Direction): IItemHandler? {
		//Grabs block state and checks if the right side of the block. If it is we can slap a funnel on it.
		return if (direction == blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).counterClockWise) itemHandler else null
	}

	fun getDrillTipItemHandler(): IItemHandler {
		return drillTipHandler
	}

	// Checks if the axis is the Y axis (up and down) and if its positive (just up) thus from the top
	fun getFluidHandler(direction: Direction): FluidHandler? = when {
		direction.axis == Direction.Axis.Y && direction.axisDirection == Direction.AxisDirection.POSITIVE -> lubricantHandler
		direction == blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).opposite -> coolantHandler
		else -> null
	}
}