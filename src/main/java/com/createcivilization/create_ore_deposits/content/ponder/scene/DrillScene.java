package com.createcivilization.create_ore_deposits.content.ponder.scene;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.Direction;

public class DrillScene {
    public static void basic(SceneBuilder builder, SceneBuildingUtil util){
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("drill.intro", "HOLY SHIT THEY HIT THE SECOND TOWER");
        scene.configureBasePlate(0,0, 7);
        scene.showBasePlate();
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(5, 5, 5, 0, 1,0 ), Direction.DOWN);
        scene.idle(5);
        scene.overlay().showText(50)
                .text("World Trade Center!!")
                .placeNearTarget()
                .pointAt(util.vector().centerOf(3, 4, 2));
    }
}
