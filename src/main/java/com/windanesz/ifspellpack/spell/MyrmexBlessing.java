package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.registry.IFSPPotions;
import electroblob.wizardry.spell.SpellBuff;

public class MyrmexBlessing extends SpellBuff {

	public MyrmexBlessing() {
		super(IFSpellPack.MODID, "myrmex_blessing", 0.49803921f, 0.49803921f, 0.1411764705882353f, () -> IFSPPotions.MYRMEX_BLESSING);
	}
}
