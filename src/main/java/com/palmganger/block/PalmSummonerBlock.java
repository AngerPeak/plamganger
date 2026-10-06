package com.palmganger.block;

import com.palmganger.entity.PalmGanger9000Entity;
import com.palmganger.registry.ModEntities;
import com.palmganger.registry.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

/**
 * "Palm mayhem": once placed it counts 5..1 in the middle of every nearby player's screen,
 * then explodes (visual only) and PALMGANGER 9000 appears on his tank.
 */
public class PalmSummonerBlock extends Block {
    public static final IntegerProperty COUNT = IntegerProperty.create("count", 1, 5);

    public PalmSummonerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(COUNT, 5));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COUNT);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving) {
        if (!level.isClientSide && !oldState.is(this)) {
            announce((ServerLevel) level, pos, state.getValue(COUNT));
            level.scheduleTick(pos, this, 20);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int next = state.getValue(COUNT) - 1;
        if (next >= 1) {
            level.setBlock(pos, state.setValue(COUNT, next), 3);
            announce(level, pos, next);
            level.scheduleTick(pos, this, 20);
        } else {
            summon(level, pos);
        }
    }

    private static void announce(ServerLevel level, BlockPos pos, int number) {
        for (ServerPlayer player : level.players()) {
            if (player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 64 * 64) {
                player.connection.send(new ClientboundSetTitlesAnimationPacket(0, 16, 4));
                player.connection.send(new ClientboundSetSubtitleTextPacket(Component.empty()));
                player.connection.send(new ClientboundSetTitleTextPacket(
                        Component.literal(String.valueOf(number)).withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD)));
            }
        }
        level.playSound(null, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1.5F, 0.5F + (5 - number) * 0.25F);
        level.sendParticles(ParticleTypes.END_ROD, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 12, 0.3, 0.4, 0.3, 0.05);
    }

    private static void summon(ServerLevel level, BlockPos pos) {
        double x = pos.getX() + 0.5, y = pos.getY(), z = pos.getZ() + 0.5;
        level.removeBlock(pos, false);

        // Explosion for show: no damage and no destroyed blocks.
        level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y + 1.0, z, 1, 0, 0, 0, 0);
        level.sendParticles(ParticleTypes.FLAME, x, y + 1.0, z, 80, 1.5, 1.0, 1.5, 0.08);
        level.playSound(null, pos, SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 4.0F, 0.7F);
        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
        if (bolt != null) {
            bolt.moveTo(x, y, z);
            bolt.setVisualOnly(true);
            level.addFreshEntity(bolt);
        }

        PalmGanger9000Entity boss = ModEntities.PALM_GANGER_9000.get().create(level);
        if (boss != null) {
            boss.moveTo(x, y, z, level.random.nextFloat() * 360F, 0F);
            boss.setPersistenceRequired();
            level.addFreshEntity(boss);
            // Voice line + quiet music
            level.playSound(null, pos, ModSounds.BOSS_SUMMON.get(), SoundSource.HOSTILE, 1.5F, 1.0F);
            level.playSound(null, pos, ModSounds.THEME.get(), SoundSource.RECORDS, 2.0F, 1.0F);
        }
        for (ServerPlayer player : level.players()) {
            if (player.distanceToSqr(x, y, z) < 64 * 64) {
                player.connection.send(new ClientboundSetTitlesAnimationPacket(5, 50, 15));
                player.connection.send(new ClientboundSetSubtitleTextPacket(Component.empty()));
                player.connection.send(new ClientboundSetTitleTextPacket(
                        Component.literal("PALMGANGER 9000").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)));
            }
        }
    }
}
