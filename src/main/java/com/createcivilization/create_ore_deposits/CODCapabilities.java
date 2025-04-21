package com.createcivilization.create_ore_deposits;

import com.createcivilization.create_ore_deposits.content.kinetics.drill.BaseDrillBlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = CreateOreDeposits.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CODCapabilities {

    @SubscribeEvent
    private static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                CODBlockEntityTypes.DEPOSIT_TESTER_BLOCK.get(),
                BaseDrillBlockEntity::getItemHandler
        );
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                CODBlockEntityTypes.DEPOSIT_TESTER_BLOCK.get(),
                BaseDrillBlockEntity::getFluidHandler
        );
    }
}
