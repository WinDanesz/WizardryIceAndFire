package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.spell.SpellBuff;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class HydrasHeartbeat extends SpellBuff {

	public static final Potion POTION = MobEffects.REGENERATION;

	public HydrasHeartbeat() {
		super(IFSpellPack.MODID, "hydras_heartbeat", 0.43137254f, 0.43137254f, 0f, ()-> POTION);
	}

	@Override
	protected boolean applyEffects(EntityLivingBase caster, SpellModifiers modifiers) {
		double healthPercentage = caster.getHealth() / Math.max(1, caster.getMaxHealth());
		if(healthPercentage < 1.0D /*&& caster.getHealth() > 0 maybe leave this out in case there's a funny interaction?*/){
			int level = this.getProperty(getStrengthKey(POTION)).intValue();
			if(healthPercentage < 0.25D){
				level += 3;
			} else if(healthPercentage < 0.5D){
				level += 2;
			} else if(healthPercentage < 0.75D){
				level += 1;
			}
			caster.addPotionEffect(new PotionEffect(POTION, this.getProperty(getDurationKey(POTION)).intValue(), level, true, false));
			return true;
		}
		return false;
	}
}

