package com.palmganger.entity;

import com.palmganger.registry.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Neutral drunk mob. Calm and slow until somebody hurts him, then he chases the attacker faster.
 */
public class PalmGangerEntity extends PathfinderMob {
    private static final UUID ANGRY_SPEED_UUID = UUID.fromString("5d3a1f0e-7c2b-4e55-9a11-8f0c6b2d4a77");
    /** +70% of base speed while angry (0.17 -> ~0.29). */
    private static final AttributeModifier ANGRY_SPEED =
            new AttributeModifier(ANGRY_SPEED_UUID, "Palm ganger anger", 0.7D, AttributeModifier.Operation.MULTIPLY_BASE);

    /** Radius (blocks) in which players get the "drunk" screen wobble. */
    private static final double DRUNK_RADIUS = 5.0D;

    public PalmGangerEntity(EntityType<? extends PalmGangerEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.17D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        // Neutral: only fights back against whoever hit him.
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        if (this.level().isClientSide) return;
        AttributeInstance speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed == null) return;
        if (target != null && target.isAlive()) {
            if (!speed.hasModifier(ANGRY_SPEED)) {
                speed.addTransientModifier(ANGRY_SPEED);
                this.playSound(ModSounds.ANGRY.get(), this.getSoundVolume(), this.getVoicePitch());
            }
        } else if (speed.hasModifier(ANGRY_SPEED)) {
            speed.removeModifier(ANGRY_SPEED_UUID);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide) {
            // Swirling potion-like particles (pink / lime) around the body.
            if (this.random.nextInt(2) == 0) {
                boolean pink = this.random.nextBoolean();
                this.level().addParticle(ParticleTypes.ENTITY_EFFECT,
                        this.getRandomX(0.6D), this.getRandomY(), this.getRandomZ(0.6D),
                        pink ? 0.91D : 0.62D, pink ? 0.30D : 0.88D, pink ? 0.60D : 0.10D);
            }
        } else if (this.tickCount % 20 == 0) {
            // Drunk effect for everybody standing close (screen wobble, like a potion).
            for (Player player : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(DRUNK_RADIUS))) {
                if (!player.isCreative() && !player.isSpectator()) {
                    player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0, true, false));
                }
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.DEATH.get();
    }
}
