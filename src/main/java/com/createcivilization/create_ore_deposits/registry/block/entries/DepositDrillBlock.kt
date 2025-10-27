package com.createcivilization.create_ore_deposits.registry.block.entries

import com.createcivilization.create_ore_deposits.registry.blockentities.CreateOreDepositsBlockEntities
import com.createcivilization.create_ore_deposits.registry.blockentities.entries.DepositDrillBlockEntity
import com.simibubi.create.content.kinetics.base.HorizontalAxisKineticBlock
import com.simibubi.create.foundation.block.IBE
import net.minecraft.world.level.block.entity.BlockEntityType

class DepositDrillBlock(properties: Properties) : HorizontalAxisKineticBlock(properties), IBE<DepositDrillBlockEntity> {

	constructor() : this(Properties.of())

	override fun getBlockEntityClass(): Class<DepositDrillBlockEntity> {
		return DepositDrillBlockEntity::class.java
	}

	override fun getBlockEntityType(): BlockEntityType<out DepositDrillBlockEntity> {
		return CreateOreDepositsBlockEntities.DEPOSIT_DRILL
	}
}