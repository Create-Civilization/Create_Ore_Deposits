package com.createcivilization.create_ore_deposits.foundation.datagen;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CODBlockTagGenerator extends BlockTagsProvider {

    public CODBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CreateOreDeposits.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {}

    @Override
    public String getName() {
        return "Block Tags";
    }
}
