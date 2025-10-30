package com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill

import com.simibubi.create.AllPartialModels
import com.simibubi.create.AllSpriteShifts
import com.simibubi.create.content.contraptions.pulley.AbstractPulleyRenderer

import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels.HOSE
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels.HOSE_HALF
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels.DRILL_MAGNET

import dev.engine_room.flywheel.lib.model.baked.PartialModel

import net.createmod.catnip.render.CachedBuffers
import net.createmod.catnip.render.SpriteShiftEntry
import net.createmod.catnip.render.SuperByteBuffer

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.properties.BlockStateProperties

class DepositDrillBlockRenderer(
	context: BlockEntityRendererProvider.Context,
) : AbstractPulleyRenderer<DepositDrillBlockEntity>(context, HOSE, HOSE_HALF) {

	protected override fun getShaftAxis(be: DepositDrillBlockEntity): Direction.Axis =
		be.blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).clockWise.axis

	protected override fun getCoil(): PartialModel = AllPartialModels.HOSE_COIL

	protected override fun getCoilShift(): SpriteShiftEntry = AllSpriteShifts.HOSE_PULLEY_COIL

	override fun renderRope(be: DepositDrillBlockEntity): SuperByteBuffer = CachedBuffers.partial(HOSE, be.blockState)

	override fun renderMagnet(be: DepositDrillBlockEntity): SuperByteBuffer = CachedBuffers.partial(DRILL_MAGNET, be.blockState)

	override fun getOffset(be: DepositDrillBlockEntity, partialTicks: Float): Float = be.getInterpolatedOffset(partialTicks)

	override fun isRunning(be: DepositDrillBlockEntity?): Boolean = true
}