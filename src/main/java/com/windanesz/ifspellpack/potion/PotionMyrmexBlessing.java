package com.windanesz.ifspellpack.potion;

import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.potion.PotionMagicEffect;
import net.minecraft.util.ResourceLocation;

public class PotionMyrmexBlessing extends PotionMagicEffect {

	public static final float DAMAGE_INCREASE = 4f;

	public PotionMyrmexBlessing() {
		super(false, 0x7F4124, new ResourceLocation(IFSpellPack.MODID, "textures/potions/myrmex_blessing.png"));
	}
}
