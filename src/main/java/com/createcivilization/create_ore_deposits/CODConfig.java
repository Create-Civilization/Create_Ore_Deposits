package com.createcivilization.create_ore_deposits;

import com.createcivilization.create_ore_deposits.content.kinetics.drill.DepositBlock;
import com.createcivilization.create_ore_deposits.content.materials.SimpleBaseDeposit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.createcivilization.create_ore_deposits.CreateOreDeposits.REGISTRATE;

public class CODConfig {
    private static final Path CONFIG_FILE = FMLPaths.CONFIGDIR.get().resolve("create_ore_deposits.json");
    public static final Map<ResourceLocation, DepositBlock> REGISTERED_DEPOSITS = new HashMap<>();

    public static void loadConfig() {
        CreateOreDeposits.LOGGER.info("Parsing config..");
        List<DepositBlock> toAdd = new ArrayList<>();
        if (Files.exists(CONFIG_FILE)) {
            try {
                toAdd.addAll(new Gson().fromJson(Files.readString(CONFIG_FILE), new TypeToken<List<DepositBlock>>() {}.getType()));
            } catch (IOException e) {
                CreateOreDeposits.LOGGER.error("Failed to read default config file.", e);
                throw new RuntimeException(e);
            }
        } else {
            try {
                CreateOreDeposits.LOGGER.info("No config found, adding defaults..");

                toAdd.add(DepositBlock.fromBlock(Blocks.IRON_ORE, 20, 40));
                toAdd.add(DepositBlock.fromBlock(Blocks.GOLD_ORE, 20, 40));
                toAdd.add(DepositBlock.fromBlock(Blocks.DIAMOND_ORE, 20, 40));
                toAdd.add(new DepositBlock("create:zinc_ore", 20, 40));

                Files.writeString(CONFIG_FILE, new GsonBuilder().setPrettyPrinting().create().toJson(toAdd));
                CreateOreDeposits.LOGGER.error("Created default config file.");
            } catch (IOException e) {
                CreateOreDeposits.LOGGER.error("Failed to create default config file.", e);
                throw new RuntimeException(e);
            }
        }
        for (DepositBlock depositBlock : toAdd) {
            String path = depositBlock.getPath() + "_deposit";
            REGISTRATE.block( path, properties -> new SimpleBaseDeposit(depositBlock))
                    .tag(CODTags.Blocks.ORE_DEPOSITS)
                    .simpleItem()
                    .register();
            REGISTERED_DEPOSITS.put(ResourceLocation.fromNamespaceAndPath(CreateOreDeposits.MOD_ID, path), depositBlock);
        }
        CreateOreDeposits.LOGGER.info("Config parsed.");
    }
}
