package com.createcivilization.create_ore_deposits.tag;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;

import com.createcivilization.create_ore_deposits.util.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.level.block.Block;

public class CODTags {

    public static class Blocks {

        public static final TagKey<Block> ORE_DEPOSITS = registerTag("ore_deposits");

        //Remove This Please

        public static final TagKey<Block> EVERYTHING = registerTag("everything");

        public static TagKey<Block> registerTag(String name) {
            return BlockTags.create(Utils.asResource(name));
        }
    }
}