package com.createcivilization.create_ore_deposits;

import com.createcivilization.create_ore_deposits.block.CODBlocks;
import com.createcivilization.create_ore_deposits.block.entity.CODBlockEntities;
import com.createcivilization.create_ore_deposits.item.CODItems;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(CreateOreDeposits.MOD_ID)
public class CreateOreDeposits {

    public static final String MOD_ID = "create_ore_deposits";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateOreDeposits(IEventBus modEventBus, ModContainer container) {

        CODItems.register(modEventBus);
        CODBlocks.register(modEventBus);
        CODBlockEntities.register(modEventBus);

    }
}