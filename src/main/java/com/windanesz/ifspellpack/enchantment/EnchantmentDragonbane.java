package com.windanesz.ifspellpack.enchantment;

import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.enchantment.EnchantmentTimed;

public class EnchantmentDragonbane extends EnchantmentTimed {

	public static final String DRAGONBANE_KEY = "ifspellpack:dragonbane";
	public static final float DRAGONBANE_DAMAGE_INCREASE = 0.15f;
	public static final float DRAGONBANE_VELOCITY_INCREASE = 0.15f;

	public EnchantmentDragonbane() {
		this.setRegistryName(IFSpellPack.MODID, "dragonbane");
	}

}
