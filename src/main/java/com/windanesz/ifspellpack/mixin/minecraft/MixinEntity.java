package com.windanesz.ifspellpack.mixin.minecraft;

import com.windanesz.ifspellpack.accessor.AccessorEntityTameable;
import com.windanesz.ifspellpack.registry.IFSPItems;
import com.windanesz.ifspellpack.world.EntityPosData;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntity {

	@Shadow public World world;

	@Inject(method = "Lnet/minecraft/entity/Entity;isInvisibleToPlayer(Lnet/minecraft/entity/player/EntityPlayer;)Z", at = @At("HEAD"), cancellable = true)
	private void injectIsEntityInvisibleToPlayer(EntityPlayer player, CallbackInfoReturnable<Boolean> info) {
		if (ItemArtefact.isArtefactActive(player, IFSPItems.CHARM_DRAGON_EYE)) {
			info.setReturnValue(false);
		}
	}

	@Inject(method = "onRemovedFromWorld()V", at = @At("HEAD"))
	private void injectOnRemovedFromWorld(CallbackInfo info) {
		Entity entity = (Entity)(Object)this;
		if (entity instanceof EntityTameable) {
			EntityTameable entityTameable = (EntityTameable)entity;
			if (((AccessorEntityTameable)entityTameable).ifspellpack$shouldSavePos() && !entity.isDead) {
				EntityPosData.get(entity.world).addEntity(entityTameable.getUniqueID(), entityTameable.getPosition());
			}
		}
	}

	@Inject(method = "setDead()V", at = @At("HEAD"))
	private void injectSetDead(CallbackInfo info) {
		Entity entity = (Entity)(Object)this;
		if (entity instanceof EntityTameable) {
			EntityTameable entityTameable = (EntityTameable)entity;
			if (((AccessorEntityTameable)entityTameable).ifspellpack$shouldSavePos()) {
				EntityPosData.get(entity.world).removeEntity(entity.getUniqueID());
			}
		}
	}

}
