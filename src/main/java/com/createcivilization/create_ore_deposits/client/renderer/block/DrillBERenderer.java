package com.createcivilization.create_ore_deposits.client.renderer.block;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import com.createcivilization.create_ore_deposits.block.entity.custom.DrillBlockEntity;
import com.createcivilization.create_ore_deposits.client.events.ModClientBusEvents;
import com.createcivilization.create_ore_deposits.client.model.block.DrillBlockModel;
import com.createcivilization.create_ore_deposits.util.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

import java.util.Objects;

@SuppressWarnings("NullableProblems")
public class DrillBERenderer implements BlockEntityRenderer<DrillBlockEntity> {
    final BlockEntityRendererProvider.Context context;
    final DrillBlockModel model;
    final ResourceLocation texture = Utils.asResource("textures/entity/block/drill_head.png");
    public DrillBERenderer(BlockEntityRendererProvider.Context context){
        this.context = context;
        this.model = new DrillBlockModel(context.bakeLayer(ModClientBusEvents.ModModelLayers.DRILL_LAYER));
    }

    @Override
    public void render(DrillBlockEntity drillBlockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        var isAir = Objects.requireNonNull(drillBlockEntity.getLevel()).getBlockState(drillBlockEntity.getBlockPos().below()).isAir();
        if(isAir){
            poseStack.pushPose();
            model.renderToBuffer(poseStack,bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture,false)),packedLight,packedOverlay);
            poseStack.translate(0.5F, -1.0F, 0.5F);
            poseStack.popPose();
        }
        else{
            CreateOreDeposits.LOGGER.warn("IDIOTA, REAL IDIOTA");
        }
    }
}
