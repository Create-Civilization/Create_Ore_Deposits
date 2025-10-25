package com.createcivilization.create_ore_deposits.registry.block.entries

import com.createcivilization.create_ore_deposits.registry.blockentities.entries.DepositDrillBlockEntity

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class DepositDrillBlock(properties: Properties) : Block(properties), EntityBlock {

	constructor() : this(Properties.of())

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = DepositDrillBlockEntity(pos, state)
}