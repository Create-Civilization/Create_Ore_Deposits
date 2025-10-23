package com.createcivilization.create_ore_deposits.registry.item

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.util.Item

import net.minecraft.world.item.Item
import net.minecraft.core.registries.BuiltInRegistries as Registries

import net.neoforged.neoforge.registries.DeferredRegister

import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

import java.util.function.Supplier

object CreateOreDepositItems {

	val DEFERRED_REGISTER: DeferredRegister<Item> = DeferredRegister.create(
		Registries.ITEM,
		CreateOreDeposits.MOD_ID
	)

	// Won't let me use `() -> Item` because... it just won't >.>
	val TEST_ITEM: Supplier<Item> = DEFERRED_REGISTER.register("test", ::Item)

	init { DEFERRED_REGISTER.register(MOD_BUS) }
}