package com.createcivilization.create_ore_deposits.content.ponder.scene;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.function.UnaryOperator;

public class DrillScene {

    public static void basic(SceneBuilder builder, SceneBuildingUtil util) {

        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("drill.intro", "Setting Up and Using a Drill");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        scene.idle(5);

        // === Selections ===
        Selection drill = util.select().position(2, 5, 2);
        Selection cogAndShaft = util.select().fromTo(0, 2, 5, 1, 5, 2);
        Selection funnel = util.select().position(3, 5, 2);
        Selection depositVein = util.select().fromTo(0, 1, 0, 3, 2, 3);
        Selection kinetics = util.select().fromTo(0, 2, 5, 2, 5, 2);

        BlockPos depositPos = util.grid().at(0, 1, 0);
        BlockPos funnelPos = util.grid().at(3, 5, 2);

        // === Show Drill ===
        ElementLink<WorldSectionElement> drillLink = scene.world().showIndependentSection(drill, Direction.UP);
        scene.idle(5);

        scene.overlay().showText(60)
                .text("This is the drill... it drills.")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(drill.getCenter());
        scene.idle(70);

        // === Show Deposit Vein ===
        ElementLink<WorldSectionElement> depositLink = scene.world().showIndependentSection(depositVein, Direction.WEST);
        scene.overlay().showText(60)
                .text("This is a deposit vein. This is what the drill mines.")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().centerOf(2, 1, 2));
        scene.idle(70);

        // === Power the Drill ===
        scene.overlay().showText(60)
                .text("Apply power to the drill.")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(drill.getCenter());
        scene.idle(10);

        scene.world().showSectionAndMerge(cogAndShaft, Direction.EAST, drillLink);
        scene.idle(5);

        scene.world().setKineticSpeed(kinetics, 32);
        scene.idle(35);

        scene.overlay().showText(40)
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().topOf(depositPos))
                .text("When a running drill hits a deposit, it will break the furthest deposit in the vein.");

        for (int i = 0; i < 10; i++) {
            scene.idle(8);
            scene.world().incrementBlockBreakingProgress(depositPos);

            if (i == 6) {
                scene.overlay().showText(40)
                        .attachKeyFrame()
                        .placeNearTarget()
                        .pointAt(util.vector().topOf(depositPos))
                        .text("It will keep doing this until the entire deposit vein is depleted.");
            }
        }

        scene.idle(40);
        scene.world().hideIndependentSection(depositLink, Direction.WEST);
        scene.idle(45);
        scene.world().showSection(funnel, Direction.WEST);
        scene.idle(20);

        scene.overlay().showText(80)
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(funnel.getCenter())
                .text("A max of 64 items are stored in the drill. Get them out.");
        scene.idle(40);

        ItemStack rawIron = new ItemStack(Items.RAW_IRON, 64);
        scene.world().flapFunnel(funnelPos, true);
        scene.world().createItemEntity(funnelPos.getCenter(), Vec3.ZERO, rawIron);
        scene.idle(10);
    }
}
