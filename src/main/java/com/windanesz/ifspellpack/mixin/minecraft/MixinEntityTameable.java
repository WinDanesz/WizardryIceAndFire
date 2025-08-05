package com.windanesz.ifspellpack.mixin.minecraft;

import com.windanesz.ifspellpack.accessor.AccessorEntityTameable;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityTameable.class)
public class MixinEntityTameable implements AccessorEntityTameable {

	@Unique
	private boolean ifspellpack$shouldSavePos;

	@Override
	public boolean ifspellpack$shouldSavePos() {
		return this.ifspellpack$shouldSavePos;
	}

	@Override
	public void ifspellpack$setShouldSavePos(boolean b) {
		this.ifspellpack$shouldSavePos = b;
	}

	@Inject(method = "writeEntityToNBT(Lnet/minecraft/nbt/NBTTagCompound;)V", at = @At("TAIL"))
	private void injectWriteEntityToNBT(NBTTagCompound compound, CallbackInfo info) {
		compound.setBoolean("ShouldSavePos", this.ifspellpack$shouldSavePos());
	}

	@Inject(method = "readEntityFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)V", at = @At("TAIL"))
	private void InjectEntityFromNBT(NBTTagCompound compound, CallbackInfo info) {
		this.ifspellpack$setShouldSavePos(compound.getBoolean("ShouldSavePos"));
	}

}
