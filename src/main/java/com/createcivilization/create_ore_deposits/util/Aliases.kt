package com.createcivilization.create_ore_deposits.util

import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

import net.neoforged.bus.api.IEventBus

// Don't type your types.
typealias EventBus = IEventBus

// Lambda taking in zero parameters and returning `Item`
typealias ItemProvider = () -> Item

// Lambda taking in zero parameters and returning `Block`
typealias BlockProvider = () -> Block