package com.palmganger.client;

import com.palmganger.PalmGangerMod;
import com.palmganger.entity.PalmGanger9000Entity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PalmTankRenderer extends MobRenderer<PalmGanger9000Entity, PalmTankModel> {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(new ResourceLocation(PalmGangerMod.MODID, "palm_ganger_9000"), "main");
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(PalmGangerMod.MODID, "textures/entity/palm_ganger_9000.png");

    public PalmTankRenderer(EntityRendererProvider.Context context) {
        super(context, new PalmTankModel(context.bakeLayer(LAYER)), 1.8F);
    }

    @Override
    public ResourceLocation getTextureLocation(PalmGanger9000Entity entity) {
        return TEXTURE;
    }
}
