package com.palmganger.entity;

import com.palmganger.registry.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

/** Cannon shell of PALMGANGER 9000. Explodes on any impact. */
public class PalmShellEntity extends ThrowableItemProjectile {
    public static final float EXPLOSION_POWER = 3.5F;

    public PalmShellEntity(EntityType<? extends PalmShellEntity> type, Level level) {
        super(type, level);
    }

    public PalmShellEntity(Level level, LivingEntity shooter) {
        super(ModEntities.PALM_SHELL.get(), shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.FIRE_CHARGE;
    }

    @Override
    protected float getGravity() {
        return 0.005F;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX(), this.getY(), this.getZ(), 0.91D, 0.30D, 0.60D);
            this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        } else if (this.tickCount > 200) {
            this.discard();
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            // The boss ignores damage coming from his own shells (see PalmGanger9000Entity#hurt).
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), EXPLOSION_POWER, Level.ExplosionInteraction.MOB);
            this.discard();
        }
    }
}
