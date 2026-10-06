package com.palmganger.client;

import com.palmganger.PalmGangerMod;
import com.palmganger.registry.ModEntities;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PalmGangerMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(PalmGangerRenderer.LAYER, PalmGangerModel::createBodyLayer);
        event.registerLayerDefinition(PalmTankRenderer.LAYER, PalmTankModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.PALM_GANGER.get(), PalmGangerRenderer::new);
        event.registerEntityRenderer(ModEntities.PALM_GANGER_9000.get(), PalmTankRenderer::new);
        event.registerEntityRenderer(ModEntities.PALM_SHELL.get(), ctx -> new ThrownItemRenderer<>(ctx, 2.0F, true));
    }
}
