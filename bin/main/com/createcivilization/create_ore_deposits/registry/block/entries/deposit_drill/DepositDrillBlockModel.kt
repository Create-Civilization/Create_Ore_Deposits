package com.createcivilization.create_ore_deposits.registry.block.entries.deposit_drill

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer

import net.minecraft.client.model.Model
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.renderer.RenderType

class DepositDrillBlockModel(root: ModelPart) : Model(RenderType::entityCutoutNoCull) {

	val main: ModelPart = root.getChild("main")

	override fun renderToBuffer(
		poseStack: PoseStack,
		vertexConsumer: VertexConsumer,
		packedLight: Int,
		packedOverlay: Int,
		color: Int
	) = this.main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color)

	companion object {

		fun createModel(): LayerDefinition {
			val mesh = MeshDefinition()
			val part = mesh.root

			val main = part.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0f, 16.0f, 0.0f))

			main.addOrReplaceChild(
				"drill", CubeListBuilder.create()
					.texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 2.0f, 8.0f, CubeDeformation(0.0f))
					.texOffs(0, 10).addBox(-3.0f, -10.0f, -3.0f, 6.0f, 2.0f, 6.0f, CubeDeformation(0.0f))
					.texOffs(0, 18).addBox(-2.0f, -12.0f, -2.0f, 4.0f, 2.0f, 4.0f, CubeDeformation(0.0f))
					.texOffs(0, 24).addBox(-1.0f, -14.0f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.0f)),
				PartPose.offset(0.0f, 0.0f, 0.0f)
			)

			return LayerDefinition.create(mesh, 32, 32)
		}
	}
}