package com.createcivilization.create_ore_deposits.client.renderer.block;

import com.createcivilization.create_ore_deposits.block.entity.custom.DrillBlockEntity;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.pulley.AbstractPulleyRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class DrillBlockRenderer extends AbstractPulleyRenderer<DrillBlockEntity> {

    public DrillBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context, AllPartialModels.ROPE_HALF, AllPartialModels.ROPE_HALF_MAGNET);
    }

    @Override
    protected Direction.Axis getShaftAxis(DrillBlockEntity be) {
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
    protected SuperByteBuffer renderRope(DrillBlockEntity be) {
        return null;
    }

    @Override
    protected SuperByteBuffer renderMagnet(DrillBlockEntity be) {
        return null;
    }

    @Override
    protected float getOffset(DrillBlockEntity be, float partialTicks) {
        return 0;
    }

    @Override
    protected boolean isRunning(DrillBlockEntity be) {
        return false;
    }
}
