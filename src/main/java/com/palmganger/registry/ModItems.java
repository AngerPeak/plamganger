package com.palmganger.registry;

import com.palmganger.PalmGangerMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, PalmGangerMod.MODID);

    // Colours of the egg (green / red / black) are painted in the item texture itself.
    public static final RegistryObject<Item> PALM_GANGER_SPAWN_EGG = ITEMS.register("palm_ganger_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.PALM_GANGER, 0x7BC62D, 0xC8102E, new Item.Properties()));
}
