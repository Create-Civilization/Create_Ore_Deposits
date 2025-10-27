package com.createcivilization.create_ore_deposits.registry.fluid.entries

import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositsBlocks
import com.createcivilization.create_ore_deposits.registry.fluid.CreateOreDepositsFluids
import com.createcivilization.create_ore_deposits.registry.item.CreateOreDepositsItems

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.Item
import net.minecraft.world.level.*
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.FluidState

import net.neoforged.neoforge.fluids.FluidType

abstract class TestFluid : FlowingFluid() {

	override fun getFluidType(): FluidType = FluidType(FluidType.Properties.create())

	override fun getFlowing(): Fluid = CreateOreDepositsFluids.TEST_FLUID_FLOWING

	override fun getSource(): Fluid = CreateOreDepositsFluids.TEST_FLUID

	override fun getBucket(): Item = CreateOreDepositsItems.TEST_BUCKET

	// NeoForge marks this as deprecated and suggests using their injected interface method instead. Ignore their suggestion.
	@Suppress("OVERRIDE_DEPRECATION")
	override fun canConvertToSource(level: Level): Boolean =
		level.gameRules.getBoolean(GameRules.RULE_WATER_SOURCE_CONVERSION)

	override fun beforeDestroyingBlock(level: LevelAccessor, pos: BlockPos, state: BlockState) = Unit

	override fun getSlopeFindDistance(level: LevelReader): Int = 4

	override fun getDropOff(level: LevelReader): Int = 1

	override fun canBeReplacedWith(
		state: FluidState,
		level: BlockGetter,
		pos: BlockPos,
		fluid: Fluid,
		direction: Direction
	): Boolean = direction == Direction.DOWN && !isSame(fluid)

	override fun isSame(fluid: Fluid): Boolean = fluid is TestFluid

	override fun getTickDelay(level: LevelReader): Int = 5

	override fun getExplosionResistance(): Float = 100.0f

	override fun createLegacyBlock(state: FluidState): BlockState =
		CreateOreDepositsBlocks.TEST_FLUID.defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state))

	class Flowing : TestFluid() {

		override fun createFluidStateDefinition(builder: StateDefinition.Builder<Fluid, FluidState>) {
			super.createFluidStateDefinition(builder)
			builder.add(LEVEL)
		}

		override fun getAmount(state: FluidState): Int = state.getValue(LEVEL)

		override fun isSource(state: FluidState): Boolean = false
	}

	class Source : TestFluid() {

		override fun getAmount(state: FluidState): Int = 8

		override fun isSource(state: FluidState): Boolean = true
	}
}