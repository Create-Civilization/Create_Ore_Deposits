package com.createcivilization.create_ore_deposits.block.custom.gen;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class SimpleBaseDeposit extends Block {

    public static final BooleanProperty DRILLED = BooleanProperty.create("drilled");

    private static BlockState anyState;

    private BlockState setDrilled(boolean drilled) {
        return anyState.setValue(DRILLED, drilled);
    }

    public SimpleBaseDeposit(Properties properties) {
        super(properties);
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DRILLED);
    }
}
