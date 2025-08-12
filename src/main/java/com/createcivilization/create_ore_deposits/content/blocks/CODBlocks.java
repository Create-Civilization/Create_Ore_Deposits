package com.createcivilization.create_ore_deposits.content.blocks;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static com.createcivilization.create_ore_deposits.CreateOreDeposits.REGISTRATE;

public class CODBlocks {

    public static final BlockEntry<DepositDrillBlock> DEPOSIT_DRILL_BLOCK = REGISTRATE
            .block("deposit_drill_block", properties -> new DepositDrillBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()
            ))
            .simpleItem()
            .register();


}
