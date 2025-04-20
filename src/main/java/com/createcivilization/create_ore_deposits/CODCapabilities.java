package com.createcivilization.create_ore_deposits;

import com.createcivilization.create_ore_deposits.block.entity.CODBlockEntityTypes;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class CODCapabilities {
    private void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                CODBlockEntityTypes.DEPOSIT_TESTER_BLOCK.get(),
                (blockEntity, direction) -> blockEntity.getItemHandler()
        );
    }
}
