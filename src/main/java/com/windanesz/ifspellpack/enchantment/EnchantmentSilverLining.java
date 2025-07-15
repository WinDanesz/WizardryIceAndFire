package com.windanesz.ifspellpack.enchantment;

import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.enchantment.EnchantmentTimed;

public class EnchantmentSilverLining extends EnchantmentTimed {

	public static final float DAMAGE_INCREASE = 0.25f;

	public EnchantmentSilverLining() {
		this.setRegistryName(IFSpellPack.MODID, "silver_lining");
	}

}
