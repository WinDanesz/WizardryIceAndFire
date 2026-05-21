package com.windanesz.ifspellpack.entity.living;

import com.github.alexthe666.iceandfire.entity.DragonUtils;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.google.common.base.Predicate;
import com.windanesz.wizardryutils.entity.ai.EntityAIMinionOwnerHurtByTarget;
import com.windanesz.wizardryutils.entity.ai.EntityAIMinionOwnerHurtTarget;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.living.ISummonedCreature;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class EntityFireDragonMinion extends EntityFireDragon implements ISummonedCreature {

	public static final String LIFETIME_KEY = "Lifetime";
	public static final String PLAYER_SUMMONED_KEY = "PlayerSummoned";
	public static final String MODEL_DEAD_TIME_KEY = "ModelDeadTime";
	public static final String SHOULD_DESPAWN_KEY = "ShouldDespawn";

	private int lifetime = -1;
	private boolean playerSummoned = false;
	private int modelDeadTime;
	private boolean shouldDespawn;

	public EntityFireDragonMinion(World worldIn) {
		super(worldIn);
		this.setCommand(2);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.targetTasks.taskEntries.clear();
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityLivingBase.class, 0, false, true, this.getTargetSelector()));
		this.targetTasks.addTask(3, new EntityAIMinionOwnerHurtByTarget(this));
		this.targetTasks.addTask(4, new EntityAIMinionOwnerHurtTarget(this));
	}

	@Override
	public Predicate<Entity> getTargetSelector() {
		if (this.isPlayerSummoned()) {
			return ISummonedCreature.super.getTargetSelector();
		} else {
			return entity -> isValidTarget(entity) && entity instanceof EntityLivingBase && DragonUtils.canHostilesTarget(entity) && !(entity instanceof EntityPlayer && ((EntityPlayer)entity).isCreative());
		}
	}

	@Override public int getLifetime() {
		return this.lifetime;
	}

	@Override public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}

	public boolean isPlayerSummoned() {
		return this.playerSummoned;
	}

	public void setPlayerSummoned(boolean playerSummoned) {
		this.playerSummoned = playerSummoned;
	}

	public int getModelDeadTime() {
		return this.modelDeadTime;
	}

	public void setModelDeadTime(int modelDeadTime) {
		this.modelDeadTime = modelDeadTime;
	}

	public boolean getShouldDespawn() {
		return this.shouldDespawn;
	}

	public void setShouldDespawn(boolean shouldDespawnWithoutOwner) {
		this.shouldDespawn = shouldDespawnWithoutOwner;
	}

	@Override
	protected boolean canDespawn() {
		if (this.getShouldDespawn() && !this.isModelDead()) {
			return true;
		}
		return super.canDespawn();
	}

	@Override
	public void setRevengeTarget(EntityLivingBase entity){
		if(this.shouldRevengeTarget(entity)) super.setRevengeTarget(entity);
	}

	@Override
	public void onUpdate(){
		super.onUpdate();
		if (this.isPlayerSummoned()) {
			this.updateDelegate();
		}
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		if (this.isPlayerSummoned()) {
			return this.interactDelegate(player, hand);
		} else {
			return super.processInteract(player, hand);
		}
	}

	@Override
	public Item getSummoningCrystal() {
		return null;
	}

	@Override
	public void onSpawn() {
		this.spawnParticleEffect();
	}

	@Override
	public void onDespawn() {
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
	public boolean hasParticleEffect() {
		return this.isPlayerSummoned();
	}

	@Override
	public ITextComponent getDisplayName() {
		if(getCaster() != null){
			return new TextComponentTranslation(NAMEPLATE_TRANSLATION_KEY, getCaster().getName(),
					new TextComponentTranslation("entity." + this.getEntityString() + ".name"));
		}else{
			return super.getDisplayName();
		}
	}

	@Override
	public boolean hasCustomName() {
		return Wizardry.settings.summonedCreatureNames && getCaster() != null;
	}

	@Override
	protected void onDeathUpdate() {
		if (this.isPlayerSummoned()) {
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
		} else {
			super.onDeathUpdate();
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound){
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setInteger(LIFETIME_KEY, this.getLifetime());
		nbttagcompound.setBoolean(PLAYER_SUMMONED_KEY, this.isPlayerSummoned());
		nbttagcompound.setInteger(MODEL_DEAD_TIME_KEY, this.getModelDeadTime());
		nbttagcompound.setBoolean(SHOULD_DESPAWN_KEY, this.getShouldDespawn());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound){
		super.readEntityFromNBT(nbttagcompound);
		this.setLifetime(nbttagcompound.getInteger(LIFETIME_KEY));
		this.setPlayerSummoned(nbttagcompound.getBoolean(PLAYER_SUMMONED_KEY));
		this.setModelDeadTime(nbttagcompound.getInteger(MODEL_DEAD_TIME_KEY));
		this.setShouldDespawn(nbttagcompound.getBoolean(SHOULD_DESPAWN_KEY));
	}
}
