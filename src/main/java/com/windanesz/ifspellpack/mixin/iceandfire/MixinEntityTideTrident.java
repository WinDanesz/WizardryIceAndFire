package com.windanesz.ifspellpack.mixin.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntityHydra;
import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import com.github.alexthe666.iceandfire.entity.EntityTideTrident;
import com.windanesz.ifspellpack.accessor.AccessorEntityTideTrident;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = EntityTideTrident.class, remap = false)
public class MixinEntityTideTrident implements AccessorEntityTideTrident {

	@Shadow protected int timeInGround;
	@Unique
	private boolean ifspellpack$isSpell;

	@Unique
	private float ifspellpack$damageMultiplier;

	@Unique
	private int ifspellpack$burnDuration;

	@Override
	public boolean ifspellpack$isSpell() {
		return this.ifspellpack$isSpell;
	}

	@Override
	public void ifspellpack$setSpell(boolean b) {
		this.ifspellpack$isSpell = b;
	}

	@Override
	public float ifspellpack$getDamageMultiplier() {
		return this.ifspellpack$damageMultiplier;
	}

	@Override
	public void ifspellpack$setDamageMultiplier(float f) {
		this.ifspellpack$damageMultiplier = f;
	}

	@Override
	public int ifspellpack$getBurnDuration() {
		return this.ifspellpack$burnDuration;
	}

	@Override
	public void ifspellpack$setBurnDuration(int i) {
		this.ifspellpack$burnDuration = i;
	}

	@Inject(method = "onUpdate()V", at = @At("HEAD"))
	private void injectOnUpdate(CallbackInfo info) {
		if (this.ifspellpack$isSpell() && this.timeInGround == 100) {
			((EntityTideTrident)(Object)this).setDead();
		}
	}

	@Inject(method = "onHit(Lnet/minecraft/util/math/RayTraceResult;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	private void injectOnHit(RayTraceResult result, CallbackInfo info, Entity entity) {
		if (this.ifspellpack$isSpell()) {
			if (entity instanceof EntityHydra || entity instanceof EntityGorgon || entity instanceof EntitySeaSerpent) {
				EntityTideTrident trident = (EntityTideTrident) (Object) this;
				trident.setDamage(trident.getDamage() * this.ifspellpack$getDamageMultiplier());
				entity.setFire(this.ifspellpack$getBurnDuration());
			}
		}
	}
}
