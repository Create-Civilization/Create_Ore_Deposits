package com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill

import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositsBlockEntities
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockEntity
import com.simibubi.create.content.kinetics.base.HorizontalAxisKineticBlock
import com.simibubi.create.foundation.block.IBE
import net.minecraft.world.level.block.entity.BlockEntityType

class DepositDrillBlock(properties: Properties) : HorizontalAxisKineticBlock(properties), IBE<DepositDrillBlockEntity> {

	override fun getBlockEntityClass(): Class<DepositDrillBlockEntity> {
		return DepositDrillBlockEntity::class.java
	}

	override fun getBlockEntityType(): BlockEntityType<out DepositDrillBlockEntity> {
		return CreateOreDepositsBlockEntities.DEPOSIT_DRILL
	}
}