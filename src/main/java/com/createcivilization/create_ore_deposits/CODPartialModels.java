package com.createcivilization.create_ore_deposits;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SpriteShifter;

public class CODPartialModels {

    public static final PartialModel

            DRILL_COIL = block("drill/drill_coil"),
            HOSE = block("drill/rope"),
            DRILL_MAGNET = block("drill/pulley_drill"),
            HOSE_HALF = block("drill/rope_half"),
            HOSE_HALF_MAGNET = block("drill/rope_half_drill");

    //Sprite Shift This should not be here I just dont wanna make a new class rn.

    public static final SpriteShiftEntry DRILL_PULLEY_COIL = get("block/drill/hose_pulley_coil", "block/drill/hose_pulley_coil_scroll");

    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(CreateOreDeposits.asResource(originalLocation), CreateOreDeposits.asResource(targetLocation));
    }


    private static PartialModel block(String path) {
        return PartialModel.of(CreateOreDeposits.asResource("block/" + path));
    }


    public static void init() {
        // init static fields
    }


}
