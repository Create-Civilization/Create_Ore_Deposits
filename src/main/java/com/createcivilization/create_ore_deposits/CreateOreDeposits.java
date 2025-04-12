package com.createcivilization.create_ore_deposits;

import com.createcivilization.create_ore_deposits.block.CODBlocks;
import com.createcivilization.create_ore_deposits.block.entity.CODBlockEntities;
import com.createcivilization.create_ore_deposits.item.CODItems;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(CreateOreDeposits.MOD_ID)
public class CreateOreDeposits {

    public static final String MOD_ID = "create_ore_deposits";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateOreDeposits(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (Untitled1) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        CODItems.register(modEventBus);
        CODBlocks.register(modEventBus);
        CODBlockEntities.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("CREATE ORE DEPOSIT GO BRRRRR");
    }

}