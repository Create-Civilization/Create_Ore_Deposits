package com.createcivilization.create_ore_deposits;

import com.simibubi.create.Create;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class CODPartialModels {

    public static final PartialModel

            DRILL_HOSE = block("drill/rope"),
            DRILL_HEAD = block("drill/pulley_drill"),
            DRILL_HOSE_HALF = block("drill/rope_half"),
            DRILL_HOSE_DRILL = block("drill/rope_half_drill");

    private static PartialModel block(String path) {
        return PartialModel.of(CreateOreDeposits.asResource("block/" + path));
    }


}
