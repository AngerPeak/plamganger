package com.palmganger.registry;

import com.palmganger.PalmGangerMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, PalmGangerMod.MODID);

    // Spawn egg colours are painted into the item textures themselves.
    public static final RegistryObject<Item> PALM_GANGER_SPAWN_EGG = ITEMS.register("palm_ganger_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.PALM_GANGER, 0x7BC62D, 0xC8102E, new Item.Properties()));
    public static final RegistryObject<Item> PALM_9000_SPAWN_EGG = ITEMS.register("palm_ganger_9000_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.PALM_GANGER_9000, 0x111111, 0xE8449A, new Item.Properties()));

    public static final RegistryObject<Item> PALM_ORE = ITEMS.register("palm_ore",
            () -> new BlockItem(ModBlocks.PALM_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> PALM_BLOCK = ITEMS.register("palm_block",
            () -> new BlockItem(ModBlocks.PALM_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> PALM_SUMMONER = ITEMS.register("palm_summoner",
            () -> new BlockItem(ModBlocks.PALM_SUMMONER.get(), new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> PALM_CRYSTAL = ITEMS.register("palm_crystal",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    /** Music disc with the theme track (about 10.4 s = ~210 ticks). */
    public static final RegistryObject<Item> PALM_DISC = ITEMS.register("palm_disc",
            () -> new RecordItem(13, ModSounds.DISC, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 210));
}
