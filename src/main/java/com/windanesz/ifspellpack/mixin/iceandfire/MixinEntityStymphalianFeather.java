package com.windanesz.ifspellpack.mixin.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntityStymphalianFeather;
import com.windanesz.ifspellpack.accessor.AccessorEntityStymphalianFeather;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityStymphalianFeather.class, remap = false)
public abstract class MixinEntityStymphalianFeather implements AccessorEntityStymphalianFeather {

	@Unique
	private boolean ifspellpack$droppable;

	@Override
	public boolean ifspellpack$isDroppable() {
		return this.ifspellpack$droppable;
	}

	@Override
	public void ifspellpack$setDroppable(boolean b) {
		this.ifspellpack$droppable = b;
	}

	@Inject(method = "setDead()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/EntityArrow;setDead()V", shift = At.Shift.AFTER), cancellable = true)
	private void injectSetDead(CallbackInfo info) {
		if (!ifspellpack$droppable) {
			info.cancel();
		}
	}
}
