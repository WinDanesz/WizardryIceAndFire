package com.windanesz.ifspellpack.potion;

import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.potion.ISyncedPotion;
import electroblob.wizardry.potion.PotionMagicEffect;
import net.minecraft.util.ResourceLocation;

public class PotionAllure extends PotionMagicEffect implements ISyncedPotion {

	public static final String UUID_KEY = "ifspellpack:AllurerUUID";

	public PotionAllure() {
		super(true, 0xFF0000, new ResourceLocation(IFSpellPack.MODID,"textures/potions/allure.png"));
	}

}
