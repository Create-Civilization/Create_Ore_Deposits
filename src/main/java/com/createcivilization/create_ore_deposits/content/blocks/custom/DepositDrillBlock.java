package com.createcivilization.create_ore_deposits.content.blocks.custom;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;

import java.util.function.Function;

public class DepositDrillBlock extends HorizontalKineticBlock implements IBE<DepositDrillBlockEntity> {

    public DepositDrillBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Class<DepositDrillBlockEntity> getBlockEntityClass() {
        return DepositDrillBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DepositDrillBlockEntity> getBlockEntityType() {
        return CODBlockEntityTypes.DEPOSIT_TESTER_BLOCK.get();
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == (state.getValue(HORIZONTAL_FACING)).getClockWise();
    }

    public static boolean hasPipeTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return state.getValue(HORIZONTAL_FACING)
                .getCounterClockWise() == face;
    }

    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public InteractionResult onBlockEntityUse(BlockGetter world, BlockPos pos, Function<DepositDrillBlockEntity, InteractionResult> action) {
        if(!world.getBlockEntity(pos).getLevel().isClientSide()){

            return InteractionResult.SUCCESS;
        }
        return IBE.super.onBlockEntityUse(world, pos, action);
    }


    @Override
    public Direction.Axis getRotationAxis(BlockState blockState) {
        return (blockState.getValue(HORIZONTAL_FACING)).getClockWise().getAxis();
    }
}
