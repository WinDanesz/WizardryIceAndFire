package com.windanesz.ifspellpack.entity.living;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSoldier;
import com.github.alexthe666.iceandfire.entity.MyrmexHive;
import com.windanesz.ifspellpack.item.ItemCharmVolatileResin;
import com.windanesz.wizardryutils.entity.ai.EntityAIMinionOwnerHurtByTarget;
import com.windanesz.wizardryutils.entity.ai.EntityAIMinionOwnerHurtTarget;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.living.ISummonedCreature;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityMyrmexSoldierMinion extends EntityMyrmexSoldier implements ISummonedCreature {

	private static final DataParameter<Boolean> SPAWN_PARTICLES = EntityDataManager.createKey(EntityMyrmexSoldierMinion.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> EXPLOSIVE = EntityDataManager.createKey(EntityMyrmexSoldierMinion.class, DataSerializers.BOOLEAN);

	private int lifetime = -1;
	private UUID casterUUID;

	public EntityMyrmexSoldierMinion(World worldIn) {
		super(worldIn);
		this.experienceValue = 0;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, true));
		this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityLivingBase.class, 0, false, true, this.getTargetSelector()));
		this.targetTasks.addTask(3, new EntityAIMinionOwnerHurtByTarget(this));
		this.targetTasks.addTask(4, new EntityAIMinionOwnerHurtTarget(this));
	}

	public boolean isExplosive() {
		return this.dataManager.get(EXPLOSIVE);
	}

	public void setExplosive(boolean explosive) {
		this.dataManager.set(EXPLOSIVE, explosive);
	}

	@Override
	public int getGrowthStage() {
		return 2;
	}

	@Override
	public MyrmexHive getHive() {
		return null;
	}

	@Override
	public void setHive(MyrmexHive newHive) {
	}

	@Override
	public boolean isChild() {
		return false;
	}

	@Override
	public boolean shouldHaveNormalAI() {
		return false;
	}

	@Override
	public int getLifetime(){ return lifetime; }

	@Override
	public void setLifetime(int lifetime){ this.lifetime = lifetime; }

	@Override
	public UUID getOwnerId(){ return casterUUID; }

	@Override
	public void setOwnerId(UUID uuid){ this.casterUUID = uuid; }

	@Override
	protected void entityInit(){
		super.entityInit();
		this.dataManager.register(SPAWN_PARTICLES, true);
		this.dataManager.register(EXPLOSIVE, false);
	}

	@Override
	public void onUpdate(){
		super.onUpdate();
		this.updateDelegate();
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
	public boolean processInteract(EntityPlayer player, EnumHand hand){
		return this.interactDelegate(player, hand) || super.processInteract(player, hand);
	}

	@Override
	public void onDeath(DamageSource cause) {
		if (this.isExplosive()) {
			ItemCharmVolatileResin.explode(this);
		}
		super.onDeath(cause);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound){
		super.writeEntityToNBT(nbttagcompound);
		this.writeNBTDelegate(nbttagcompound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound){
		super.readEntityFromNBT(nbttagcompound);
		this.readNBTDelegate(nbttagcompound);
	}

	@Override
	protected int getExperiencePoints(EntityPlayer player){ return 0; }

	@Override
	protected boolean canDropLoot(){ return false; }

	@Override
	protected Item getDropItem(){ return null; }

	@Override
	protected ResourceLocation getLootTable(){ return null; }

	@Override
	public boolean canPickUpLoot(){ return false; }

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
}
