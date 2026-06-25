package com.createcivilization.create_ore_deposits.registry.tab

import com.createcivilization.create_ore_deposits.CreateOreDeposits.MOD_ID
import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositsBlocks

import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTabs
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

data object CreateOreDepositsTabs {

	private val REGISTER: DeferredRegister<CreativeModeTab> = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID)

	@JvmField
	val BASE_CREATIVE_TAB: DeferredHolder<CreativeModeTab, CreativeModeTab> = REGISTER.register("base") { _ ->
		CreativeModeTab.builder()
			.title(Component.translatable("itemGroup.${MOD_ID}.base"))
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.icon(CreateOreDepositsBlocks.DRILL_BLOCK::asStack)
			.build()
	}

	fun register(modEventBus: IEventBus) = REGISTER.register(modEventBus)
}