package com.createcivilization.create_ore_deposits.registry.blockentities.entries

import com.createcivilization.create_ore_deposits.registry.blockentities.CreateOreDepositsBlockEntities

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class DepositDrillBlockEntity(
	pos: BlockPos,
	state: BlockState
) : BlockEntity(CreateOreDepositsBlockEntities.DEPOSIT_DRILL, pos, state)