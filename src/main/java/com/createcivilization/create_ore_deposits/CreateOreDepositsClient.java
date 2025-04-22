package com.createcivilization.create_ore_deposits;

import com.createcivilization.create_ore_deposits.content.ponder.CODPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = CreateOreDeposits.MOD_ID, dist = Dist.CLIENT)
public class CreateOreDepositsClient {

    public CreateOreDepositsClient(net.neoforged.bus.api.IEventBus modEventBus) {
        onCtorClient(modEventBus);
    }

    public static void onCtorClient(net.neoforged.bus.api.IEventBus modEventBus) {
        net.neoforged.bus.api.IEventBus neoEventBus = NeoForge.EVENT_BUS;

        modEventBus.addListener(CreateOreDepositsClient::clientInit);
    }

    public static void clientInit(final FMLClientSetupEvent event) {

        CODPartialModels.init();
        PonderIndex.addPlugin(new CODPonderPlugin());
    }


}
