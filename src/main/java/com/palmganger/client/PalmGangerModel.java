package com.palmganger.client;

import com.palmganger.entity.PalmGangerEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/** Humanoid with a lime "cube" hat, pink horns, a big belly, heart boxers, a cigarette and a bottle. Texture 128x128. */
public class PalmGangerModel extends HumanoidModel<PalmGangerEntity> {
    public PalmGangerModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // ---- head: black face + cigarette + pink horns
        CubeListBuilder head = CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4F, -8F, -4F, 8, 8, 8)
                .texOffs(96, 40).addBox(0.5F, -2.5F, -7F, 1, 1, 3);
        // horn segments for the right side (x of the segment's min corner, y, z); left side is mirrored
        float[][] horn = {
                {-6F, -10F, -1F}, {-7F, -12F, -1F}, {-7.5F, -14F, -1F}, {-6.5F, -15.5F, -1F},
                {-6.5F, -7F, -3F}, {-6.5F, -5F, 1.5F}
        };
        for (float[] h : horn) {
            head.texOffs(48, 40).addBox(h[0], h[1], h[2], 2, 2, 2);
            head.texOffs(48, 40).addBox(-h[0] - 2F, h[1], h[2], 2, 2, 2);
        }
        root.addOrReplaceChild("head", head, PartPose.ZERO);

        // ---- lime cube on the back of the head (HumanoidModel copies head pose to hat)
        root.addOrReplaceChild("hat", CubeListBuilder.create()
                .texOffs(32, 0).addBox(-5F, -9.5F, -3F, 10, 10, 9), PartPose.ZERO);

        // ---- body: torso + belly + boxers
        root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 20).addBox(-4F, 0F, -2F, 8, 12, 4)
                .texOffs(70, 0).addBox(-3.5F, 5F, -5F, 7, 5, 3)
                .texOffs(0, 40).addBox(-4F, 8F, -2F, 8, 4, 4, new CubeDeformation(0.3F)), PartPose.ZERO);

        // ---- right arm holds the vodka bottle
        root.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .texOffs(24, 20).addBox(-3F, -2F, -2F, 4, 12, 4)
                .texOffs(64, 40).addBox(-5F, 7F, -1F, 2, 6, 2)
                .texOffs(84, 40).addBox(-4.5F, 5F, -0.5F, 1, 2, 1)
                .texOffs(80, 40).addBox(-4.5F, 4F, -0.5F, 1, 1, 1), PartPose.offset(-5F, 2F, 0F));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create()
                .texOffs(24, 20).addBox(-1F, -2F, -2F, 4, 12, 4), PartPose.offset(5F, 2F, 0F));

        // ---- legs with boxer cuffs
        root.addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(56, 20).addBox(-2F, 0F, -2F, 4, 12, 4)
                .texOffs(24, 40).addBox(-2F, 0F, -2F, 4, 5, 4, new CubeDeformation(0.3F)), PartPose.offset(-1.9F, 12F, 0F));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create()
                .texOffs(56, 20).addBox(-2F, 0F, -2F, 4, 12, 4)
                .texOffs(24, 40).addBox(-2F, 0F, -2F, 4, 5, 4, new CubeDeformation(0.3F)), PartPose.offset(1.9F, 12F, 0F));

        return LayerDefinition.create(mesh, 128, 128);
    }
}
