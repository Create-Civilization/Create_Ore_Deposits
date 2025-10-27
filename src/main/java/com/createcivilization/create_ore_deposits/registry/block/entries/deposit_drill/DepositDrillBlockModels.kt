package com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import dev.engine_room.flywheel.lib.model.baked.PartialModel

class DepositDrillBlockModels {
	companion object {
		val DRILL_COIL: PartialModel = block("drill/drill_coil")
		val HOSE: PartialModel = block("drill/rope")
		val DRILL_MAGNET: PartialModel = block("drill/pulley_drill")
		val HOSE_HALF: PartialModel = block("drill/rope_half")
		val HOSE_HALF_MAGNET: PartialModel = block("drill/rope_half_drill")

		private fun block(path: String): PartialModel {
			return PartialModel.of(CreateOreDeposits.asResource("block/$path"))
		}

		fun init() {
			// init static fields
		}
	}
}