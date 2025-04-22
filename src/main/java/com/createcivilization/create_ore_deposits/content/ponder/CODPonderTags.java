package com.createcivilization.create_ore_deposits.content.ponder;

import com.createcivilization.create_ore_deposits.CODBlocks;
import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import com.simibubi.create.AllBlocks;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class CODPonderTags {
    public static final ResourceLocation TEST = CreateOreDeposits.asResource("test");
    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        helper.registerTag(TEST)
                .addToIndex()
                .item(CODBlocks.DRILL_BLOCK.get(), true, true)
                .title("Drill Things")
                .description("Drill")
                .register();
    }
}
