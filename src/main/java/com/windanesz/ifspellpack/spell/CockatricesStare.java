package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.registry.IFSPItems;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.spell.SpellBuff;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CockatricesStare extends Spell {

	public static final String DAMAGE_SCALE = "damage_scale";
	public static final String VIEW_RADIUS = "view_radius";

	public CockatricesStare() {
		super(IFSpellPack.MODID, "cockatrices_stare", SpellActions.IMBUE, true);
		this.addProperties(DAMAGE_SCALE, VIEW_RADIUS);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (!EntityGorgon.isBlindfolded(caster)) {
			int baseDamage = SpellBuff.getStandardBonusAmplifier(modifiers.get(SpellModifiers.POTENCY));
			float damageScale = this.getProperty(DAMAGE_SCALE).floatValue();
			double range = (double) IceAndFire.CONFIG.cockatriceChickenSearchLength * modifiers.get(WizardryItems.range_upgrade);
			float view_radius = this.getProperty(VIEW_RADIUS).floatValue();
			List<EntityLivingBase> targets = EntityUtils.getLivingWithinRadius(range, caster.posX, caster.posY, caster.posZ, world);
			List<EntityPlayer> allies = new ArrayList<>();
			//placeholder artefact
			if (ItemArtefact.isArtefactActive(caster, IFSPItems.HEAD_COCKATRICES_COMB)) {
				for (EntityLivingBase entityLivingBase : targets) {
					if (entityLivingBase instanceof EntityPlayer && AllyDesignationSystem.isPlayerAlly(caster, (EntityPlayer)entityLivingBase)) {
						allies.add((EntityPlayer)entityLivingBase);
					}
				}
			}
			targets.removeIf(e -> e == caster || !EntityGorgon.isEntityLookingAt(caster, e, view_radius) || !EntityGorgon.isEntityLookingAt(e, caster, view_radius) || !EntityUtils.isLiving(e) || EntityGorgon.isBlindfolded(e) || !AllyDesignationSystem.isValidTarget(caster, e));
			for (EntityLivingBase target : targets) {
				if (!world.isRemote) {
					int attackStrength = (int)((baseDamage + this.getFriendsCount(allies, target)) * damageScale);
					target.addPotionEffect(new PotionEffect(MobEffects.WITHER, 10, 2 + Math.min(1, attackStrength)));
					target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 10, Math.min(4, attackStrength)));
					target.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 0));
					if (attackStrength >= 2 && target.ticksExisted % 40 == 0) {
						target.attackEntityFrom(DamageSource.WITHER, attackStrength - 1);
					}
				} else {
					double d5 = this.getAttackAnimationScale(ticksInUse);
					double d0 = target.posX - caster.posX;
					double d1 = target.posY + (double) (target.height * 0.5F) - (caster.posY + (double) caster.getEyeHeight());
					double d2 = target.posZ - caster.posZ;
					double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
					d0 = d0 / d3;
					d1 = d1 / d3;
					d2 = d2 / d3;
					double d4 = world.rand.nextDouble();
					while (d4 < d3) {
						d4 += 1.8D - d5 + world.rand.nextDouble() * (1.7D - d5);
						world.spawnParticle(EnumParticleTypes.SPELL_MOB, caster.posX + d0 * d4, caster.posY + d1 * d4 + (double) caster.getEyeHeight(), caster.posZ + d2 * d4, 0.0D, 0.0D, 0.0D, 3484199);
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		if (!EntityGorgon.isBlindfolded(caster)) {
			int baseDamage = SpellBuff.getStandardBonusAmplifier(modifiers.get(SpellModifiers.POTENCY));
			float damageScale = this.getProperty(DAMAGE_SCALE).floatValue();
			double range = (double) IceAndFire.CONFIG.cockatriceChickenSearchLength * modifiers.get(WizardryItems.range_upgrade);
			float view_radius = this.getProperty(VIEW_RADIUS).floatValue();
			List<EntityLivingBase> targets = EntityUtils.getLivingWithinRadius(range, caster.posX, caster.posY, caster.posZ, world);
			targets.removeIf(e -> e == caster || !EntityGorgon.isEntityLookingAt(caster, e, view_radius) || !EntityGorgon.isEntityLookingAt(e, caster, view_radius) || !EntityUtils.isLiving(e) || EntityGorgon.isBlindfolded(e) || !AllyDesignationSystem.isValidTarget(caster, e));
			for (EntityLivingBase target1 : targets) {
				if (!world.isRemote) {
					int attackStrength = (int)(baseDamage * damageScale);
					target1.addPotionEffect(new PotionEffect(MobEffects.WITHER, 10, 2 + Math.min(1, attackStrength)));
					target1.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 10, Math.min(4, attackStrength)));
					target1.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 0));
					if (attackStrength >= 2 && target1.ticksExisted % 40 == 0) {
						target1.attackEntityFrom(DamageSource.WITHER, attackStrength - 1);
					}
				} else {
					double d5 = this.getAttackAnimationScale(ticksInUse);
					double d0 = target1.posX - caster.posX;
					double d1 = target1.posY + (double) (target1.height * 0.5F) - (caster.posY + (double) caster.getEyeHeight());
					double d2 = target1.posZ - caster.posZ;
					double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
					d0 = d0 / d3;
					d1 = d1 / d3;
					d2 = d2 / d3;
					double d4 = world.rand.nextDouble();
					while (d4 < d3) {
						d4 += 1.8D - d5 + world.rand.nextDouble() * (1.7D - d5);
						world.spawnParticle(EnumParticleTypes.SPELL_MOB, caster.posX + d0 * d4, caster.posY + d1 * d4 + (double) caster.getEyeHeight(), caster.posZ + d2 * d4, 0.0D, 0.0D, 0.0D, 3484199);
					}
				}
			}
		}
		return true;
	}

	public float getAttackAnimationScale(int ticksInUse) {
		return (float)ticksInUse / 80f;
	}

	public int getFriendsCount(List<EntityPlayer> allies, EntityLivingBase target) {
		int i = 0;
		float view_radius = this.getProperty(VIEW_RADIUS).floatValue();
		for (EntityPlayer ally : allies) {
			if (EntityGorgon.isEntityLookingAt(ally, target, view_radius) && EntityGorgon.isEntityLookingAt(target, ally, view_radius) && !EntityGorgon.isBlindfolded(ally)) {
				i++;
			}
		}
		return i;
	}
}
