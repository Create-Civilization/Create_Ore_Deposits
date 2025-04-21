package com.createcivilization.create_ore_deposits.content.kinetics.drill;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public record DepositBlock(String blockId, int min, int max) {
    public static DepositBlock fromBlock(Block block, int min, int max) {
        return new DepositBlock(BuiltInRegistries.BLOCK.getKey(block).toString(), min, max);
    }

    public String getPath() {
        return ResourceLocation.parse(blockId()).getPath();
    }

    public Block getBlock() {
        return BuiltInRegistries.BLOCK.get(ResourceLocation.parse(blockId()));
    }
}
