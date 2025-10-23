package com.createcivilization.create_ore_deposits.util

import net.minecraft.world.item.Item

import java.util.function.Supplier

import kotlin.reflect.KProperty

fun Item(): Item = Item(Item.Properties())

operator fun Supplier<Item>.getValue(thisRef: Any?, property: KProperty<*>): Item = this.get()