package com.createcivilization.create_ore_deposits.registry.blockentities

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.registry.blockentities.entries.DepositDrillBlockEntity
import com.createcivilization.create_ore_deposits.util.KotlinDeferredRegister

import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.entity.BlockEntityType

object CreateOreDepositBlockEntities {

	internal val BLOCK_ENTITY_PROVIDER: KotlinDeferredRegister<BlockEntityType<*>> = KotlinDeferredRegister(
		Registries.BLOCK_ENTITY_TYPE,
		CreateOreDeposits.MOD_ID
	)

	private val _DEPOSIT_DRILL: () -> BlockEntityType<DepositDrillBlockEntity> = BLOCK_ENTITY_PROVIDER.register("deposit_drill") { ->
		BlockEntityType.Builder.of(
			::DepositDrillBlockEntity
		).build(null)
	}
	val DEPOSIT_DRILL: BlockEntityType<DepositDrillBlockEntity> get() = _DEPOSIT_DRILL()
}