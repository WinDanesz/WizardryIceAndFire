package com.windanesz.ifspellpack.mixin.minecraft;

import com.windanesz.ifspellpack.mixin.modrefs.VanillaMixinHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class ClientMixinEntity {

	@Inject(method = "isInvisibleToPlayer(Lnet/minecraft/entity/player/EntityPlayer;)Z", at = @At("HEAD"), cancellable = true)
	private void injectIsEntityInvisibleToPlayer(EntityPlayer player, CallbackInfoReturnable<Boolean> info) {
		if (VanillaMixinHelper.hasArtefact(player)) {
			;
			info.setReturnValue(false);
		}
	}
}
