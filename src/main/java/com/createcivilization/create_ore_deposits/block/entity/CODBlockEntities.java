package com.createcivilization.create_ore_deposits.block.entity;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import com.createcivilization.create_ore_deposits.block.CODBlocks;
import com.createcivilization.create_ore_deposits.block.entity.custom.*;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class CODBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, CreateOreDeposits.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<DrillBlockEntity>> DEPOSIT_TESTER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("deposit_tester_block_entity", () ->
                    BlockEntityType.Builder.of(
                            DrillBlockEntity::new,
                            CODBlocks.DRILL_BLOCK.get()
                    ).build(null)
            );

    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TestBlockEntity>> TEST_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("test_block_entity", () ->
                    BlockEntityType.Builder.of(
                            TestBlockEntity::new,
                            CODBlocks.TEST_BLOCK.get()
                    ).build(null)
            );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}