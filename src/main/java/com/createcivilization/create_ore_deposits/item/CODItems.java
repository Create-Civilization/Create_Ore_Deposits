package com.createcivilization.create_ore_deposits.item;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CODItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CreateOreDeposits.MOD_ID);

    public static final DeferredItem<Item> TEST_ITEM = ITEMS.register("test_item", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}