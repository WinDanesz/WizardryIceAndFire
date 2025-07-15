package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.registry.IFSPPotions;
import electroblob.wizardry.spell.SpellBuff;

public class SentinelShell extends SpellBuff {

	public SentinelShell() {
		super(IFSpellPack.MODID, "sentinel_shell", 0.49803921f, 0.49803921f, 0.1411764705882353f, () -> IFSPPotions.SENTINEL_SHELL);
	}
}
