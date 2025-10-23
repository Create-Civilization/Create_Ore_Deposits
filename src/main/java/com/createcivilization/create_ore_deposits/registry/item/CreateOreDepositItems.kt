package com.createcivilization.create_ore_deposits.registry.item

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.util.Item
import com.createcivilization.create_ore_deposits.util.Registries
import com.createcivilization.create_ore_deposits.util.getValue

import net.minecraft.world.item.Item

import net.neoforged.neoforge.registries.DeferredRegister

import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

import java.util.function.Supplier

object CreateOreDepositItems {

	private val DEFERRED_REGISTER: DeferredRegister<Item> = DeferredRegister.create(
		Registries.ITEM,
		CreateOreDeposits.MOD_ID
	)

	// Won't let me use `() -> Item` because... it just won't >.>
	private val _TEST_ITEM: Supplier<Item> = DEFERRED_REGISTER.register("test", ::Item)

	val TEST_ITEM: Item by _TEST_ITEM

	init { DEFERRED_REGISTER.register(MOD_BUS) }
}