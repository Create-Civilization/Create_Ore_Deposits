package com.createcivilization.create_ore_deposits.content.items;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CODItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CreateOreDeposits.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}