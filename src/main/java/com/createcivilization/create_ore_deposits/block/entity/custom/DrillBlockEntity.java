package com.createcivilization.create_ore_deposits.block.entity.custom;

import com.createcivilization.create_ore_deposits.block.entity.custom.base.*;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DrillBlockEntity extends BaseDrillBlockEntity {

    public DrillBlockEntity(BlockEntityType<?> type, BlockPos pPos, BlockState pBlockState) {
        super(type, pPos, pBlockState);
        setEfficiency(10);
        setResourcePullSpeed(20);
    }

    //Can add some on rotation speed change events here and etc


}