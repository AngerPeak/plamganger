package com.palmganger.client;

import com.palmganger.PalmGangerMod;
import com.palmganger.entity.PalmGangerEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PalmGangerRenderer extends MobRenderer<PalmGangerEntity, PalmGangerModel> {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(new ResourceLocation(PalmGangerMod.MODID, "palm_ganger"), "main");
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(PalmGangerMod.MODID, "textures/entity/palm_ganger.png");

    public PalmGangerRenderer(EntityRendererProvider.Context context) {
        super(context, new PalmGangerModel(context.bakeLayer(LAYER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(PalmGangerEntity entity) {
        return TEXTURE;
    }
}
