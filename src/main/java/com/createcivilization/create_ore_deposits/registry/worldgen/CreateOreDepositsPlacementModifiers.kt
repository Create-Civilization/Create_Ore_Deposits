package com.createcivilization.create_ore_deposits.registry.worldgen

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.placement.PlacementModifierType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

data object CreateOreDepositsPlacementModifiers {

	private val REGISTER = DeferredRegister.create(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, CreateOreDeposits.MOD_ID)

	@JvmField
	val ORE_VEIN: DeferredHolder<PlacementModifierType<*>, PlacementModifierType<OreVeinPlacementModifier>> =
		REGISTER.register("ore_vein", Supplier {
			PlacementModifierType { OreVeinPlacementModifier.CODEC }
		})

	fun register(modEventBus: IEventBus) {
		REGISTER.register(modEventBus)
	}
}