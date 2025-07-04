package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.registry.IFSPPotions;
import electroblob.wizardry.spell.SpellBuff;

public class Dragonhide extends SpellBuff {

	public static final String EFFECT_SCALE = "effect_scale";

	public Dragonhide() {
		super(IFSpellPack.MODID, "dragonhide", 1f, 0.627f, 0.314f, () -> IFSPPotions.DRAGONHIDE);
		this.addProperties(EFFECT_SCALE);
	}

	@Override
	protected int getBonusAmplifier(float potencyModifier) {
		return (int)(super.getBonusAmplifier(potencyModifier) * this.getProperty(EFFECT_SCALE).floatValue());
	}
}
