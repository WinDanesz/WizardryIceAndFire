package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.registry.IFSPPotions;
import electroblob.wizardry.spell.SpellBuff;

public class TrollSkin extends SpellBuff {

	public static final String EFFECT_SCALE = "effect_scale";
	public static final String DAMAGE_REDUCTION = "damage_reduction";

	public TrollSkin() {
		super(IFSpellPack.MODID, "troll_skin",0.227450980f, 0.274509803f, 0.301960784f, () -> IFSPPotions.TROLL_SKIN);
		this.addProperties(EFFECT_SCALE, DAMAGE_REDUCTION);
	}

	@Override
	protected int getBonusAmplifier(float potencyModifier) {
		return (int)(super.getBonusAmplifier(potencyModifier) * this.getProperty(EFFECT_SCALE).floatValue());
	}

}
