package com.palmganger.registry;

import com.palmganger.PalmGangerMod;
import com.palmganger.entity.PalmGangerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PalmGangerMod.MODID);

    public static final RegistryObject<EntityType<PalmGangerEntity>> PALM_GANGER = ENTITIES.register("palm_ganger",
            () -> EntityType.Builder.<PalmGangerEntity>of(PalmGangerEntity::new, MobCategory.MONSTER)
                    .sized(0.8F, 1.95F)
                    .clientTrackingRange(8)
                    .build(PalmGangerMod.MODID + ":palm_ganger"));
}
