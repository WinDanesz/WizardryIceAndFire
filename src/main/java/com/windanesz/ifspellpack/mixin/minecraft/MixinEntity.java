package com.windanesz.ifspellpack.mixin.minecraft;

import com.windanesz.ifspellpack.spell.CallBeast;
import com.windanesz.ifspellpack.world.BeastPosData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity {

	@Shadow public World world;

	@Inject(method = "onRemovedFromWorld()V", at = @At("HEAD"))
	private void injectOnRemovedFromWorld(CallbackInfo info) {
		Entity entity = (Entity)(Object)this;
		if (entity instanceof EntityTameable) {
			EntityTameable entityTameable = (EntityTameable)entity;
			if (CallBeast.isAcceptableBeast(entityTameable) && entityTameable.getOwner() instanceof EntityPlayer) {
				BeastPosData.get(entity.world).addBeast(entityTameable.getUniqueID(), entityTameable.getPosition());
			}
		}
	}

	@Inject(method = "setDead()V", at = @At("HEAD"))
	private void injectSetDead(CallbackInfo info) {
		Entity entity = (Entity)(Object)this;
		BeastPosData.get(entity.world).removeBeast(entity.getUniqueID());
	}

}
