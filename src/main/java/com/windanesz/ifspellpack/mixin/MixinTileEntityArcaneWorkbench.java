package com.windanesz.ifspellpack.mixin;

import com.windanesz.ifspellpack.item.ItemChargeableArtefact;
import com.windanesz.ifspellpack.item.ItemChargedArtefact;
import electroblob.wizardry.inventory.ContainerArcaneWorkbench;
import electroblob.wizardry.tileentity.TileEntityArcaneWorkbench;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TileEntityArcaneWorkbench.class)
public abstract class MixinTileEntityArcaneWorkbench {

	@Shadow public abstract ItemStack getStackInSlot(int slot);

	@Inject(method = "isItemValidForSlot(ILnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
	private void injectIsItemValidForSlot(int slotNumber, ItemStack itemStack, CallbackInfoReturnable<Boolean> info) {
		if (getStackInSlot(ContainerArcaneWorkbench.CENTRE_SLOT).getItem() instanceof ItemChargeableArtefact) {
			if (itemStack.getItem() == ((ItemChargeableArtefact)getStackInSlot(ContainerArcaneWorkbench.CENTRE_SLOT).getItem()).getChargedItem().getChargeItem() && slotNumber == ContainerArcaneWorkbench.CRYSTAL_SLOT) {
				info.setReturnValue(true);
			}
		}
		if (getStackInSlot(ContainerArcaneWorkbench.CENTRE_SLOT).getItem() instanceof ItemChargedArtefact) {
			if (itemStack.getItem() == ((ItemChargedArtefact)getStackInSlot(ContainerArcaneWorkbench.CENTRE_SLOT).getItem()).getChargeItem() && slotNumber == ContainerArcaneWorkbench.CRYSTAL_SLOT) {
				info.setReturnValue(true);
			}
		}
	}

}
