package com.windanesz.ifspellpack.mixin.minecraft;

import com.windanesz.ifspellpack.potion.PotionDragonhide;
import com.windanesz.ifspellpack.registry.IFSPPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
public class MixinEntityLivingBase {

	@Inject(method = "getTotalArmorValue()I", at = @At("RETURN"), cancellable = true)
	private void injectGetTotalArmorValue(CallbackInfoReturnable<Integer> info) {
		EntityLivingBase entity = (EntityLivingBase)(Object)this;
		PotionEffect effect = entity.getActivePotionEffect(IFSPPotions.DRAGONHIDE);
		if (effect != null) {
			int min = PotionDragonhide.BASE_ARMOR + (PotionDragonhide.ARMOR_PER_LEVEL * effect.getAmplifier());
			info.setReturnValue(Math.max(info.getReturnValue(), min));
		}
	}

}
