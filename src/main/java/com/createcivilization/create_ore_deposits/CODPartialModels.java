package com.createcivilization.create_ore_deposits;

import com.simibubi.create.Create;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class CODPartialModels {

    public static final PartialModel

            HOSE_COIL = block("hose_pulley/hose_coil"),
            HOSE = block("hose_pulley/rope"),
            HOSE_MAGNET = block("hose_pulley/pulley_magnet"),
            HOSE_HALF = block("hose_pulley/rope_half"),
            HOSE_HALF_MAGNET = block("hose_pulley/rope_half_magnet");

    private static PartialModel block(String path) {
        return PartialModel.of(CreateOreDeposits.asResource("block/" + path));
    }


}
