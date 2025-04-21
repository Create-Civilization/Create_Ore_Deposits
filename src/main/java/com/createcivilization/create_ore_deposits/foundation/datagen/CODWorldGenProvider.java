package com.createcivilization.create_ore_deposits.foundation.datagen;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import com.createcivilization.create_ore_deposits.content.worldgen.CODBiomeModifiers;
import com.createcivilization.create_ore_deposits.content.worldgen.CODConfiguredFeatures;
import com.createcivilization.create_ore_deposits.content.worldgen.CODPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;


import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CODWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, CODConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, CODPlacedFeatures::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, CODBiomeModifiers::bootstrap);


    public CODWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(CreateOreDeposits.MOD_ID));
    }
}
