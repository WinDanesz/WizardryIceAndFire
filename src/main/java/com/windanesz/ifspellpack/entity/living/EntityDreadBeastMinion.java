package com.windanesz.ifspellpack.entity.living;

import com.github.alexthe666.iceandfire.entity.EntityDreadBeast;
import com.windanesz.wizardryutils.entity.ai.EntityAIMinionOwnerHurtByTarget;
import com.windanesz.wizardryutils.entity.ai.EntityAIMinionOwnerHurtTarget;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.living.ISummonedCreature;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityDreadBeastMinion extends EntityDreadBeast implements ISummonedCreature {

	private static final DataParameter<Boolean> SPAWN_PARTICLES = EntityDataManager.createKey(EntityDreadBeastMinion.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IS_CONVERT = EntityDataManager.createKey(EntityDreadBeastMinion.class, DataSerializers.BOOLEAN);

	public static final String IS_CONVERT_KEY = "IsConvert";

	private int lifetime = -1;
	private UUID casterUUID;

	public EntityDreadBeastMinion(World world) {
		super(world);
		this.experienceValue = 0;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(SPAWN_PARTICLES, true);
		this.dataManager.register(IS_CONVERT, false);
	}

	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, true));
		this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityLivingBase.class, 0, false, true, this.getTargetSelector()));
		this.targetTasks.addTask(3, new EntityAIMinionOwnerHurtByTarget(this));
		this.targetTasks.addTask(4, new EntityAIMinionOwnerHurtTarget(this));
	}

	@Override
	public boolean isOnSameTeam(Entity entityIn) {
		return isOnScoreboardTeam(entityIn.getTeam());
	}

	@Override public void onKillEntity(EntityLivingBase entityLivingIn) {}

	@Override public int getLifetime() { return lifetime; }

	@Override public void setLifetime(int lifetime) { this.lifetime = lifetime; }

	@Override public UUID getOwnerId() { return casterUUID; }

	@Override public void setOwnerId(UUID uuid) { this.casterUUID = uuid; }

	@Override
	public void setRevengeTarget(EntityLivingBase entity) {
		if(this.shouldRevengeTarget(entity)) super.setRevengeTarget(entity);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.updateDelegate();
	}

	@Override
	public void onSpawn() {
		if(this.dataManager.get(SPAWN_PARTICLES)) this.spawnParticleEffect();
	}

	@Override
	public void onDespawn() {
		this.spawnParticleEffect();
	}

	private void spawnParticleEffect() {
		if(this.world.isRemote){
			for(int i = 0; i < 15; i++){
				this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + this.rand.nextFloat() - 0.5f,
						this.posY + this.rand.nextFloat() * 2, this.posZ + this.rand.nextFloat() - 0.5f, 0, 0, 0);
			}
		}
	}



	@Override
	public boolean hasParticleEffect() {
		//converts dont show particles
		return !this.isConvert();
	}

	public boolean isConvert() {
		return this.dataManager.get(IS_CONVERT);
	}

	public void setConvert(boolean b) {
		this.dataManager.set(IS_CONVERT, b);
	}

	@Override
	public boolean hasAnimation() {
		return this.dataManager.get(SPAWN_PARTICLES) || this.ticksExisted > 20;
	}

	public void hideParticles() {
		this.dataManager.set(SPAWN_PARTICLES, false);
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		//retain the ability to feed converted wolves
		if (this.isConvert()) {
			ItemStack itemstack = player.getHeldItem(hand);
			if (!itemstack.isEmpty()) {
				if (itemstack.getItem() instanceof ItemFood) {
					ItemFood itemfood = (ItemFood) itemstack.getItem();
					if (itemfood.isWolfsFavoriteMeat() && this.getHealth() < this.getMaxHealth()) {
						if (!player.capabilities.isCreativeMode) {
							itemstack.shrink(1);
						}
						this.heal((float) itemfood.getHealAmount(itemstack));
						this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1f, 0.7f);
						return true;
					}
				}
			}
		}
		// In this case, the delegate method determines whether super is called.
		// Rather handily, we can make use of Java's short-circuiting method of evaluating OR statements.
		return this.interactDelegate(player, hand) || super.processInteract(player, hand);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		this.writeNBTDelegate(nbttagcompound);
		nbttagcompound.setBoolean(IS_CONVERT_KEY, this.isConvert());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.readNBTDelegate(nbttagcompound);
		this.setConvert(nbttagcompound.getBoolean(IS_CONVERT_KEY));
	}

	@Override
	public boolean shouldAnimalsFear(Entity entity) {
		return false;
	}

	@Override
	public boolean shouldFear() {
		return false;
	}

	@Override protected int getExperiencePoints(EntityPlayer player) { return 0; }

	@Override protected boolean canDropLoot() { return false; }

	@Override protected Item getDropItem() { return null; }

	@Override protected ResourceLocation getLootTable() { return null; }

	@Override protected boolean canDespawn() {
		return getCaster() == null && getOwnerId() == null;
	}

	@Override
	public boolean getCanSpawnHere() {
		return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
	}

	@Override
	public boolean canAttackClass(Class<? extends EntityLivingBase> entityType) {
		// Returns true unless the given entity type is a flying entity.
		return !EntityFlying.class.isAssignableFrom(entityType);
	}

	@Override
	public ITextComponent getDisplayName() {
		//converts take the name of the previous wolf
		if (this.isConvert()) {
			TextComponentString textcomponentstring = new TextComponentString(ScorePlayerTeam.formatPlayerName(this.getTeam(), this.getName()));
			textcomponentstring.getStyle().setHoverEvent(this.getHoverEvent());
			textcomponentstring.getStyle().setInsertion(this.getCachedUniqueIdString());
			return textcomponentstring;
		}
		else if (getCaster() != null) {
			return new TextComponentTranslation(NAMEPLATE_TRANSLATION_KEY, getCaster().getName(),
					new TextComponentTranslation("entity." + this.getEntityString() + ".name"));
		} else {
			return super.getDisplayName();
		}
	}

	@Override
	public boolean hasCustomName() {
		//converts take the name of the previous wolf
		if (this.isConvert()) {
			return super.hasCustomName();
		} else {
			return Wizardry.settings.summonedCreatureNames && getCaster() != null;
		}
	}
}
