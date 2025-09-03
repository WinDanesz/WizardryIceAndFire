package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.registry.IFSPPotions;
import electroblob.wizardry.spell.SpellBuff;

public class Menace extends SpellBuff {

	public static final String EFFECT_SCALE = "effect_scale";

	public Menace() {
		super(IFSpellPack.MODID, "menace", 0.184f, 0.114f, 0.337f, () -> IFSPPotions.MENACE);
		this.addProperties(EFFECT_SCALE);
	}

	@Override
	protected int getBonusAmplifier(float potencyModifier) {
		return (int)(super.getBonusAmplifier(potencyModifier) * this.getProperty(EFFECT_SCALE).floatValue());
	}
}
