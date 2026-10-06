package com.palmganger.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.palmganger.entity.PalmGanger9000Entity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/** Tank with a big Palm Ganger head on the turret. Texture 256x256. The turret follows the head yaw, the barrel the pitch. */
public class PalmTankModel extends EntityModel<PalmGanger9000Entity> {
    private final ModelPart root;
    private final ModelPart turret;
    private final ModelPart barrel;
    private final ModelPart head;

    public PalmTankModel(ModelPart root) {
        this.root = root;
        this.turret = root.getChild("turret");
        this.barrel = this.turret.getChild("barrel");
        this.head = this.turret.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("track_l", CubeListBuilder.create().texOffs(0, 0).addBox(14F, 12F, -28F, 10, 12, 56), PartPose.ZERO);
        root.addOrReplaceChild("track_r", CubeListBuilder.create().texOffs(0, 0).addBox(-24F, 12F, -28F, 10, 12, 56), PartPose.ZERO);
        root.addOrReplaceChild("hull", CubeListBuilder.create().texOffs(0, 70).addBox(-14F, 6F, -26F, 28, 12, 52), PartPose.ZERO);

        PartDefinition turret = root.addOrReplaceChild("turret", CubeListBuilder.create()
                .texOffs(0, 136).addBox(-12F, -10F, -12F, 24, 10, 24)
                .texOffs(100, 136).addBox(-10F, -8F, 10F, 20, 8, 6), PartPose.offset(0F, 6F, 2F));

        turret.addOrReplaceChild("barrel", CubeListBuilder.create()
                .texOffs(140, 0).addBox(-2F, -2F, -36F, 4, 4, 36)
                .texOffs(140, 42).addBox(-3F, -3F, -40F, 6, 6, 6)
                .texOffs(156, 136).addBox(-5F, -5F, -3F, 10, 10, 4), PartPose.offset(0F, -4F, -12F));

        CubeListBuilder head = CubeListBuilder.create()
                .texOffs(0, 172).addBox(-6F, -12F, -6F, 12, 12, 12)
                .texOffs(50, 172).addBox(-7.5F, -14.25F, -4.5F, 15, 15, 14);
        float[][] horn = {
                {-9F, -15F, -1.5F}, {-10.5F, -18F, -1.5F}, {-11.25F, -21F, -1.5F}, {-9.75F, -23.25F, -1.5F},
                {-9.75F, -10.5F, -4.5F}, {-9.75F, -7.5F, 2.25F}
        };
        for (float[] h : horn) {
            head.texOffs(112, 172).addBox(h[0], h[1], h[2], 3, 3, 3);
            head.texOffs(112, 172).addBox(-h[0] - 3F, h[1], h[2], 3, 3, 3);
        }
        turret.addOrReplaceChild("head", head, PartPose.offset(0F, -10F, 4F));

        return LayerDefinition.create(mesh, 256, 256);
    }

    @Override
    public void setupAnim(PalmGanger9000Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float yaw = netHeadYaw * ((float) Math.PI / 180F);
        this.turret.yRot = yaw;
        this.barrel.xRot = Mth.clamp(headPitch * ((float) Math.PI / 180F), -0.35F, 0.2F);
        this.head.yRot = 0F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
