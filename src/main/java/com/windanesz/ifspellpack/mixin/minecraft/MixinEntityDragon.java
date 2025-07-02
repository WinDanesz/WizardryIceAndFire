package com.windanesz.ifspellpack.mixin.minecraft;

import com.windanesz.ifspellpack.registry.IFSPPotions;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityDragon.class)
public class MixinEntityDragon extends EntityLiving {

	public MixinEntityDragon(World worldIn) {
		super(worldIn);
	}

	@Inject(method = "addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V", at = @At("HEAD"))
	private void injectAddPotionEffect(PotionEffect potionEffect, CallbackInfo info) {
		if (potionEffect.getPotion() == IFSPPotions.DRAGONREND) {
			super.addPotionEffect(potionEffect);
		}
	}

}
