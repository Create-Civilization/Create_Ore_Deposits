package com.createcivilization.create_ore_deposits.util

import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour

import java.util.function.Supplier

import kotlin.reflect.KProperty

fun Item(): Item = Item(Item.Properties())

operator fun Supplier<Item>.getValue(thisRef: Any?, property: KProperty<*>): Item = this.get()

fun Block(): Block = Block(BlockBehaviour.Properties.of())

operator fun Supplier<Block>.getValue(thisRef: Any?, property: KProperty<*>): Block = this.get()