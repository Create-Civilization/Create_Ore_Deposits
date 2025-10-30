package com.createcivilization.create_ore_deposits.registry.capability

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositsBlockEntities
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockEntity

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent

@Suppress("unused")
@EventBusSubscriber(modid = CreateOreDeposits.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object CreateOreDepositsCapabilities {

	@SubscribeEvent
	fun onRegisterCapabilities(event: RegisterCapabilitiesEvent) {
		event.registerBlockEntity(
			Capabilities.ItemHandler.BLOCK,
			CreateOreDepositsBlockEntities.DEPOSIT_DRILL,
			DepositDrillBlockEntity::getItemHandler
		)

		event.registerBlockEntity(
			Capabilities.FluidHandler.BLOCK,
			CreateOreDepositsBlockEntities.DEPOSIT_DRILL,
			DepositDrillBlockEntity::getFluidHandler
		)
	}
}