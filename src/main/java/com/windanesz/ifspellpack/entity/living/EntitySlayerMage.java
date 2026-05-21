package com.windanesz.ifspellpack.entity.living;

import com.google.common.base.Predicate;
import com.windanesz.ifspellpack.entity.ai.EntityAIAttackHybrid;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.living.ISpellCaster;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class EntitySlayerMage extends EntityAbstractSlayerMerchant implements ISpellCaster {

	private final EntityAIAttackHybrid<EntitySlayerMage> spellCastingAI = new EntityAIAttackHybrid<>(this, this.getMovementSpeed(), 14.0F, 10, 50);

	private static final DataParameter<Integer> HEAL_COOLDOWN = EntityDataManager.createKey(EntitySlayerMage.class, DataSerializers.VARINT);
	private static final DataParameter<String> CONTINUOUS_SPELL = EntityDataManager.createKey(EntitySlayerMage.class, DataSerializers.STRING);
	private static final DataParameter<Integer> SPELL_COUNTER = EntityDataManager.createKey(EntitySlayerMage.class, DataSerializers.VARINT);

	protected Predicate<Entity> targetSelector;
	protected List<Spell> spells = new ArrayList<>();
	protected float healFactor = 1f;

	public EntitySlayerMage(World worldIn) {
		super(worldIn);
		this.tasks.addTask(3, this.spellCastingAI);
	}

	@Override
	protected void initEntityAI(){
		super.initEntityAI();
		this.targetSelector = entity -> {
			if(entity != null && !entity.isInvisible() && AllyDesignationSystem.isValidTarget(this, entity)) {
				if((entity instanceof IMob && !(entity instanceof ISummonedCreature) || entity instanceof ISummonedCreature && (((ISummonedCreature)entity).getOwner() instanceof IMob || ((ISummonedCreature)entity).getOwner() == this.getRevengeTarget() || ((ISummonedCreature) entity).getOwner() == this.getAttackTarget()) || Arrays.asList(Wizardry.settings.summonedCreatureTargetsWhitelist).contains(EntityList.getKey(entity.getClass()))) && !Arrays.asList(Wizardry.settings.summonedCreatureTargetsBlacklist).contains(EntityList.getKey(entity.getClass()))){
					return true;
				}
			}
			return false;
		};
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(0, new EntityAINearestAttackableTarget<>(this, EntityLiving.class, 0, false, true, this.targetSelector));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(HEAL_COOLDOWN, -1);
		this.dataManager.register(CONTINUOUS_SPELL, "ebwizardry:none");
		this.dataManager.register(SPELL_COUNTER, 0);
	}

	public int getHealCooldown() {
		return this.dataManager.get(HEAL_COOLDOWN);
	}

	public void setHealCooldown(int cooldown) {
		this.dataManager.set(HEAL_COOLDOWN, cooldown);
	}

	@Override
	public void setContinuousSpell(Spell spell) {
		this.dataManager.set(CONTINUOUS_SPELL, spell.getRegistryName().toString());
	}

	@Nonnull
	@Override
	public Spell getContinuousSpell() {
		return Spell.get(this.dataManager.get(CONTINUOUS_SPELL));
	}

	@Override
	public void setSpellCounter(int count) {
		this.dataManager.set(SPELL_COUNTER, count);
	}

	@Override
	public int getSpellCounter() {
		return this.dataManager.get(SPELL_COUNTER);
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		int i = 0;
		if (entityIn instanceof EntityLivingBase) {
			f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
			i += EnchantmentHelper.getKnockbackModifier(this);
		}
		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);
		if (flag) {
			if (i > 0 && entityIn instanceof EntityLivingBase) {
				((EntityLivingBase)entityIn).knockBack(this, (float)i * 0.5F, MathHelper.sin(this.rotationYaw * 0.017453292F), -MathHelper.cos(this.rotationYaw * 0.017453292F));
				this.motionX *= 0.6D;
				this.motionZ *= 0.6D;
			}
			int j = EnchantmentHelper.getFireAspectModifier(this);
			if (j > 0) {
				entityIn.setFire(j * 4);
			}
			if (entityIn instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer)entityIn;
				ItemStack itemstack = this.getHeldItemMainhand();
				ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;
				if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.getItem().isShield(itemstack1, entityplayer)) {
					float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;
					if (this.rand.nextFloat() < f1) {
						entityplayer.getCooldownTracker().setCooldown(itemstack1.getItem(), 100);
						this.world.setEntityState(entityplayer, (byte)30);
					}
				}
			}
			this.applyEnchantments(this, entityIn);
		}
		return flag;
	}

	@Nonnull
	@Override
	public List<Spell> getSpells() {
		return this.spells;
	}

	@Override
	public int getAimingError(EnumDifficulty difficulty){
		// Being more intelligent than skeletons, wizards are a little more accurate.
		switch(difficulty){
			case EASY: return 7;
			case NORMAL: return 4;
			case HARD: return 1;
			default: return 7; // Peaceful counts as easy
		}
	}

	@Override
	public void onLivingUpdate(){
		super.onLivingUpdate();
		this.updateArmSwingProgress();
		// Still better to store this to a local variable as it's almost certainly more efficient.
		int healCooldown = this.getHealCooldown();
		// This is now done slightly differently because isPotionActive doesn't work on client here, meaning that when
		// affected with arcane jammer and healCooldown == 0, whilst the wizard didn't actually heal or play the sound,
		// the particles still spawned, and since healCooldown wasn't reset they spawned every tick until the arcane
		// jammer wore off.
		if (healCooldown == 0 && this.getHealth() < this.getMaxHealth() && this.getHealth() > 0 && !this.isPotionActive(WizardryPotions.arcane_jammer)) {
			this.heal(4 * this.healFactor);
			this.setHealCooldown(-1);
			// deathTime == 0 checks the wizard isn't currently dying
		} else if(healCooldown == -1 && this.deathTime == 0) {
			// Heal particles
			if(world.isRemote) {
				ParticleBuilder.spawnHealParticles(world, this);
			} else {
				if(this.getHealth() < 10) {
					// Wizards heal themseselves more often if they have low health
					this.setHealCooldown(150);
				} else {
					this.setHealCooldown(400);
				}
				this.playSound(Spells.heal.getSounds()[0], 0.7F, rand.nextFloat() * 0.4F + 1.0F);
			}
		}
		if(healCooldown > 0) {
			this.setHealCooldown(healCooldown - 1);
		}
	}

}
