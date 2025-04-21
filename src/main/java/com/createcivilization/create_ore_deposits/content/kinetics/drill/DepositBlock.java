package com.createcivilization.create_ore_deposits.content.kinetics.drill;

import com.createcivilization.create_ore_deposits.CODConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public record DepositBlock(String blockId, int min, int max) {
    public static DepositBlock fromBlock(Block block, int min, int max) {
        return new DepositBlock(BuiltInRegistries.BLOCK.getKey(block).toString(), min, max);
    }

    public static DepositBlock fromString(String string) {
        return fromResourceLocation(ResourceLocation.parse(string));
    }

    public static DepositBlock fromResourceLocation(ResourceLocation resourceLocation) {
        return CODConfig.REGISTERED_DEPOSITS.get(resourceLocation);
    }

    public String getPath() {
        return ResourceLocation.parse(blockId()).getPath();
    }

    public Block getBlock() {
        return BuiltInRegistries.BLOCK.get(ResourceLocation.parse(blockId()));
    }
}
