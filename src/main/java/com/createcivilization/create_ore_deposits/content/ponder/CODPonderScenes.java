package com.createcivilization.create_ore_deposits.content.ponder;

import com.createcivilization.create_ore_deposits.CODBlocks;
import com.createcivilization.create_ore_deposits.content.ponder.scene.DrillScene;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.data.loading.DatagenModLoader;

public class CODPonderScenes {
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(CODBlocks.DRILL_BLOCK)
                .addStoryBoard("drill/basic", DrillScene::basic, CODPonderTags.TEST);

    }
}
