package com.palmganger;

import com.palmganger.registry.ModBlocks;
import com.palmganger.registry.ModEntities;
import com.palmganger.registry.ModItems;
import com.palmganger.registry.ModSounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod(PalmGangerMod.MODID)
public class PalmGangerMod {
    public static final String MODID = "palmganger";

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> TAB = TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.palmganger"))
                    .icon(() -> new ItemStack(ModItems.PALM_CRYSTAL.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.PALM_GANGER_SPAWN_EGG.get());
                        output.accept(ModItems.PALM_9000_SPAWN_EGG.get());
                        output.accept(ModItems.PALM_ORE.get());
                        output.accept(ModItems.PALM_BLOCK.get());
                        output.accept(ModItems.PALM_CRYSTAL.get());
                        output.accept(ModItems.PALM_SUMMONER.get());
                        output.accept(ModItems.PALM_DISC.get());
                    })
                    .build());

    public PalmGangerMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModSounds.SOUNDS.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        ModEntities.ENTITIES.register(bus);
        TABS.register(bus);
    }
}
