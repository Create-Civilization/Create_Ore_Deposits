package com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill

import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositsBlockEntities
import com.createcivilization.create_ore_deposits.registry.tag.CreateOreDepositsTags
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock
import com.simibubi.create.foundation.block.IBE
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.items.IItemHandler
import java.util.function.Function

class DepositDrillBlock(properties: Properties) : HorizontalKineticBlock(properties), IBE<DepositDrillBlockEntity> {

	override fun getBlockEntityClass(): Class<DepositDrillBlockEntity> = DepositDrillBlockEntity::class.java

	override fun getBlockEntityType(): BlockEntityType<out DepositDrillBlockEntity> =
		CreateOreDepositsBlockEntities.DEPOSIT_DRILL.get()

	override fun getRenderShape(pState: BlockState): RenderShape = RenderShape.MODEL

	override fun hasShaftTowards(world: LevelReader, pos: BlockPos, state: BlockState, face: Direction): Boolean =
		face == state.getValue(HORIZONTAL_FACING).clockWise

	override fun getRotationAxis(state: BlockState): Direction.Axis =
		state.getValue(HORIZONTAL_FACING).clockWise.axis

	override fun onBlockEntityUse(
		world: BlockGetter,
		pos: BlockPos,
		action: Function<DepositDrillBlockEntity, InteractionResult>
	): InteractionResult =
		if (!world.getBlockEntity(pos)!!.level!!.isClientSide) InteractionResult.SUCCESS
		else super.onBlockEntityUse(world, pos, action)

	override fun useItemOn(
		stack: ItemStack,
		state: BlockState,
		level: Level,
		pos: BlockPos,
		player: Player,
		hand: InteractionHand,
		hitResult: BlockHitResult
	): ItemInteractionResult {

		if (level.isClientSide)
			return ItemInteractionResult.SUCCESS

		withBlockEntityDo(level, pos, { be ->
			val drillTipHandler: IItemHandler = be.getDrillTipItemHandler()
			if (player.mainHandItem.isEmpty) {
				if (drillTipHandler.getStackInSlot(0).isEmpty) {
					return@withBlockEntityDo
				}
				player.setItemInHand(InteractionHand.MAIN_HAND, drillTipHandler.getStackInSlot(0).copy())
				drillTipHandler.extractItem(0, 1, false)
				be.notifyUpdate()
				return@withBlockEntityDo
			}

			if(drillTipHandler.getStackInSlot(0).isEmpty && stack.tags.anyMatch { key -> CreateOreDepositsTags.DRILL_TIP == key }){
				val newItemStack = ItemStack(stack.item, 1);
				stack.consume(1, player)
				drillTipHandler.insertItem(0, newItemStack, false)
				be.notifyUpdate()
				return@withBlockEntityDo
			}
			println(stack.tags.anyMatch { key -> CreateOreDepositsTags.DRILL_TIP == key })
			println(stack.tags)
		})

		return ItemInteractionResult.SUCCESS
	}
}