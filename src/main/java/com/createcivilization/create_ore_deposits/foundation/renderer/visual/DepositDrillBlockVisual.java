package com.createcivilization.create_ore_deposits.foundation.renderer.visual;

import com.createcivilization.create_ore_deposits.content.CODPartialModels;
import com.createcivilization.create_ore_deposits.content.blocks.entities.custom.DepositDrillBlockEntity;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.contraptions.pulley.AbstractPulleyRenderer;
import com.simibubi.create.content.processing.burner.ScrollInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instancer;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.core.Direction;

public class DepositDrillBlockVisual extends AbstractPulleyRenderer<DepositDrillBlockEntity> {

    public DepositDrillBlockVisual(VisualizationContext dispatcher, DepositDrillBlockEntity blockEntity, float partialTick) {
        super(dispatcher, blockEntity, partialTick);
    }

    @Override
    protected Direction.Axis getShaftAxis(DepositDrillBlockEntity be) {
        return null;
    }

    @Override
    protected PartialModel getCoil() {
        return null;
    }

    @Override
    protected SpriteShiftEntry getCoilShift() {
        return null;
    }

    @Override
    protected SuperByteBuffer renderRope(DepositDrillBlockEntity be) {
        return null;
    }

    @Override
    protected SuperByteBuffer renderMagnet(DepositDrillBlockEntity be) {
        return null;
    }

    @Override
    protected float getOffset(DepositDrillBlockEntity be, float partialTicks) {
        return 0;
    }

    @Override
    protected boolean isRunning(DepositDrillBlockEntity be) {
        return false;
    }
}
