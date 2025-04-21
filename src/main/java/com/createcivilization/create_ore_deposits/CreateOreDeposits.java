package com.createcivilization.create_ore_deposits;

import com.createcivilization.create_ore_deposits.content.item.CODItems;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
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

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    public CreateOreDeposits(IEventBus modEventBus, ModContainer modContainer) {
        REGISTRATE.registerEventListeners(modEventBus);

        CODItems.register(modEventBus);
        CODBlockEntityTypes.register();



        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
        CODConfig.loadConfig();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(CreateOreDeposits.MOD_ID, path);
    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("CREATE ORE DEPOSIT GO BRRRRR");
    }



}