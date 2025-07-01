package com.windanesz.ifspellpack.mixin.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntityDreadMob;
import electroblob.wizardry.entity.living.ISummonedCreature;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(EntityDreadMob.class)
public abstract class MixinEntityDreadMob {

	@Inject(method = "isOnSameTeam(Lnet/minecraft/entity/Entity;)Z", at = @At("RETURN"), cancellable = true)
	private void injectIsOnSameTeam(Entity entity, CallbackInfoReturnable<Boolean> info) {
		info.setReturnValue(info.getReturnValue() && !(entity instanceof ISummonedCreature));
	}

}
