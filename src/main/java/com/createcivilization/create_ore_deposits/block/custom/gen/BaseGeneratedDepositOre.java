package com.createcivilization.create_ore_deposits.block.custom.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;
import java.util.Random;

public class BaseGeneratedDepositOre extends Block {

    private final Block ORE;
    Random random = new Random();
    public static final IntegerProperty RESOURCE_VALUE = IntegerProperty.create("resource_value", 0, 300);

    private static BlockState anyState;

    private BlockState setResourceLevel(int amount) {
        return anyState.setValue(RESOURCE_VALUE, amount);
    }

    public BaseGeneratedDepositOre(Block ore) {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE));
        this.ORE = ore;
        anyState = this.stateDefinition.any();
        this.registerDefaultState(setResourceLevel(random.nextInt(150,301)));
    }

    public ItemStack extractItemStack(ServerLevel serverLevel, int amount) {
        return this.getOreDrop(serverLevel, amount);
    }

    public ItemStack getOreDrop(ServerLevel serverLevel, int amount) {

        LootParams.Builder lootParamsBuilder = new LootParams.Builder(serverLevel)
                .withParameter(LootContextParams.ORIGIN, BlockPos.ZERO.getCenter())
                .withParameter(LootContextParams.TOOL, new ItemStack(Items.DIAMOND_PICKAXE))
                .withParameter(LootContextParams.BLOCK_STATE, anyState);

        List<ItemStack> oreDrops = ORE.defaultBlockState().getDrops(lootParamsBuilder);

        return oreDrops.isEmpty() ? new ItemStack(Items.AIR) : new ItemStack(oreDrops.get(0).getItem(), amount);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(RESOURCE_VALUE);
    }
}
