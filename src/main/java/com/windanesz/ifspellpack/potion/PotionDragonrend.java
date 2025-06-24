package com.windanesz.ifspellpack.potion;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.potion.PotionMagicEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class PotionDragonrend extends PotionMagicEffect {

	public static final float DAMAGE_INCREASE = IFSpellPack.settings.dragonrendBonusDamage;

	public PotionDragonrend() {
		super(true, 0xC2FFFF, new ResourceLocation(IFSpellPack.MODID, "textures/potions/dragonrend.png"));
	}

	@Override
	public void performEffect(EntityLivingBase entityLivingBase, int strength) {
		if (entityLivingBase instanceof EntityDragonBase) {
			EntityDragonBase dragon = (EntityDragonBase)entityLivingBase;
			dragon.down(true);
			dragon.setFlying(false);
			dragon.setHovering(false);
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

}
