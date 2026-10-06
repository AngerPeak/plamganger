package com.palmganger.registry;

import com.palmganger.PalmGangerMod;
import com.palmganger.block.PalmSummonerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, PalmGangerMod.MODID);

    public static final RegistryObject<Block> PALM_ORE = BLOCKS.register("palm_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(3.0F, 3.0F).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> PALM_BLOCK = BLOCKS.register("palm_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5.0F, 6.0F).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    public static final RegistryObject<Block> PALM_SUMMONER = BLOCKS.register("palm_summoner",
            () -> new PalmSummonerBlock(BlockBehaviour.Properties.of().strength(5.0F, 6.0F).requiresCorrectToolForDrops()
                    .sound(SoundType.METAL).lightLevel(state -> 10)));
}
