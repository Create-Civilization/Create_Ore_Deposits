package com.createcivilization.create_ore_deposits.registry.fluid.entries

import net.minecraft.core.BlockPos
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FlowingFluid

class SlurryWasteBlock(fluid: FlowingFluid, properties: Properties) : LiquidBlock(fluid, properties) {

	override fun entityInside(state: BlockState, level: Level, pos: BlockPos, entity: Entity) {
		if (entity is LivingEntity && !entity.hasEffect(MobEffects.POISON)) {
			entity.addEffect(MobEffectInstance(MobEffects.POISON, 50, 2))
		}
		super.entityInside(state, level, pos, entity)
	}
}