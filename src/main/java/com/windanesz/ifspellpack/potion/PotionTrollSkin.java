package com.windanesz.ifspellpack.potion;

import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.potion.PotionMagicEffect;
import net.minecraft.util.ResourceLocation;

public class PotionTrollSkin extends PotionMagicEffect {

	public PotionTrollSkin() {
		super(false, 0x3A464D, new ResourceLocation(IFSpellPack.MODID, "textures/potions/troll_skin.png"));
	}

}
