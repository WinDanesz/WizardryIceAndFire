package com.windanesz.ifspellpack.potion;

import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.potion.PotionMagicEffect;
import net.minecraft.util.ResourceLocation;

public class PotionSentinelShell extends PotionMagicEffect {

	public PotionSentinelShell() {
		super(false, 0x7F4124, new ResourceLocation(IFSpellPack.MODID, "textures/potions/sentinel_shell.png"));
	}
}
