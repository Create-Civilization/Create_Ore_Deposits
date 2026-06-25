package com.createcivilization.create_ore_deposits.util

import com.createcivilization.create_ore_deposits.CreateOreDeposits

import net.minecraft.resources.ResourceLocation

fun String.asResource(): ResourceLocation = ResourceLocation.fromNamespaceAndPath(CreateOreDeposits.MOD_ID, this)