package com.createcivilization.create_ore_deposits.registry.blockentities.entries

import com.createcivilization.create_ore_deposits.registry.blockentities.CreateOreDepositsBlockEntities

import com.simibubi.create.content.kinetics.base.KineticBlockEntity

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState


class DepositDrillBlockEntity(
	pos: BlockPos,
	state: BlockState
) : KineticBlockEntity(CreateOreDepositsBlockEntities.DEPOSIT_DRILL, pos, state) {


}