package com.createcivilization.create_ore_deposits.block.custom.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;

public class SimpleBaseDeposit extends Block {

    public static final BooleanProperty DRILLED = BooleanProperty.create("drilled");

    private static BlockState anyState;

    private BlockState setDrilled(boolean drilled) {
        return anyState.setValue(DRILLED, drilled);
    }

    public SimpleBaseDeposit(Properties properties) {
        super(properties);
        this.registerDefaultState(getStateDefinition().any()
               .setValue(DRILLED, false));
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DRILLED);
    }
}
