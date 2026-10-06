package com.palmganger.registry;

import com.palmganger.PalmGangerMod;
import com.palmganger.entity.PalmGanger9000Entity;
import com.palmganger.entity.PalmGangerEntity;
import com.palmganger.entity.PalmShellEntity;
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

    public static final RegistryObject<EntityType<PalmGanger9000Entity>> PALM_GANGER_9000 = ENTITIES.register("palm_ganger_9000",
            () -> EntityType.Builder.<PalmGanger9000Entity>of(PalmGanger9000Entity::new, MobCategory.MISC)
                    .sized(3.0F, 2.9F)
                    .clientTrackingRange(10)
                    .fireImmune()
                    .build(PalmGangerMod.MODID + ":palm_ganger_9000"));

    public static final RegistryObject<EntityType<PalmShellEntity>> PALM_SHELL = ENTITIES.register("palm_shell",
            () -> EntityType.Builder.<PalmShellEntity>of(PalmShellEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(2)
                    .build(PalmGangerMod.MODID + ":palm_shell"));
}
