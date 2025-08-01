package com.windanesz.ifspellpack.item;

import com.windanesz.ifspellpack.registry.IFSPItems;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.spell.SpellBuff;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;

import javax.annotation.Nullable;

public class ItemCharmLoversHeart extends ItemArtefactIFSP {

	public static final double DAMAGE_REDUCTION = 0.15f;

	public ItemCharmLoversHeart() {
		super(EnumRarity.RARE, Type.CHARM);
	}

	public static int getAmplifier(@Nullable EntityLivingBase caster, SpellModifiers modifiers) {
		int amplifier = 0;
		if (caster instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)caster;
			if (ItemArtefact.isArtefactActive(player, IFSPItems.CHARM_LOVER_HEART)) {
				amplifier = 1 + SpellBuff.getStandardBonusAmplifier(modifiers.get(SpellModifiers.POTENCY));
			}
		}
		return amplifier;
	}

	public static float damageMultiplier(int amplifier) {
		return (float)Math.pow(1 - DAMAGE_REDUCTION, amplifier);
	}

}
