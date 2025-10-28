package com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.CreateOreDeposits.asResource
import dev.engine_room.flywheel.lib.model.baked.PartialModel
import net.createmod.catnip.render.SpriteShiftEntry
import net.createmod.catnip.render.SpriteShifter


class DepositDrillBlockModels {
	companion object {
		val DRILL_COIL: PartialModel = block("drill/drill_coil")
		val HOSE: PartialModel = block("drill/rope")
		val DRILL_MAGNET: PartialModel = block("drill/pulley_drill")
		val HOSE_HALF: PartialModel = block("drill/rope_half")
		val HOSE_HALF_MAGNET: PartialModel = block("drill/rope_half_drill")

		val DRILL_PULLEY_COIL: SpriteShiftEntry =
			get("block/drill/hose_pulley_coil", "block/drill/hose_pulley_coil_scroll")

		private fun get(originalLocation: String, targetLocation: String): SpriteShiftEntry {
			return SpriteShifter.get(asResource(originalLocation), asResource(targetLocation))
		}

		private fun block(path: String): PartialModel {
			return PartialModel.of(asResource("block/$path"))
		}

		fun init() {
			// init static fields
		}
	}
}