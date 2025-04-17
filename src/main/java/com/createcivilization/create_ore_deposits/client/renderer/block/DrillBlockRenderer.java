package com.createcivilization.create_ore_deposits.client.renderer.block;

import com.createcivilization.create_ore_deposits.CODPartialModels;
import com.createcivilization.create_ore_deposits.block.entity.custom.DrillBlockEntity;
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

public class DrillBlockRenderer extends AbstractPulleyRenderer<DrillBlockEntity> {

    public DrillBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context, CODPartialModels.DRILL_HOSE_HALF, CODPartialModels.DRILL_HOSE_DRILL);
    }

    @Override
    protected Direction.Axis getShaftAxis(DrillBlockEntity be) {
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
    protected SuperByteBuffer renderRope(DrillBlockEntity be) {
        return CachedBuffers.partial(CODPartialModels.DRILL_HOSE, be.getBlockState());
    }

    @Override
    protected SuperByteBuffer renderMagnet(DrillBlockEntity be) {
        return CachedBuffers.partial(CODPartialModels.DRILL_HEAD, be.getBlockState());
    }

    @Override
    protected float getOffset(DrillBlockEntity be, float partialTicks) {
        return be.getInterpolatedOffset(partialTicks);
    }

    @Override
    protected boolean isRunning(DrillBlockEntity be) {
        return true;
    }
}
