package com.createcivilization.create_ore_deposits.client.events;

import com.createcivilization.create_ore_deposits.CreateOreDeposits;
import com.createcivilization.create_ore_deposits.block.entity.CODBlockEntities;
import com.createcivilization.create_ore_deposits.client.model.block.DrillBlockModel;
import com.createcivilization.create_ore_deposits.client.renderer.block.DrillBERenderer;
import com.createcivilization.create_ore_deposits.util.Utils;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = CreateOreDeposits.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientBusEvents
{

    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event)
    {
        BlockEntityRenderers.register(CODBlockEntities.DEPOSIT_TESTER_BLOCK_ENTITY.get(), DrillBERenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        event.registerLayerDefinition(ModModelLayers.DRILL_LAYER, DrillBlockModel::createModel);
    }

    //I dont think we would need that many model layers
    //TODO::Make it in a seperate class
    public static class ModModelLayers
    {
        public static final ModelLayerLocation DRILL_LAYER = new ModelLayerLocation(
                Utils.asResource("drill"),"main"
        );
    }
}
