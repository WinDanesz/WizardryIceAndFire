package com.windanesz.ifspellpack.potion;

import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.potion.PotionMagicEffect;
import net.minecraft.util.ResourceLocation;

public class PotionDragonhide extends PotionMagicEffect {

	public static final int BASE_ARMOR = 8;
	public static final int ARMOR_PER_LEVEL = 4;

	public PotionDragonhide() {
		super(false, 0xFFA050, new ResourceLocation(IFSpellPack.MODID,"textures/potions/dragonhide.png"));
	}

}
