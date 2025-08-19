package com.windanesz.ifspellpack.entity.living;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.windanesz.wizardryutils.entity.ai.EntityAIMinionOwnerHurtByTarget;
import com.windanesz.wizardryutils.entity.ai.EntityAIMinionOwnerHurtTarget;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.living.ISummonedCreature;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import java.util.Random;
import java.util.UUID;

public abstract class EntitySkeletalDragon extends EntityDragonBase implements ISummonedCreature {

	private static final DataParameter<Boolean> SPAWN_PARTICLES = EntityDataManager.createKey(EntitySkeletalDragon.class, DataSerializers.BOOLEAN);

	public static final float[] growth_stage_1 = new float[]{1F, 3F};
	public static final float[] growth_stage_2 = new float[]{3F, 7F};
	public static final float[] growth_stage_3 = new float[]{7F, 12.5F};
	public static final float[] growth_stage_4 = new float[]{12.5F, 20F};
	public static final float[] growth_stage_5 = new float[]{20F, 30F};
	private int lifetime = -1;
	private UUID casterUUID;
	private int modelDeadTime;

	public EntitySkeletalDragon(World worldIn, DragonType dragonType) {
		super(worldIn, dragonType, 1, 1 + IceAndFire.CONFIG.dragonAttackDamage, IceAndFire.CONFIG.dragonHealth * 0.04, IceAndFire.CONFIG.dragonHealth, 0.15F, 0.4F);
		this.experienceValue = 0;
		this.setSize(0.78F, 1.2F);
		ANIMATION_SPEAK = Animation.create(20);
		ANIMATION_BITE = Animation.create(35);
		ANIMATION_SHAKEPREY = Animation.create(65);
		ANIMATION_TAILWHACK = Animation.create(40);
		ANIMATION_FIRECHARGE = Animation.create(30);
		ANIMATION_WINGBLAST = Animation.create(50);
		ANIMATION_ROAR = Animation.create(40);
		ANIMATION_EPIC_ROAR = Animation.create(60);
		this.growth_stages = new float[][]{growth_stage_1, growth_stage_2, growth_stage_3, growth_stage_4, growth_stage_5};
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new DragonAIRide<>(this));
		this.tasks.addTask(1, this.aiSit = new EntityAISit(this));
		this.tasks.addTask(2, new DragonAIMate(this, 1.0D));
		this.tasks.addTask(3, new DragonAIReturnToRoost(this));
		this.tasks.addTask(4, new DragonAIEscort(this, 1.5D));
		this.tasks.addTask(5, new DragonAIAttackMelee(this, 1.5D, false));
		this.tasks.addTask(7, new DragonAIWander(this, 1.0D));
		this.tasks.addTask(8, new DragonAIWatchClosest(this, EntityLivingBase.class, 6.0F));
		this.tasks.addTask(8, new DragonAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityLivingBase.class, 0, false, true, this.getTargetSelector()));
		this.targetTasks.addTask(3, new EntityAIMinionOwnerHurtByTarget(this));
		this.targetTasks.addTask(4, new EntityAIMinionOwnerHurtTarget(this));
	}

	@Override
	public int getLifetime() {
		return this.lifetime;
	}

	@Override
	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}

	@Override
	public UUID getOwnerId() {
		return this.casterUUID;
	}

	@Override
	public void setOwnerId(UUID uuid) {
		this.casterUUID = uuid;
	}

	public int getModelDeadTime() {
		return this.modelDeadTime;
	}

	public void setModelDeadTime(int i) {
		this.modelDeadTime = i;
	}

	@Override
	protected void entityInit(){
		super.entityInit();
		this.dataManager.register(SPAWN_PARTICLES, true);
	}

	@Override
	public void onUpdate(){
		super.onUpdate();
		this.updateDelegate();
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!world.isRemote && this.getAttackTarget() != null) {
			if (this.getEntityBoundingBox().grow(2.5F + this.getRenderSize() * 0.33F, 2.5F + this.getRenderSize() * 0.33F, 2.5F + this.getRenderSize() * 0.33F).intersects(this.getAttackTarget().getEntityBoundingBox())) {
				attackEntityAsMob(this.getAttackTarget());
			}
		}
	}

	@Override
	public void onSpawn(){
		if(this.dataManager.get(SPAWN_PARTICLES)) this.spawnParticleEffect();
	}

	@Override
	public void onDespawn(){
		this.spawnParticleEffect();
	}

	private void spawnParticleEffect(){
		if(this.world.isRemote){
			for(int i = 0; i < 15; i++){
				this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + this.rand.nextFloat() - 0.5f,
						this.posY + this.rand.nextFloat() * 2, this.posZ + this.rand.nextFloat() - 0.5f, 0, 0, 0);
			}
		}
	}

	@Override
	public boolean hasParticleEffect(){
		return true;
	}

	@Override
	public boolean hasAnimation(){
		return this.dataManager.get(SPAWN_PARTICLES) || this.ticksExisted > 20;
	}

	public void hideParticles(){
		this.dataManager.set(SPAWN_PARTICLES, false);
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		return this.interactDelegate(player, hand);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		this.writeNBTDelegate(nbttagcompound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound){
		super.readEntityFromNBT(nbttagcompound);
		this.readNBTDelegate(nbttagcompound);
	}

	@Override
	protected int getExperiencePoints(EntityPlayer player) {
		return 0;
	}

	@Override
	protected boolean canDropLoot() {
		return false;
	}

	@Override
	protected Item getDropItem() {
		return null;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return null;
	}

	@Override
	public boolean canPickUpLoot() {
		return false;
	}


	@Override protected boolean canDespawn(){
		return getCaster() == null && getOwnerId() == null;
	}

	@Override
	public boolean getCanSpawnHere(){
		return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
	}

	@Override
	public boolean canAttackClass(Class<? extends EntityLivingBase> entityType){
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
		return Wizardry.settings.summonedCreatureNames && getCaster() != null;
	}

	public String getVariantName(int variant) {
		return "";
	}

	@Override
	protected void breathFireAtPos(BlockPos burningTarget) {
	}

	@Override
	protected Item getHeartItem() {
		return null;
	}

	@Override
	protected Item getBloodItem() {
		return null;
	}

	@Override
	public ResourceLocation getDeadLootTable() {
		return null;
	}

	@Override
	public Item getVariantScale(int variant) {
		return null;
	}

	@Override
	public Item getVariantEgg(int variant) {
		return null;
	}

	@Override
	public Item getSummoningCrystal() {
		return null;
	}

	@Override
	public void stimulateFire(double burnX, double burnY, double burnZ, int syncType) {
	}

	@Override
	public void tryScorchTarget() {
	}

	@Override
	public boolean isSkeletal() {
		return true;
	}

	@Override
	public boolean isFlying() {
		return false;
	}

	@Override
	public void setFlying(boolean flying) {
	}

	@Override
	public boolean useFlyingPathFinder() {
		return false;
	}

	@Override
	public boolean canMateWith(EntityAnimal otherAnimal) {
		return false;
	}

	@Override
	public boolean isAgingDisabled() {
		return true;
	}

	@Override
	public boolean isSleeping() {
		return false;
	}

	@Override
	public void breakBlock() {
	}

	@Override
	public boolean isAllowedToTriggerFlight() {
		return false;
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}

	@Override
	public Animation[] getAnimations() {
		return new Animation[]{IAnimatedEntity.NO_ANIMATION, EntityDragonBase.ANIMATION_EAT, EntityDragonBase.ANIMATION_SPEAK, EntityDragonBase.ANIMATION_BITE, EntityDragonBase.ANIMATION_SHAKEPREY, EntityLightningDragon.ANIMATION_TAILWHACK, EntityLightningDragon.ANIMATION_FIRECHARGE, EntityLightningDragon.ANIMATION_WINGBLAST, EntityLightningDragon.ANIMATION_ROAR, EntityLightningDragon.ANIMATION_EPIC_ROAR};
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		this.getLookHelper().setLookPositionWithEntity(entityIn, 30.0F, 30.0F);
		if(!this.isPlayingAttackAnimation()){
			switch (groundAttack) {
				case BITE:
					this.setAnimation(ANIMATION_BITE);
					break;
				case TAIL_WHIP:
					this.setAnimation(ANIMATION_TAILWHACK);
					break;
				case SHAKE_PREY:
					boolean flag = false;
					if (new Random().nextInt(2) == 0 && isDirectPathBetweenPoints(this, this.getPositionVector().add(0, this.height/2, 0), entityIn.getPositionVector().add(0, entityIn.height/2, 0)) &&
							entityIn.width < this.width * 0.5F && this.getControllingPassenger() == null && this.getDragonStage() > 1 && !(entityIn instanceof EntityDragonBase) && !DragonUtils.isAnimaniaMob(entityIn)) {
						this.setAnimation(ANIMATION_SHAKEPREY);
						flag = true;
						entityIn.startRiding(this);
					}
					if(!flag){
						groundAttack = IafDragonAttacks.Ground.BITE;
						this.setAnimation(ANIMATION_BITE);
					}
					break;
				case WING_BLAST:
					this.setAnimation(ANIMATION_WINGBLAST);
					break;
				default:
					break;
			}
		}
		return false;
	}

	//override EntityDragonBase to remove the carcass after a short while
	@Override
	protected void onDeathUpdate() {
		if (!this.isModelDead()) {
			this.setModelDead(true);
		}
		this.setModelDeadTime(this.getModelDeadTime() + 1);
		if (this.getModelDeadTime() > 50) {
			this.setDead();
			for (int k = 0; k < 40; ++k) {
				double d2 = this.rand.nextGaussian() * 0.02D;
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				if (world.isRemote) {
					this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d2, d0, d1);
				}
			}
		}
	}

	//expose the method as public
	@Override
	public void updateAttributes() {
		super.updateAttributes();
	}
}
