package com.createcivilization.create_ore_deposits.util

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositsBlocks
import com.createcivilization.create_ore_deposits.registry.item.CreateOreDepositsItems

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour

import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

import java.util.function.Function
import java.util.function.Supplier

fun String.resource(): ResourceLocation = ResourceLocation.fromNamespaceAndPath(CreateOreDeposits.MOD_ID, this)

fun Item(): Item = Item(Item.Properties())

fun Block(): Block = Block(BlockBehaviour.Properties.of())

// Dearest Arctic, Orion & co. If you see these classes and wonder "what the fuck is this?", just don't even try to understand.
// it's not worth the effort, registries are a bitch.

class KotlinDeferredHolder<R, T : R>(key: ResourceKey<R>) : DeferredHolder<R, T>(key), () -> T {

	constructor(
		registryKey: ResourceKey<out Registry<R>>,
		valueName: ResourceLocation
	) : this(ResourceKey.create(registryKey, valueName))

	override fun invoke(): T = this.get()
}

class KotlinDeferredRegister<T>(
	registryKey: ResourceKey<out Registry<T>>,
	namespace: String
) : DeferredRegister<T>(registryKey, namespace) {

	constructor(
		registry: Registry<T>,
		namespace: String
	) : this(registry.key(), namespace)

	override fun <I : T> register(
		name: String,
		sup: Supplier<out I>
	): KotlinDeferredHolder<T, I> = super.register(name, sup) as KotlinDeferredHolder<T, I>

	override fun <I : T> register(
		name: String,
		func: Function<ResourceLocation, out I>
	): KotlinDeferredHolder<T, I> = super.register(name, func) as KotlinDeferredHolder<T, I>

	override fun <I : T> createHolder(
		registryKey: ResourceKey<out Registry<T>>,
		key: ResourceLocation
	): DeferredHolder<T, I> = KotlinDeferredHolder(registryKey, key)
}