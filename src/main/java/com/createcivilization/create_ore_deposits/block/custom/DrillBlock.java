package com.createcivilization.create_ore_deposits.block.custom;

import com.createcivilization.create_ore_deposits.block.entity.CODBlockEntities;
import com.createcivilization.create_ore_deposits.block.entity.custom.DrillBlockEntity;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;

import java.util.function.Function;

@SuppressWarnings("NullableProblems")
public class DrillBlock extends DirectionalKineticBlock implements IBE<DrillBlockEntity> {

    public DrillBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Class<DrillBlockEntity> getBlockEntityClass() {
        return DrillBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DrillBlockEntity> getBlockEntityType() {
        return CODBlockEntities.DEPOSIT_TESTER_BLOCK_ENTITY.get();
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == (state.getValue(FACING)).getOpposite();
    }

    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public InteractionResult onBlockEntityUse(BlockGetter world, BlockPos pos, Function<DrillBlockEntity, InteractionResult> action) {
        if(!world.getBlockEntity(pos).getLevel().isClientSide()){

            return InteractionResult.SUCCESS;
        }
        return IBE.super.onBlockEntityUse(world, pos, action);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState blockState) {
        return (blockState.getValue(FACING)).getAxis();
    }
}