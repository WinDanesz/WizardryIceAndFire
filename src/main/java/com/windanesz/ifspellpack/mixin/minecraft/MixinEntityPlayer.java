package com.windanesz.ifspellpack.mixin.minecraft;

import com.windanesz.ifspellpack.mixin.modrefs.VanillaMixinHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer {

	@Redirect(method = "attackTargetEntityWithCurrentItem(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/attributes/IAttributeInstance;getAttributeValue()D"))
	private double redirectAttribute(IAttributeInstance instance, Entity entity) {
		return VanillaMixinHelper.applyMyrmexBlessing((EntityPlayer) (Object)this, instance, entity);
	}
}
