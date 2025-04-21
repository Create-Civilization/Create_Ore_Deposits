package com.createcivilization.create_ore_deposits.content.materials;

import com.createcivilization.create_ore_deposits.content.kinetics.drill.DepositBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SimpleBaseDeposit extends Block {

    public static final BooleanProperty DRILLED = BooleanProperty.create("drilled");

    private static BlockState anyState;
    private final DepositBlock depositBlock;

    private BlockState setDrilled(boolean drilled) {
        return anyState.setValue(DRILLED, drilled);
    }

    public SimpleBaseDeposit(DepositBlock block) {
        super(BlockBehaviour.Properties.ofFullCopy(block.getBlock()));
        depositBlock = block;
    }

    public DepositBlock getDepositBlock() {
        return depositBlock;
    }

    public List<ItemStack> getDepositDrops(ServerLevel level, BlockPos pos, @Nullable BlockEntity blockEntity) {
        return Block.getDrops(depositBlock.getBlock().defaultBlockState(), level, pos, blockEntity);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DRILLED);
    }
}
