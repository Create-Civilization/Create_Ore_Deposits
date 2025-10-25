package com.createcivilization.create_ore_deposits.registry.blockentities

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositsBlocks
import com.createcivilization.create_ore_deposits.registry.blockentities.entries.DepositDrillBlockEntity
import com.createcivilization.create_ore_deposits.util.KotlinDeferredRegister

import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.entity.BlockEntityType

object CreateOreDepositsBlockEntities {

	@JvmField
	internal val BLOCK_ENTITY_PROVIDER: KotlinDeferredRegister<BlockEntityType<*>> = KotlinDeferredRegister(
		Registries.BLOCK_ENTITY_TYPE,
		CreateOreDeposits.MOD_ID
	)

	private val _DEPOSIT_DRILL: () -> BlockEntityType<DepositDrillBlockEntity> = BLOCK_ENTITY_PROVIDER.register("deposit_drill") { ->
		BlockEntityType.Builder.of(
			::DepositDrillBlockEntity,
			CreateOreDepositsBlocks.DEPOSIT_DRILL
		).build(null)
	}
	val DEPOSIT_DRILL: BlockEntityType<DepositDrillBlockEntity> get() = _DEPOSIT_DRILL()
}