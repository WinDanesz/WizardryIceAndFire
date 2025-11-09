package com.windanesz.ifspellpack.mixin.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntityDreadScuttler;
import electroblob.wizardry.entity.living.ISummonedCreature;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = EntityDreadScuttler.class, remap = false)
public abstract class MixinEntityDreadScuttler {

	@Inject(method = "isOnSameTeam(Lnet/minecraft/entity/Entity;)Z", at = @At("RETURN"), cancellable = true)
	private void injectIsOnSameTeam(Entity entity, CallbackInfoReturnable<Boolean> info) {
		info.setReturnValue(info.getReturnValue() && !(entity instanceof ISummonedCreature));
	}

}
