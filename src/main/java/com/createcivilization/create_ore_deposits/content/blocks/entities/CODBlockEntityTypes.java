package com.createcivilization.create_ore_deposits.content.blocks.entities;

import com.createcivilization.create_ore_deposits.content.blocks.CODBlocks;
import com.createcivilization.create_ore_deposits.content.blocks.entities.custom.DepositDrillBlockEntity;
import com.createcivilization.create_ore_deposits.foundation.renderer.block.DepositDrillBlockRenderer;
import com.simibubi.create.content.kinetics.drill.DrillBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.createcivilization.create_ore_deposits.CreateOreDeposits.REGISTRATE;

public class CODBlockEntityTypes {

    public static final BlockEntityEntry<DepositDrillBlockEntity> DEPOSIT_TESTER_BLOCK = REGISTRATE
            .blockEntity("drill_block_entity", DrillBlockEntity::new)
            .visual(() -> DrillBlockVisual::new)
            .validBlock(CODBlocks.DEPOSIT_DRILL_BLOCK::get)
            .renderer(() -> DepositDrillBlockRenderer::new)
            .register();

    public static void register() {
    }
}
