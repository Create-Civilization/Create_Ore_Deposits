package com.createcivilization.create_ore_deposits.block.custom.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;

public class SimpleBaseDeposit extends Block {

    private List<ItemStack> drops;
    private int maxCount;
    private int minCount;
    private float hardness;
    public SimpleBaseDeposit(Properties properties, List<ItemStack> drops, int minCount, int maxCount, float hardness) {
        super(properties);
        this.drops = drops;
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.hardness = hardness;
    }

    public List<ItemStack> getDrops() {
        return drops;
    }

    public void setDrops(List<ItemStack> drops) {
        this.drops = drops;
    }

    public float getHardness(){
        return hardness;
    }

    public int getMaxCount() {
        return maxCount;
    }
    public int getMinCount() {
        return minCount;
    }
}
