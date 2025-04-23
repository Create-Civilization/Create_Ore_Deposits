package com.createcivilization.create_ore_deposits.content.ponder.scene;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.UnaryOperator;

public class DrillScene {
    public static void basic(SceneBuilder builder, SceneBuildingUtil util){

        //I had a feeling you would look here, what if when the drill mines and drops unrefined ore
        //We make it so they have to like test the unrefined ore to see what mineral is in the deposit'
        //Idk tho

        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("drill.intro", "Setting Up And Using A Drill");
        scene.configureBasePlate(0,0, 5);
        scene.showBasePlate();

        scene.idle(5);

        Selection drill = util.select().position(2,5,2);
        Selection cogAndShaft = util.select().fromTo(0,2,5,1,5,2);
        BlockPos depositPos = util.grid().at(2,0,2);
        Selection funnel = util.select().position(3,5,2);


        ElementLink<WorldSectionElement> drillLink = scene.world().showIndependentSection(drill, Direction.UP);


        scene.idle(5);
        scene.overlay().showText(60)
                .text("This is the drill... It Drills")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(drill.getCenter());
        scene.idle(70);
        scene.overlay().showText(60)
                .text("This is the deposit. This is what the Drill Drills.")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().topOf(depositPos));
        scene.idle(70);
        scene.overlay().showText(60)
                .text("Apply power to the drill")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(drill.getCenter());
        scene.idle(10);
        scene.world().showSectionAndMerge(cogAndShaft, Direction.EAST, drillLink);
        scene.idle(5);
        Selection kinetics = util.select().fromTo(0,2,5,2,5,2);
        scene.world().setKineticSpeed(kinetics, 32);
        scene.idle(55);


        for (int i = 0; i < 10; i++) {
            scene.idle(10);
            scene.world().incrementBlockBreakingProgress(depositPos);
            if (i == 1) {
                scene.overlay().showText(80)
                        .attachKeyFrame()
                        .placeNearTarget()
                        .pointAt(util.vector().topOf(depositPos))
                        .text("When a running drill hits a deposit it will break it.");
            }
        }
        scene.idle(30);
        scene.world().setKineticSpeed(kinetics, 0);
        scene.idle(5);
        scene.world().showSection(funnel, Direction.WEST);
        scene.idle(10);
        scene.overlay().showText(60)
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(funnel.getCenter())
                .text("A max of 64 items are stored in the drill. Get them out.");
        scene.idle(10);

    }
}
