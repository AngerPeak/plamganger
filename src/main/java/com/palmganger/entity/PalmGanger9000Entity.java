package com.palmganger.entity;

import com.palmganger.registry.ModItems;
import com.palmganger.registry.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.LookAtTradingPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TradeWithPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.UUID;

/**
 * PALMGANGER 9000: a Palm Ganger on a tank. 200 HP. Neutral and tradeable (emeralds -> music disc)
 * until hit, then he shoots explosive shells that never hurt him.
 */
public class PalmGanger9000Entity extends AbstractVillager {
    private static final UUID ANGRY_SPEED_UUID = UUID.fromString("0b8c6a1e-52f4-4c53-b8d2-3f4a9e7d1c90");
    private static final AttributeModifier ANGRY_SPEED =
            new AttributeModifier(ANGRY_SPEED_UUID, "Palm 9000 anger", 0.6D, AttributeModifier.Operation.MULTIPLY_BASE);

    public PalmGanger9000Entity(EntityType<? extends PalmGanger9000Entity> type, Level level) {
        super(type, level);
        this.setMaxUpStep(1.1F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.16D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(1, new LookAtTradingPlayerGoal(this));
        this.goalSelector.addGoal(2, new CannonGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    // ---------------------------------------------------------------- combat

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // Own cannon explosions never damage the tank.
        if (source.getEntity() == this || source.getDirectEntity() == this || source.getDirectEntity() instanceof PalmShellEntity) {
            return false;
        }
        return super.hurt(source, amount);
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
                this.setTradingPlayer(null);
                this.playSound(ModSounds.BOSS_ANGRY.get(), 2.0F, 1.0F);
            }
        } else if (speed.hasModifier(ANGRY_SPEED)) {
            speed.removeModifier(ANGRY_SPEED_UUID);
        }
    }

    /** Fires one shell at the target. */
    void fireAt(LivingEntity target) {
        Vec3 horizontal = Vec3.directionFromRotation(0.0F, this.yHeadRot);
        Vec3 muzzle = this.position().add(0.0D, 1.45D, 0.0D).add(horizontal.scale(3.0D));
        PalmShellEntity shell = new PalmShellEntity(this.level(), this);
        shell.setPos(muzzle.x, muzzle.y, muzzle.z);
        double dx = target.getX() - muzzle.x;
        double dy = target.getY(0.5D) - muzzle.y;
        double dz = target.getZ() - muzzle.z;
        double flat = Math.sqrt(dx * dx + dz * dz);
        shell.shoot(dx, dy + flat * 0.02D, dz, 2.2F, 0.6F);
        this.level().addFreshEntity(shell);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 1.2F, 1.3F);
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.POOF, muzzle.x, muzzle.y, muzzle.z, 12, 0.2, 0.2, 0.2, 0.05);
        }
    }

    private static class CannonGoal extends Goal {
        private final PalmGanger9000Entity tank;
        private int cooldown;

        CannonGoal(PalmGanger9000Entity tank) {
            this.tank = tank;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity t = tank.getTarget();
            return t != null && t.isAlive();
        }

        @Override
        public void start() {
            cooldown = 30;
        }

        @Override
        public void stop() {
            tank.getNavigation().stop();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity t = tank.getTarget();
            if (t == null) return;
            tank.getLookControl().setLookAt(t, 40.0F, 40.0F);
            double dist = tank.distanceTo(t);
            if (dist > 14.0D) {
                tank.getNavigation().moveTo(t, 1.0D);
            } else {
                tank.getNavigation().stop();
            }
            if (cooldown > 0) cooldown--;
            if (cooldown <= 0 && dist < 40.0D && tank.getSensing().hasLineOfSight(t)) {
                tank.fireAt(t);
                cooldown = 45;
            }
        }
    }

    // ---------------------------------------------------------------- trading

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.isAlive() && !this.isTrading() && this.getTarget() == null) {
            if (!this.level().isClientSide) {
                this.setTradingPlayer(player);
                this.openTradingScreen(player, this.getDisplayName(), 1);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected void updateTrades() {
        MerchantOffers offers = this.getOffers();
        // 24 emeralds -> the music disc (can be bought a few times)
        offers.add(new MerchantOffer(new ItemStack(Items.EMERALD, 24), new ItemStack(ModItems.PALM_DISC.get()), 3, 5, 0.05F));
    }

    @Override
    protected void rewardTradeXp(MerchantOffer offer) {
        if (offer.shouldRewardExp()) {
            this.level().addFreshEntity(new net.minecraft.world.entity.ExperienceOrb(this.level(),
                    this.getX(), this.getY() + 0.5D, this.getZ(), 3 + this.random.nextInt(4)));
        }
    }

    @Override
    public int getVillagerXp() {
        return 0;
    }

    @Override
    public void overrideXp(int xp) {
    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.VILLAGER_YES;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return null;
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    // ---------------------------------------------------------------- misc

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide && this.random.nextInt(3) == 0) {
            boolean pink = this.random.nextBoolean();
            this.level().addParticle(ParticleTypes.ENTITY_EFFECT,
                    this.getRandomX(0.8D), this.getRandomY(), this.getRandomZ(0.8D),
                    pink ? 0.91D : 0.62D, pink ? 0.30D : 0.88D, pink ? 0.60D : 0.10D);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.getTarget() == null ? ModSounds.BOSS_AMBIENT.get() : null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.BOSS_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.BOSS_DEATH.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }
}
