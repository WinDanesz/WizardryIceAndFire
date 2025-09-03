package com.windanesz.ifspellpack.potion;

import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.potion.PotionMagicEffect;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.spell.Intimidate;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.EntityUtils;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class PotionMenace extends PotionMagicEffect {

	public static final double MENACE_RANGE = 15;
	public static final double FEAR_RANGE = 5;
	public static final int DURATION = 5;

	public PotionMenace() {
		super(false, 0x2F1D56, new ResourceLocation(IFSpellPack.MODID, "textures/potions/menace.png"));
	}

	@Override
	public void performEffect(EntityLivingBase entitylivingbase, int strength) {
		List<EntityLivingBase> targets = EntityUtils.getLivingWithinRadius(MENACE_RANGE, entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, entitylivingbase.world);
		//Because sheep like cyclopes and are dumb
		targets.removeIf(target -> !AllyDesignationSystem.isValidTarget(entitylivingbase, target) || target instanceof EntitySheep);
		for (EntityLivingBase target : targets) {
			target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, DURATION, strength));
			target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, DURATION, strength));
			if (target instanceof EntityCreature && target.getDistance(entitylivingbase) <= FEAR_RANGE) {
				target.addPotionEffect(new PotionEffect(WizardryPotions.fear, DURATION, strength));
				target.getEntityData().setUniqueId(Intimidate.NBT_KEY, entitylivingbase.getUniqueID());
			}
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
}


