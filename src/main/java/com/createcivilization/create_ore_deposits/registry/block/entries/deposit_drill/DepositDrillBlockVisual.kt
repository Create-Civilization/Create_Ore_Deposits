package com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill

import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels.Companion.HOSE
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels.Companion.DRILL_MAGNET
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels.Companion.HOSE_HALF_MAGNET
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels.Companion.HOSE_HALF
import com.simibubi.create.AllPartialModels
import com.simibubi.create.AllSpriteShifts
import com.simibubi.create.content.processing.burner.ScrollInstance
import com.simibubi.create.foundation.render.AllInstanceTypes
import dev.engine_room.flywheel.api.instance.Instancer
import dev.engine_room.flywheel.api.visualization.VisualizationContext
import dev.engine_room.flywheel.lib.instance.InstanceTypes
import dev.engine_room.flywheel.lib.instance.TransformedInstance
import dev.engine_room.flywheel.lib.model.Models
import net.createmod.catnip.render.SpriteShiftEntry


class DepositDrillBlockVisual(dispatcher: VisualizationContext,
							  blockEntity: DepositDrillBlockEntity, partialTick: Float
) : com.simibubi.create.content.contraptions.pulley.AbstractPulleyVisual<DepositDrillBlockEntity>(dispatcher, blockEntity, partialTick) {

	override fun getRopeModel(): Instancer<TransformedInstance> {
		return instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(HOSE))
	}

	override fun getMagnetModel(): Instancer<TransformedInstance> {
		return instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(DRILL_MAGNET))
	}

	override fun getHalfMagnetModel(): Instancer<TransformedInstance> {
		return instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(HOSE_HALF_MAGNET))
	}

	override fun getCoilModel(): Instancer<ScrollInstance> {
		return instancerProvider().instancer(AllInstanceTypes.SCROLLING, Models.partial(AllPartialModels.HOSE_COIL))
	}

	override fun getHalfRopeModel(): Instancer<TransformedInstance> {
		return instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(HOSE_HALF))
	}

	override fun getOffset(pt: Float): Float {
		return blockEntity.getInterpolatedOffset(pt)
	}

	override fun isRunning(): Boolean {
		return true
	}

	override fun getCoilAnimation(): SpriteShiftEntry {
		return AllSpriteShifts.HOSE_PULLEY_COIL
	}
}