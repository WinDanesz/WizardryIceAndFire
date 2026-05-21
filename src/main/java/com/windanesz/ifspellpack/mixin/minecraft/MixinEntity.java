package com.windanesz.ifspellpack.mixin.minecraft;

import com.windanesz.ifspellpack.accessor.AccessorEntityTameable;
import com.windanesz.ifspellpack.world.WorldData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity {


	@Inject(method = "onRemovedFromWorld()V", at = @At("HEAD"), remap = false)
	private void injectOnRemovedFromWorld(CallbackInfo info) {
		Entity entity = (Entity) (Object) this;
		if (entity instanceof EntityTameable) {
			EntityTameable entityTameable = (EntityTameable) entity;
			if (((AccessorEntityTameable) entityTameable).ifspellpack$shouldSavePos() && !entity.isDead) {
				WorldData.get(entity.world).addEntity(entityTameable.getUniqueID(), entityTameable.getPosition());
			}
		}
	}

	@Inject(method = "setDead()V", at = @At("HEAD"))
	private void injectSetDead(CallbackInfo info) {
		Entity entity = (Entity) (Object) this;
		if (entity instanceof EntityTameable) {
			EntityTameable entityTameable = (EntityTameable) entity;
			if (((AccessorEntityTameable) entityTameable).ifspellpack$shouldSavePos()) {
				WorldData.get(entity.world).removeEntity(entity.getUniqueID());
			}
		}
	}

}
