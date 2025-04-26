package com.createcivilization.create_ore_deposits.content.kinetics.drill;

import com.createcivilization.create_ore_deposits.CODBlockEntityTypes;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Function;

@SuppressWarnings("NullableProblems")
public class DrillBlock extends HorizontalKineticBlock implements IBE<DrillBlockEntity> {

    public DrillBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Class<DrillBlockEntity> getBlockEntityClass() {
        return DrillBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DrillBlockEntity> getBlockEntityType() {
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
    public InteractionResult onBlockEntityUse(BlockGetter world, BlockPos pos, Function<DrillBlockEntity, InteractionResult> action) {
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