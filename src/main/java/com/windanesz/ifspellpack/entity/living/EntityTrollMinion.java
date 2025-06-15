package com.windanesz.ifspellpack.entity.living;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.windanesz.ifspellpack.entity.ai.TrollAIFleeSun;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.living.ISummonedCreature;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityTrollMinion extends EntityMob implements ISummonedCreature, IAnimatedEntity, IVillagerFear, IHumanoid {

    private int lifetime = -1;
    private UUID casterUUID;
    private boolean sunlightImmune;
    public static final Animation ANIMATION_STRIKE_HORIZONTAL = Animation.create(20);
    public static final Animation ANIMATION_STRIKE_VERTICAL = Animation.create(20);
    public static final Animation ANIMATION_SPEAK = Animation.create(10);
    public static final Animation ANIMATION_ROAR = Animation.create(25);
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityTrollMinion.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> WEAPON = EntityDataManager.createKey(EntityTrollMinion.class, DataSerializers.VARINT);
    public float stoneProgress;
    private int animationTick;
    private Animation currentAnimation;
    private boolean avoidSun = true;

    public EntityTrollMinion(World worldIn) {
        super(worldIn);
        this.setSize(1.2F, 3.5F);
        this.setType(EnumTroll.values()[this.rand.nextInt(EnumTroll.values().length)]);
        this.setWeaponType(EnumTroll.getWeaponForType(this.getType()));
    }

    private void setAvoidSun(boolean day) {
        if (day && !avoidSun) {
            ((PathNavigateGround) this.getNavigator()).setAvoidSun(true);
            avoidSun = true;
        }
        if (!day && avoidSun) {
            ((PathNavigateGround) this.getNavigator()).setAvoidSun(false);
            avoidSun = false;
        }
    }

    @Override
    public boolean isAIDisabled() {
        return EntityGorgon.isStoneMob(this) || super.isAIDisabled();
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new TrollAIFleeSun(this, 1.0D));
        this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(4, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F, 1.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityVillager.class, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, false));
        setAvoidSun(true);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(IceAndFire.CONFIG.trollAttackStrength);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IceAndFire.CONFIG.trollMaxHealth);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(9.0D);

    }

    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getRNG().nextBoolean()) {
            this.setAnimation(ANIMATION_STRIKE_VERTICAL);

        } else {
            this.setAnimation(ANIMATION_STRIKE_HORIZONTAL);
        }
        return true;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, 0);
        this.dataManager.register(WEAPON, 0);
    }

    public boolean getSunlightImmune() {
        return this.sunlightImmune;
    }

    public void setSunlightImmune(boolean sunlightImmune) {
        this.sunlightImmune = sunlightImmune;
    }

    private int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    private void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
    }

    public EnumTroll getType() {
        return EnumTroll.values()[getVariant()];
    }

    public void setType(EnumTroll variant) {
        this.setVariant(variant.ordinal());
    }

    private int getWeapon() {
        return this.dataManager.get(WEAPON);
    }

    private void setWeapon(int variant) {
        this.dataManager.set(WEAPON, variant);
    }

    public EnumTroll.Weapon getWeaponType() {
        return EnumTroll.Weapon.values()[getWeapon()];
    }

    public void setWeaponType(EnumTroll.Weapon variant) {
        this.setWeapon(variant.ordinal());
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        this.writeNBTDelegate(compound);
        compound.setInteger("Variant", this.getVariant());
        compound.setInteger("Weapon", this.getWeapon());
        compound.setFloat("StoneProgress", stoneProgress);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.readNBTDelegate(compound);
        this.setVariant(compound.getInteger("Variant"));
        this.setWeapon(compound.getInteger("Weapon"));
        this.stoneProgress = compound.getFloat("StoneProgress");
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if (source.getDamageType().contains("arrow")) {
            return false;
        }
        return super.attackEntityFrom(source, damage);
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(world.getDifficulty() == EnumDifficulty.PEACEFUL && this.getAttackTarget() instanceof EntityPlayer){
            this.setAttackTarget(null);
        }
        boolean stone = EntityGorgon.isStoneMob(this);
        if (stone && stoneProgress < 20.0F) {
            stoneProgress += 2F;
        } else if (!stone && stoneProgress > 0.0F) {
            stoneProgress -= 2F;
        }
        if (!stone && this.getAnimation() == NO_ANIMATION && this.getAttackTarget() != null && this.getRNG().nextInt(100) == 0) {
            this.setAnimation(ANIMATION_ROAR);
        }
        if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() == 5) {
            this.playSound(IafSoundRegistry.TROLL_ROAR, 1, 1);
        }
        if (!stone && this.getHealth() < this.getMaxHealth() && this.ticksExisted % 30 == 0) {
            this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 30, 1, false, false));
        }
        //Artefact start
        if (!this.sunlightImmune) {
            setAvoidSun(this.world.isDaytime());
            if (this.world.isDaytime() && !this.world.isRemote) {
                float f = this.getBrightness();
                BlockPos blockpos = this.getRidingEntity() instanceof EntityBoat ? (new BlockPos(this.posX, (double) Math.round(this.posY), this.posZ)).up() : new BlockPos(this.posX, (double) Math.round(this.posY), this.posZ);
                if (f > 0.5F && this.world.canSeeSky(blockpos)) {
                    StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);
                    if (properties != null && !properties.isStone) {
                        properties.isStone = true;
                        this.motionX = 0;
                        this.motionY = 0;
                        this.motionZ = 0;
                        this.setAnimation(NO_ANIMATION);
                        this.playSound(IafSoundRegistry.GORGON_TURN_STONE, 1, 1);
                    }
                }
            }
        }
        //Artefact finish
        if (this.getAnimation() == ANIMATION_STRIKE_VERTICAL && this.getAnimationTick() == 10) {
            float weaponX = (float) (posX + 1.9F * Math.cos((renderYawOffset + 90) * Math.PI / 180));
            float weaponZ = (float) (posZ + 1.9F * Math.sin((renderYawOffset + 90) * Math.PI / 180));
            float weaponY = (float) (posY + (0.2F));
            IBlockState state = world.getBlockState(new BlockPos(weaponX, weaponY - 1, weaponZ));
            for (int i = 0; i < 20; i++) {
                double motionX = getRNG().nextGaussian() * 0.07D;
                double motionY = getRNG().nextGaussian() * 0.07D;
                double motionZ = getRNG().nextGaussian() * 0.07D;
                if (state.getMaterial().isSolid() && world.isRemote) {
                    this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, weaponX + (this.getRNG().nextFloat() - 0.5F), weaponY + (this.getRNG().nextFloat() - 0.5F), weaponZ + (this.getRNG().nextFloat() - 0.5F), motionX, motionY, motionZ, Block.getIdFromBlock(state.getBlock()));
                }
            }
        }
        if (this.getAnimation() == ANIMATION_STRIKE_VERTICAL && this.getAttackTarget() != null && this.getDistanceSq(this.getAttackTarget()) < 4D && this.getAnimationTick() == 10 && this.deathTime <= 0) {
            this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
        }
        if (this.getAnimation() == ANIMATION_STRIKE_HORIZONTAL && this.getAttackTarget() != null && this.getDistanceSq(this.getAttackTarget()) < 4D && this.getAnimationTick() == 10 && this.deathTime <= 0) {
            this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
            float f5 = MathHelper.sin(this.rotationYaw * 0.017453292F);
            float f6 = MathHelper.cos(this.rotationYaw * 0.017453292F);
            this.getAttackTarget().motionX = f5;
            this.getAttackTarget().motionZ = f6;
            this.getAttackTarget().motionY = 0.4F;
        }
        if (this.getNavigator().noPath() && this.getAttackTarget() != null && this.getDistanceSq(this.getAttackTarget()) > 3 && this.getDistanceSq(this.getAttackTarget()) < 30 && this.world.getGameRules().getBoolean("mobGriefing")) {
            this.faceEntity(this.getAttackTarget(), 30, 30);
            if (this.getAnimation() == NO_ANIMATION && this.rand.nextInt(15) == 0) {
                this.setAnimation(ANIMATION_STRIKE_VERTICAL);
            }
            if (this.getAnimation() == ANIMATION_STRIKE_VERTICAL && this.getAnimationTick() == 10) {
                float weaponX = (float) (posX + 1.9F * Math.cos((renderYawOffset + 90) * Math.PI / 180));
                float weaponZ = (float) (posZ + 1.9F * Math.sin((renderYawOffset + 90) * Math.PI / 180));
                float weaponY = (float) (posY + (this.getEyeHeight() / 2));
                BlockBreakExplosion explosion = new BlockBreakExplosion(world, this, weaponX, weaponY, weaponZ, 1F + this.getRNG().nextFloat());
                if (!MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this, weaponX, weaponY, weaponZ))){
                    explosion.doExplosionA();
                    explosion.doExplosionB(true);
                }

                this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1, 1);

            }
        }
        if (this.getAnimation() == ANIMATION_STRIKE_VERTICAL && this.getAnimationTick() == 10) {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 2.5F, 0.5F);

        }
        if (this.getAnimation() == ANIMATION_STRIKE_HORIZONTAL && this.getAnimationTick() == 10) {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 2.5F, 0.5F);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public void playLivingSound() {
        if (this.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playLivingSound();
    }

    protected void playHurtSound(DamageSource source) {
        if (this.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playHurtSound(source);
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.TROLL_IDLE;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return IafSoundRegistry.TROLL_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.TROLL_DIE;
    }


    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ANIMATION_STRIKE_HORIZONTAL, ANIMATION_STRIKE_VERTICAL, ANIMATION_SPEAK, ANIMATION_ROAR};
    }

    @Override
    public void setRevengeTarget(EntityLivingBase entity){
        if(this.shouldRevengeTarget(entity)) super.setRevengeTarget(entity);
    }

    @Override
    public void onUpdate(){
        super.onUpdate();
        this.updateDelegate();
    }

    @Override
    public void setLifetime(int lifetime) { this.lifetime = lifetime;}

    @Override
    public int getLifetime() {
        return this.lifetime;
    }

    @Override
    public void setOwnerId(UUID uuid) {
        this.casterUUID = uuid;
    }

    @Nullable
    @Override
    public UUID getOwnerId() {
        return this.casterUUID;
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public boolean hasParticleEffect() {
        return true;
    }

    protected boolean processInteract(EntityPlayer player, EnumHand hand){
        return this.interactDelegate(player, hand) || super.processInteract(player, hand);
    }

    // Recommended overrides

    @Override protected int getExperiencePoints(EntityPlayer player){ return 0; }
    @Override protected boolean canDropLoot(){ return false; }
    @Override protected Item getDropItem(){ return null; }
    @Override protected ResourceLocation getLootTable(){ return null; }
    @Override public boolean canPickUpLoot(){ return false; }

    // This vanilla method has nothing to do with the custom despawn() method.
    @Override protected boolean canDespawn(){
        return getCaster() == null && getOwnerId() == null;
    }

    @Override
    public boolean getCanSpawnHere(){
        return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    @Override
    public boolean canAttackClass(Class<? extends EntityLivingBase> entityType){
        // Returns true unless the given entity type is a flying entity.
        return !EntityFlying.class.isAssignableFrom(entityType);
    }

    @Override
    public ITextComponent getDisplayName(){
        if(getCaster() != null){
            return new TextComponentTranslation(NAMEPLATE_TRANSLATION_KEY, getCaster().getName(),
                    new TextComponentTranslation("entity." + this.getEntityString() + ".name"));
        }else{
            return super.getDisplayName();
        }
    }

    @Override
    public boolean hasCustomName(){
        // If this returns true, the renderer will show the nameplate when looking directly at the entity
        return Wizardry.settings.summonedCreatureNames && getCaster() != null;
    }
}
