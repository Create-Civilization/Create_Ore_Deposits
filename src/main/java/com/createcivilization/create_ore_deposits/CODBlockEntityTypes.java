package com.createcivilization.create_ore_deposits;


import com.createcivilization.create_ore_deposits.content.kinetics.drill.DrillBlockEntity;
import com.createcivilization.create_ore_deposits.foundation.renderer.block.DrillBlockRenderer;
import com.createcivilization.create_ore_deposits.foundation.renderer.visual.DrillBlockVisual;

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
