package com.createcivilization.create_ore_deposits.registry.fluid.entries

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FlowingFluid

class SlagFluidBlock(fluid: FlowingFluid, properties: BlockBehaviour.Properties) : LiquidBlock(fluid, properties) {
	override fun entityInside(state: BlockState, level: Level, pos: BlockPos, entity: Entity) {
		if (entity is LivingEntity) {
			entity.hurt(level.damageSources().inFire(), 4.0f);
			entity.igniteForSeconds(3f);
		}
		super.entityInside(state, level, pos, entity)
	}
}