package com.windanesz.ifspellpack.entity.living;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.windanesz.ifspellpack.entity.SlayerMerchantTrade;
import com.windanesz.ifspellpack.entity.SlayerMerchantTradeList;
import com.windanesz.ifspellpack.registry.IFSPItems;
import com.windanesz.ifspellpack.registry.IFSPSpells;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.item.ItemWand;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.WandHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class EntityLightwardBattlemage extends EntityMageSlayerMerchant {


	public EntityLightwardBattlemage(World worldIn) {
		super(worldIn);
		//Lightward battlemages heal twice as much
		this.healFactor = 2f;
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(0, new EntityAIAttackMelee(this, 0.6, true));
	}

	@Override
	public SlayerMerchantTradeList initializeTrades() {
		SlayerMerchantTradeList trades = new SlayerMerchantTradeList();
		trades.add(new SlayerMerchantTrade(new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.SILVER_LINING.metadata()), 8 + this.getRNG().nextInt(4)));
		trades.add(new SlayerMerchantTrade(new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.LIGHTWARD_ARMAMENT.metadata()), 16 + this.getRNG().nextInt(8)));
		trades.add(new SlayerMerchantTrade(new ItemStack(IFSPItems.AMULET_LIGHTWARD), 25 + this.getRNG().nextInt(5)));
		Collections.shuffle(trades);
		return trades;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30);

	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.updateArmSwingProgress();
	}

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

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		this.spells.add(IFSPSpells.SILVER_LINING);
		this.spells.add(Spells.celestial_smite);
		this.spells.add(Spells.radiant_totem);
		this.spells.add(Spells.healing_aura);
		this.spells.add(Spells.empowering_presence);
		ItemStack wand = new ItemStack(ItemWand.getWand(Tier.MASTER, Element.HEALING));
		WandHelper.setSpells(wand, this.spells.toArray(new Spell[0]));
		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, wand);
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(IafItemRegistry.silver_helmet));
		this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(IafItemRegistry.silver_chestplate));
		this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(IafItemRegistry.silver_leggings));
		this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(IafItemRegistry.silver_boots));
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(IafItemRegistry.silver_sword));
		for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
			this.setDropChance(slot, 0.0f);
		}
		this.setHealCooldown(50);
		return super.onInitialSpawn(difficulty, livingdata);
	}
}
