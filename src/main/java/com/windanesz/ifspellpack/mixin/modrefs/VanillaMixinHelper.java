package com.windanesz.ifspellpack.mixin.modrefs;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.windanesz.ifspellpack.potion.PotionMyrmexBlessing;
import com.windanesz.ifspellpack.registry.IFSPItems;
import com.windanesz.ifspellpack.registry.IFSPPotions;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;

public class VanillaMixinHelper {

	public static double applyMyrmexBlessing(EntityPlayer player, IAttributeInstance instance, Entity entity) {
		double d = 0;
		if (player.isPotionActive(IFSPPotions.MYRMEX_BLESSING)) {
			if (entity instanceof EntityLivingBase) {
				EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
				if (entityLivingBase.getCreatureAttribute() != EnumCreatureAttribute.ARTHROPOD || entityLivingBase instanceof EntityDeathWorm) {
					d += (player.getActivePotionEffect(IFSPPotions.MYRMEX_BLESSING).getAmplifier() + 1) * PotionMyrmexBlessing.DAMAGE_INCREASE;
				}
			}
		}
		return instance.getAttributeValue() + d;
	}

	public static boolean hasArtefact(EntityPlayer player) {
		return ItemArtefact.isArtefactActive(player, IFSPItems.CHARM_DRAGON_EYE);
	}
}
