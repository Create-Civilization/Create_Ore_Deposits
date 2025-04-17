package com.createcivilization.create_ore_deposits.block.entity;


import com.createcivilization.create_ore_deposits.block.CODBlocks;
import com.createcivilization.create_ore_deposits.block.entity.custom.DrillBlockEntity;
import com.createcivilization.create_ore_deposits.client.renderer.block.DrillBlockRenderer;
import com.createcivilization.create_ore_deposits.client.renderer.visual.DrillBlockVisual;

import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.createcivilization.create_ore_deposits.CreateOreDeposits.REGISTRATE;

public class CODBlockEntityTypes {

    public static final BlockEntityEntry<DrillBlockEntity> DEPOSIT_TESTER_BLOCK = REGISTRATE
            .blockEntity("drill_block_entity", DrillBlockEntity::new)
            .visual(() -> DrillBlockVisual::new)
            .validBlock(CODBlocks.DRILL_BLOCK::get)
            .renderer(() -> DrillBlockRenderer::new)
            .register();

    public static void register() {
    }
}
