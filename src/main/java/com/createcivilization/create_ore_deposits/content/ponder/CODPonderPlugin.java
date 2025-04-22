package com.createcivilization.create_ore_deposits.content.ponder;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class CODPonderPlugin implements PonderPlugin {
    @Override
    public String getModId() {
        return CreateOreDeposits.MOD_ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        CODPonderScenes.register(helper);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        CODPonderTags.register(helper);
    }
}
