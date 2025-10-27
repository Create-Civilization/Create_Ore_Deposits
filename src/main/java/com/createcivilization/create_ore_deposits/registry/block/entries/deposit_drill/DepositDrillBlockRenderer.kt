package com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill

import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels.Companion.DRILL_MAGNET
import com.simibubi.create.AllPartialModels
import com.simibubi.create.AllSpriteShifts
import com.simibubi.create.content.contraptions.pulley.AbstractPulleyRenderer
import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlock
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels.Companion.HOSE
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels.Companion.HOSE_HALF
import dev.engine_room.flywheel.lib.model.baked.PartialModel
import net.createmod.catnip.render.CachedBuffers
import net.createmod.catnip.render.SpriteShiftEntry
import net.createmod.catnip.render.SuperByteBuffer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.Direction


class DepositDrillBlockRenderer(
	context: BlockEntityRendererProvider.Context,
) : AbstractPulleyRenderer<DepositDrillBlockEntity>(context, HOSE, HOSE_HALF) {

	protected override fun getShaftAxis(be: DepositDrillBlockEntity): Direction.Axis {
		return be.blockState
			.getValue(HosePulleyBlock.HORIZONTAL_FACING)
			.clockWise
			.axis;
	}

	protected override fun getCoil(): PartialModel {
		return AllPartialModels.HOSE_COIL;
	}

	protected override fun getCoilShift(): SpriteShiftEntry? {
		return AllSpriteShifts.HOSE_PULLEY_COIL
	}

	override fun renderRope(be: DepositDrillBlockEntity): SuperByteBuffer {
		return CachedBuffers.partial(HOSE, be.blockState)
	}

	override fun renderMagnet(be: DepositDrillBlockEntity): SuperByteBuffer {
		return CachedBuffers.partial(DRILL_MAGNET, be.blockState);
	}

	override fun getOffset(
		be: DepositDrillBlockEntity,
		partialTicks: Float
	): Float {
		return be.getInterpolatedOffset(partialTicks);
	}

	override fun isRunning(be: DepositDrillBlockEntity?): Boolean {
		return true
	}
}