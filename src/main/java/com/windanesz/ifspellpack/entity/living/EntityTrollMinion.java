package com.windanesz.ifspellpack.entity.living;

import com.github.alexthe666.iceandfire.entity.EntityTroll;
import com.github.alexthe666.iceandfire.entity.IHumanoid;
import com.github.alexthe666.iceandfire.entity.IVillagerFear;
import com.github.alexthe666.iceandfire.entity.ai.TrollAIFleeSun;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.windanesz.wizardryutils.entity.ai.EntityAIMinionOwnerHurtByTarget;
import com.windanesz.wizardryutils.entity.ai.EntityAIMinionOwnerHurtTarget;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.living.ISummonedCreature;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityTrollMinion extends EntityTroll implements ISummonedCreature, IAnimatedEntity, IVillagerFear, IHumanoid {

	private int lifetime = -1;
	private UUID casterUUID;

	private static final DataParameter<Boolean> SUNIMMUNE = EntityDataManager.createKey(EntityTrollMinion.class, DataSerializers.BOOLEAN);

	public EntityTrollMinion(World worldIn) {
		super(worldIn);
		this.setSize(1.2F, 3.5F);
		this.setType(EnumTroll.values()[this.rand.nextInt(EnumTroll.values().length)]);
		this.setWeaponType(EnumTroll.getWeaponForType(this.getType()));
	}

	protected void initEntityAI() {
		if (!isSunlightImmune()) {
			super.initEntityAI();
			this.targetTasks.taskEntries.clear();
			this.tasks.taskEntries.clear();
		}

		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new TrollAIFleeSun(this, 1.0D));
		this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.0D, true));
		this.tasks.addTask(4, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F, 1.0F));
		this.tasks.addTask(5, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityVillager>(this, EntityVillager.class, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, false));

		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityLivingBase.class, 0, false, true, this.getTargetSelector()));
		this.targetTasks.addTask(3, new EntityAIMinionOwnerHurtByTarget(this));
		this.targetTasks.addTask(4, new EntityAIMinionOwnerHurtTarget(this));
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.updateDelegate();
	}

	@Override
	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}

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

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(SUNIMMUNE, false);
	}

	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		return this.interactDelegate(player, hand) || super.processInteract(player, hand);
	}

	// Recommended overrides

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

	@Override
	public void onDeath(DamageSource cause) {

	}

	// This vanilla method has nothing to do with the custom despawn() method.
	@Override
	protected boolean canDespawn() {
		return true; //() == null && getOwnerId() == null;
	}

	@Override
	public boolean getCanSpawnHere() {
		return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
	}

	@Override
	public ITextComponent getDisplayName() {
		if (getCaster() != null) {
			return new TextComponentTranslation(NAMEPLATE_TRANSLATION_KEY, getCaster().getName(),
					new TextComponentTranslation("entity." + this.getEntityString() + ".name"));
		} else {
			return super.getDisplayName();
		}
	}

	@Override
	public boolean hasCustomName() {
		// If this returns true, the renderer will show the nameplate when looking directly at the entity
		return Wizardry.settings.summonedCreatureNames && getCaster() != null;
	}

	public void setSunlightImmune(boolean b) {
		this.dataManager.set(SUNIMMUNE, b);
	}

	public boolean isSunlightImmune() {
		return this.dataManager.get(SUNIMMUNE);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		writeNBTDelegate(compound);
		compound.setBoolean("SunImmune", this.dataManager.get(SUNIMMUNE));
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		readNBTDelegate(compound);
		this.setSunlightImmune(compound.getBoolean("SunImmune"));
		super.readFromNBT(compound);
	}

}
