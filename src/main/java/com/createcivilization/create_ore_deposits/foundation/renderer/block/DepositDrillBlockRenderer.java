package com.createcivilization.create_ore_deposits.foundation.renderer.block;

import com.createcivilization.create_ore_deposits.content.CODPartialModels;
import com.createcivilization.create_ore_deposits.content.blocks.entities.custom.DepositDrillBlockEntity;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.contraptions.pulley.AbstractPulleyRenderer;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlock;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class DepositDrillBlockRenderer extends AbstractPulleyRenderer<DepositDrillBlockEntity> {

    public DepositDrillBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context, AllPartialModels.ROPE_HALF, AllPartialModels.ROPE_HALF_MAGNET);
    }

    @Override
    protected Direction.Axis getShaftAxis(DepositDrillBlockEntity be) {
        return be.getBlockState()
                .getValue(HosePulleyBlock.HORIZONTAL_FACING)
                .getClockWise()
                .getAxis();
    }

    @Override
    protected PartialModel getCoil() {
        return AllPartialModels.HOSE_COIL;
    }

    @Override
    protected SpriteShiftEntry getCoilShift() {
        return AllSpriteShifts.HOSE_PULLEY_COIL;
    }

    @Override
    protected SuperByteBuffer renderRope(DepositDrillBlockEntity be) {
        return CachedBuffers.partial(CODPartialModels.HOSE, be.getBlockState());
    }

    @Override
    protected SuperByteBuffer renderMagnet(DepositDrillBlockEntity be) {
        return CachedBuffers.partial(CODPartialModels.DRILL_MAGNET, be.getBlockState());
    }

    @Override
    protected float getOffset(DepositDrillBlockEntity be, float partialTicks) {
        return be.getInterpolatedOffset(partialTicks);
    }

    @Override
    protected boolean isRunning(DepositDrillBlockEntity be) {
        return true;
    }
}
