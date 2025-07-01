package com.windanesz.ifspellpack.mixin.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntityTroll;
import com.github.alexthe666.iceandfire.entity.ai.TrollAIFleeSun;
import com.windanesz.ifspellpack.accessor.AccessorEntityTroll;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TrollAIFleeSun.class)
public class MixinTrollAIFleeSun {

	@Shadow @Final private EntityTroll troll;

	@Inject(method = "shouldExecute()Z", at= @At("HEAD"), cancellable = true)
	private void injectShouldExecute(CallbackInfoReturnable<Boolean> info) {
		if (((AccessorEntityTroll)this.troll).ifspellpack$isSunlightImmune()) {
			info.setReturnValue(false);
		}
	}

}
