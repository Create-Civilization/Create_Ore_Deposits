package com.createcivilization.create_ore_deposits.content.blocks.entities.custom;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


public class DepositDrillBlockEntity extends KineticBlockEntity {


    protected LerpedFloat drillOffset;


    public DepositDrillBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }


    public float getInterpolatedOffset(float partialTicks) {
        return Math.max(drillOffset.getValue(partialTicks), 3 / 16f);
    }
}
