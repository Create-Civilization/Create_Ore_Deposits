package com.createcivilization.create_ore_deposits.block;

import com.createcivilization.create_ore_deposits.block.custom.DrillBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;


import static com.createcivilization.create_ore_deposits.CreateOreDeposits.REGISTRATE;

public class CODBlocks {

    public static final BlockEntry<DrillBlock> DRILL_BLOCK = REGISTRATE
            .block("drill_block", properties -> new DrillBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()
            ))
            .simpleItem()
            .register();


}
