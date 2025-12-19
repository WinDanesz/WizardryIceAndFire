package com.windanesz.ifspellpack.entity.living;

import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.windanesz.ifspellpack.registry.IFSPItems;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.EntityUtils;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class EntityPixieMinion extends EntityPixie implements ISummonedCreature {

	private static final DataParameter<Boolean> SPAWN_PARTICLES = EntityDataManager.createKey(EntityPixieMinion.class, DataSerializers.BOOLEAN);

	private int lifetime = -1;

	public EntityPixieMinion(World worldIn) {
		super(worldIn);
		this.experienceValue = 0;
	}

	@Override
	public int getLifetime(){ return lifetime; }

	@Override
	public void setLifetime(int lifetime){ this.lifetime = lifetime; }

	@Override
	protected void entityInit(){
		super.entityInit();
		this.dataManager.register(SPAWN_PARTICLES, true);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (this.ticksExisted % 80 == 0 && this.getOwner() instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer)this.getOwner(), IFSPItems.CHARM_SOUR_CANDY)) {
			List<EntityLivingBase> entities = EntityUtils.getLivingWithinRadius(10, this.posX, this.posY, this.posZ, this.world);
			entities.removeIf(e -> !AllyDesignationSystem.isValidTarget(this.getOwner(), e));
			for (EntityLivingBase entity : entities) {
				entity.addPotionEffect(new PotionEffect(negativePotions[this.getColor()], 100, 0, false, false));
			}
		}
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

