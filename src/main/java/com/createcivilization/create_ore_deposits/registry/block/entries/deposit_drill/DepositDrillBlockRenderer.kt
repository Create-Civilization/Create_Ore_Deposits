package com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill

import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels.HOSE
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels.HOSE_HALF
import com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill.DepositDrillBlockModels.DRILL_MAGNET

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.simibubi.create.AllPartialModels
import com.simibubi.create.AllSpriteShifts
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer
import com.simibubi.create.infrastructure.config.AllConfigs
import dev.engine_room.flywheel.api.visualization.VisualizationManager
import dev.engine_room.flywheel.lib.model.baked.PartialModel
import net.createmod.catnip.render.CachedBuffers
import net.createmod.catnip.render.SpriteShiftEntry
import net.createmod.catnip.render.SuperByteBuffer
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import kotlin.math.floor

class DepositDrillBlockRenderer(
	context: BlockEntityRendererProvider.Context
) : KineticBlockEntityRenderer<DepositDrillBlockEntity>(context) {

	override fun shouldRenderOffScreen(drillBlockEntity: DepositDrillBlockEntity): Boolean {
		return true
	}

	override fun renderSafe(
		be: DepositDrillBlockEntity,
		partialTicks: Float,
		ms: PoseStack,
		buffer: MultiBufferSource,
		light: Int, overlay: Int
	) {
		if (VisualizationManager.supportsVisualization(be.getLevel())) return

		super.renderSafe(be, partialTicks, ms, buffer, light, overlay)
		val offset: Float = getOffset(be, partialTicks)
		val running: Boolean = isRunning(be)

		val vb: VertexConsumer = buffer.getBuffer(RenderType.solid())
		scrollCoil(getRotatedCoil(be), this.coilShift, offset, 1f)
			.light<SuperByteBuffer>(light)
			.renderInto(ms, vb)

		val world: Level? = be.getLevel()
		val blockState: BlockState = be.blockState
		val pos: BlockPos = be.blockPos

		val halfMagnet: SuperByteBuffer? = CachedBuffers.partial(this.halfMagnet, blockState)
		val halfRope = CachedBuffers.partial(this.halfRope, blockState)
		val magnet = renderMagnet(be)
		val rope = renderRope(be)

		if (running || offset == 0f) {
			renderAt(
				world!!,
				(if (offset > .25f) magnet else halfMagnet)!!,
				offset,
				pos,
				ms,
				vb
			)
		}

		val f: Float = offset % 1
		if (offset > .75f && (f !in .25f.. .75f)) renderAt(
			world!!,
			halfRope,
			if (f > .75f) f - 1 else f,
			pos,
			ms,
			vb
		)

		if (!running) return

		var i = 0
		while (i < offset - 1.25f) {
			renderAt(world!!, rope, offset - i - 1, pos, ms, vb)
			i++
		}
	}

	fun getShaftAxis(be: DepositDrillBlockEntity): Direction.Axis =
		be.blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).clockWise.axis

	private val halfMagnet: PartialModel = HOSE
	private val halfRope: PartialModel = HOSE_HALF
	val coil: PartialModel get() = AllPartialModels.HOSE_COIL

	val coilShift: SpriteShiftEntry get() = AllSpriteShifts.HOSE_PULLEY_COIL

	fun renderRope(be: DepositDrillBlockEntity): SuperByteBuffer = CachedBuffers.partial(HOSE, be.blockState)

	fun renderMagnet(be: DepositDrillBlockEntity): SuperByteBuffer = CachedBuffers.partial(DRILL_MAGNET, be.blockState)

	fun getOffset(be: DepositDrillBlockEntity, partialTicks: Float): Float = be.getInterpolatedOffset(partialTicks)

	// TODO: Implement actual is running logic
	fun isRunning(be: DepositDrillBlockEntity): Boolean = true

	override fun getRenderedBlockState(be: DepositDrillBlockEntity): BlockState = shaft(getShaftAxis(be))

	fun getRotatedCoil(be: DepositDrillBlockEntity): SuperByteBuffer = CachedBuffers.partialFacing(
		this.coil,
		be.blockState,
		Direction.get(Direction.AxisDirection.POSITIVE, getShaftAxis(be))
	)

	override fun getViewDistance(): Int = AllConfigs.server().kinetics.maxRopeLength.get()

	companion object {

		fun renderAt(
			world: LevelAccessor,
			partial: SuperByteBuffer, offset: Float, pulleyPos: BlockPos,
			ms: PoseStack, buffer: VertexConsumer
		) {
			val actualPos: BlockPos = pulleyPos.below(offset.toInt())
			val light: Int = LevelRenderer.getLightColor(world, world.getBlockState(actualPos), actualPos)
			partial.translate(0f, -offset, 0f)
				.light<SuperByteBuffer>(light)
				.renderInto(ms, buffer)
		}

		fun scrollCoil(
			sbb: SuperByteBuffer,
			coilShift: SpriteShiftEntry,
			offset: Float,
			speedModifier: Float
		): SuperByteBuffer {
			var offset: Float = offset
			if (offset == 0f) return sbb
			val spriteSize: Float = (coilShift.getTarget().v1 - coilShift.getTarget().v0)
			offset *= speedModifier / 2
			val coilScroll = -(offset + 3 / 16f) - floor(((offset + 3 / 16f) * -2).toDouble()) / 2
			return sbb.shiftUVScrolling(coilShift, coilScroll.toFloat() * spriteSize)
		}
	}
}