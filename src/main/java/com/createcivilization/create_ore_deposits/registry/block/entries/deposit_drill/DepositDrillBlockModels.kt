package com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill

import com.createcivilization.create_ore_deposits.util.resource

import dev.engine_room.flywheel.lib.model.baked.PartialModel

import net.createmod.catnip.render.SpriteShiftEntry
import net.createmod.catnip.render.SpriteShifter

object DepositDrillBlockModels {

	val DRILL_COIL: PartialModel = "drill/drill_coil".block()
	val HOSE: PartialModel = "drill/rope".block()
	val DRILL_MAGNET: PartialModel = "drill/pulley_drill".block()
	val HOSE_HALF: PartialModel = "drill/rope_half".block()
	val HOSE_HALF_MAGNET: PartialModel = "drill/rope_half_drill".block()

	val DRILL_PULLEY_COIL: SpriteShiftEntry = get("block/drill/hose_pulley_coil", "block/drill/hose_pulley_coil_scroll")

	@Suppress("SameParameterValue")
	private fun get(originalLocation: String, targetLocation: String): SpriteShiftEntry =
		SpriteShifter.get(originalLocation.resource(), targetLocation.resource())

	private fun String.block(): PartialModel = PartialModel.of("block/$this".resource())
}