package com.createcivilization.create_ore_deposits.registry.block

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockEntity
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockVisual
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockRenderer
import com.createcivilization.create_ore_deposits.util.KotlinDeferredRegister

import com.tterrag.registrate.util.entry.BlockEntityEntry
import com.tterrag.registrate.util.nullness.NonNullFunction

import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer

import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.entity.BlockEntityType

import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object CreateOreDepositsBlockEntities {

	@JvmField
	internal val BLOCK_ENTITY_PROVIDER: KotlinDeferredRegister<BlockEntityType<*>> = KotlinDeferredRegister(
        Registries.BLOCK_ENTITY_TYPE,
        CreateOreDeposits.MOD_ID
    )

	private val _DEPOSIT_DRILL: BlockEntityEntry<DepositDrillBlockEntity> = CreateOreDeposits.REGISTRATE
		.blockEntity("deposit_drill_entity", ::DepositDrillBlockEntity)
		.visual { SimpleBlockEntityVisualizer.Factory(::DepositDrillBlockVisual) }
		.validBlock(CreateOreDepositsBlocks._DRILL_BLOCK)
		.renderer { NonNullFunction(::DepositDrillBlockRenderer) }
		.register()
	val DEPOSIT_DRILL: BlockEntityType<DepositDrillBlockEntity> get() = _DEPOSIT_DRILL.get()

	init { BLOCK_ENTITY_PROVIDER.register(MOD_BUS) }
}