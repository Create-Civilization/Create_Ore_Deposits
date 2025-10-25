package com.createcivilization.create_ore_deposits.registry.blockentities.entries

import com.createcivilization.create_ore_deposits.registry.blockentities.CreateOreDepositsBlockEntities
import com.simibubi.create.content.contraptions.AssemblyException
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class DepositDrillBlockEntity(
    pos: BlockPos,
    state: BlockState
) : MechanicalPistonBlockEntity(CreateOreDepositsBlockEntities.DEPOSIT_DRILL, pos, state) {

	var lastExceptionAccess: AssemblyException? by this::lastException
}