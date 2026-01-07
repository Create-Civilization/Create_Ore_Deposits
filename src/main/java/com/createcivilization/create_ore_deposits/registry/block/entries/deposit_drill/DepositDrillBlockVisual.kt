package com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill

import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels.HOSE
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels.DRILL_MAGNET
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels.HOSE_HALF_MAGNET
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels.HOSE_HALF

import com.simibubi.create.AllPartialModels
import com.simibubi.create.AllSpriteShifts
import com.simibubi.create.content.contraptions.pulley.AbstractPulleyVisual
import com.simibubi.create.content.processing.burner.ScrollInstance
import com.simibubi.create.foundation.render.AllInstanceTypes

import dev.engine_room.flywheel.api.instance.Instancer
import dev.engine_room.flywheel.api.model.Model
import dev.engine_room.flywheel.api.visualization.VisualizationContext
import dev.engine_room.flywheel.lib.instance.InstanceTypes
import dev.engine_room.flywheel.lib.instance.TransformedInstance
import dev.engine_room.flywheel.lib.model.Models
import dev.engine_room.flywheel.lib.model.baked.BakedModelBuilder

import net.createmod.catnip.render.SpriteShiftEntry
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class DepositDrillBlockVisual(
	dispatcher: VisualizationContext,
	blockEntity: DepositDrillBlockEntity,
	partialTick: Float
) : AbstractPulleyVisual<DepositDrillBlockEntity>(dispatcher, blockEntity, partialTick) {

	override fun getRopeModel(): Instancer<TransformedInstance> =
		instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(HOSE))


//	override fun getMagnetModel(): Instancer<TransformedInstance> =
//		instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(DRILL_MAGNET))

	override fun getMagnetModel(): Instancer<TransformedInstance> {
		var stack = blockEntity.getDrillTipItemHandler().getStackInSlot(0)
		if(stack.isEmpty){
			return instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(DRILL_MAGNET))
		}
		val minecraft = Minecraft.getInstance()
		val bakedModel: BakedModel = minecraft.itemRenderer.getModel(stack, null, null, 0)
		val model : Model = BakedModelBuilder(bakedModel).build()
		return instancerProvider().instancer(InstanceTypes.TRANSFORMED, model)
	}

	override fun getHalfMagnetModel(): Instancer<TransformedInstance> =
		instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(HOSE_HALF_MAGNET))

	override fun getCoilModel(): Instancer<ScrollInstance> {
		return instancerProvider().instancer(AllInstanceTypes.SCROLLING, Models.partial(AllPartialModels.HOSE_COIL))
	}

	override fun getHalfRopeModel(): Instancer<TransformedInstance> =
		instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(HOSE_HALF))

	override fun getOffset(pt: Float): Float = blockEntity.getInterpolatedOffset(pt)

	override fun isRunning(): Boolean = true

	override fun getCoilAnimation(): SpriteShiftEntry = AllSpriteShifts.HOSE_PULLEY_COIL
}