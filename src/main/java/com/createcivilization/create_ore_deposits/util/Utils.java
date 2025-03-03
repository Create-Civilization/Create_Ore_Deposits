package com.createcivilization.create_ore_deposits.util;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import net.minecraft.resources.ResourceLocation;

public class Utils
{
    public static ResourceLocation asResource(String pPath)
    {
        return ResourceLocation.fromNamespaceAndPath(CreateOreDeposits.MOD_ID,pPath);
    }
}
