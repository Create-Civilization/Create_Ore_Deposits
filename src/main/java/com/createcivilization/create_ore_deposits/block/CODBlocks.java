package com.createcivilization.create_ore_deposits.block;

import com.createcivilization.create_ore_deposits.block.custom.DrillBlock;
import com.createcivilization.create_ore_deposits.block.custom.gen.SimpleBaseDeposit;
import com.createcivilization.create_ore_deposits.tag.CODTags;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.List;

import static com.createcivilization.create_ore_deposits.CreateOreDeposits.REGISTRATE;

public class CODBlocks {

    public static final BlockEntry<SimpleBaseDeposit> IRON_ORE_DEPOSIT = REGISTRATE
            .block("iron_ore_deposit", properties -> new SimpleBaseDeposit(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE),
                    List.of(new ItemStack(Items.RAW_IRON)),
                    1,400, 1.0f
            ))
            .simpleItem()
            .tag(CODTags.Blocks.ORE_DEPOSITS)
            .register();

    public static final BlockEntry<DrillBlock> DRILL_BLOCK = REGISTRATE
            .block("drill_block", properties -> new DrillBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()
            ))
            .simpleItem()
            .register();

}
