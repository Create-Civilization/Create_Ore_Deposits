package com.createcivilization.create_ore_deposits.worldgen;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class CODConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_IRON_DEPOSIT_KEY = registerKey("iron_ore_deposit_block");

    //Some fucked shit. This is gonna need to be removed. I am just horsing around


    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
//        RuleTest stoneReplaceabeles = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
//        RuleTest deepslateReplaceabeles = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
//        RuleTest fuckingEverything = new TagMatchTest(CODTags.Blocks.EVERYTHING);
//
//        List<OreConfiguration.TargetBlockState> overworldIronOreDeposit = List.of(OreConfiguration.target(stoneReplaceabeles,
//                CODBlocks.IRON_ORE_DEPOSIT_BLOCK.get().defaultBlockState()),
//                OreConfiguration.target(deepslateReplaceabeles, CODBlocks.IRON_ORE_DEPOSIT_BLOCK.get().defaultBlockState()));
//
//
//        register(context, OVERWORLD_IRON_DEPOSIT_KEY, Feature.ORE, new OreConfiguration(overworldIronOreDeposit, 64));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(CreateOreDeposits.MOD_ID, name));
    }


    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
