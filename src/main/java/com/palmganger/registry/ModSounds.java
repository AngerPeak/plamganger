package com.palmganger.registry;

import com.palmganger.PalmGangerMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PalmGangerMod.MODID);

    public static final RegistryObject<SoundEvent> AMBIENT = reg("palm_ganger.ambient");
    public static final RegistryObject<SoundEvent> HURT = reg("palm_ganger.hurt");
    public static final RegistryObject<SoundEvent> ANGRY = reg("palm_ganger.angry");
    public static final RegistryObject<SoundEvent> DEATH = reg("palm_ganger.death");

    public static final RegistryObject<SoundEvent> BOSS_AMBIENT = reg("boss.ambient");
    public static final RegistryObject<SoundEvent> BOSS_ANGRY = reg("boss.angry");
    public static final RegistryObject<SoundEvent> BOSS_SUMMON = reg("boss.summon");
    public static final RegistryObject<SoundEvent> BOSS_HURT = reg("boss.hurt");
    public static final RegistryObject<SoundEvent> BOSS_DEATH = reg("boss.death");

    /** Music that plays when the boss appears. */
    public static final RegistryObject<SoundEvent> THEME = reg("theme");
    /** Same track, used by the music disc in a jukebox. */
    public static final RegistryObject<SoundEvent> DISC = reg("disc");

    private static RegistryObject<SoundEvent> reg(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PalmGangerMod.MODID, name)));
    }
}
