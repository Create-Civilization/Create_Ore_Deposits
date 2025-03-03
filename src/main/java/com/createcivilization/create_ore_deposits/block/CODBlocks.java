package com.createcivilization.create_ore_deposits.block;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import com.createcivilization.create_ore_deposits.block.custom.*;
import com.createcivilization.create_ore_deposits.block.custom.gen.BaseGeneratedDepositOre;
import com.createcivilization.create_ore_deposits.item.CODItems;

import com.simibubi.create.Create;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;


import java.util.function.Supplier;

public class CODBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CreateOreDeposits.MOD_ID);

    public static DeferredBlock<Block> DRILL_BLOCK = registerBlock(
            "deposit_tester_block", () -> new DrillBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK))
    ); //TODO: Figure out a name for the drill

    public static DeferredBlock<Block> TEST_BLOCK = registerBlock(
            "test_block", () -> new TestBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK))
    );

    public static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        CODItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {

        // TODO:: DO NOT TOUCH THIS, for now its debug but I shall, and I repeat **I**, Orion, shall make a better system for this, DO NOT TOUCH!!!! <3
        for (Block ore : BuiltInRegistries.BLOCK.stream().filter(block -> block.getName().toString().toLowerCase().contains("ore")).toList()) {
            registerBlock(
                    BuiltInRegistries.BLOCK.getKey(ore).getPath(),
                    () -> new BaseGeneratedDepositOre(ore)
            );
        }
        BLOCKS.register(eventBus);
    }
}