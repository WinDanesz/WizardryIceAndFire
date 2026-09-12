package com.windanesz.ifspellpack.mixin.minecraft;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.windanesz.ifspellpack.mixin.modrefs.VanillaMixinHelper;
import com.windanesz.ifspellpack.potion.PotionMyrmexBlessing;
import com.windanesz.ifspellpack.registry.IFSPPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer {

	@ModifyExpressionValue(method = "attackTargetEntityWithCurrentItem(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/attributes/IAttributeInstance;getAttributeValue()D"))
	private double redirectAttribute(double damage, Entity entity) {
		EntityPlayer player = (EntityPlayer)(Object)this;
		if (player.isPotionActive(IFSPPotions.MYRMEX_BLESSING)) {
			if (entity instanceof EntityLivingBase) {
				EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
				if (entityLivingBase.getCreatureAttribute() != EnumCreatureAttribute.ARTHROPOD || entityLivingBase instanceof EntityDeathWorm) {
					damage += (player.getActivePotionEffect(IFSPPotions.MYRMEX_BLESSING).getAmplifier() + 1) * PotionMyrmexBlessing.DAMAGE_INCREASE;
				}
			}
		}
		return damage;
	}

/*	@Redirect(method = "attackTargetEntityWithCurrentItem(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/attributes/IAttributeInstance;getAttributeValue()D"))
	private double redirectAttribute(IAttributeInstance instance, Entity entity) {
		return VanillaMixinHelper.applyMyrmexBlessing((EntityPlayer) (Object)this, instance, entity);
	}*/

}
