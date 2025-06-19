package com.windanesz.ifspellpack.mixin;

import com.github.alexthe666.iceandfire.entity.ChainEntityProperties;
import com.windanesz.ifspellpack.accessor.AccessorChainEntityProperties;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChainEntityProperties.class)
public class MixinChainEntityProperties implements AccessorChainEntityProperties {

	@Unique
	private boolean ifspellpack$dropsChain= true;

	@Override
	public boolean ifspellpack$getDropsChain() {
		return this.ifspellpack$dropsChain;
	}

	@Override
	public void ifspellpack$setDropsChain(boolean b) {
		this.ifspellpack$dropsChain = b;
	}

	@Inject(method = "addChain(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"))
	private void injectAddChain(CallbackInfo info) {
		this.ifspellpack$dropsChain = true;
	}

	@Inject(method = "saveNBTData(Lnet/minecraft/nbt/NBTTagCompound;)V", at = @At("TAIL"))
	private void injectSaveNBTData(NBTTagCompound compound, CallbackInfo info) {
		compound.setBoolean("DropsChain", this.ifspellpack$dropsChain);
	}

	@Inject(method = "loadNBTData(Lnet/minecraft/nbt/NBTTagCompound;)V", at = @At("TAIL"))
	private void injectLoadNBTData(NBTTagCompound compound, CallbackInfo info) {
		this.ifspellpack$dropsChain = compound.getBoolean("DropsChain");
	}

}
