package com.windanesz.ifspellpack.mixin.ebwizardry;

import com.windanesz.ifspellpack.item.ItemChargedArtefact;
import electroblob.wizardry.inventory.ContainerArcaneWorkbench;
import electroblob.wizardry.inventory.SlotItemList;
import electroblob.wizardry.tileentity.TileEntityArcaneWorkbench;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SlotItemList.class)
public class MixinSlotItemList {

	@Inject(method = "isItemValid(Lnet/minecraft/item/ItemStack;)Z", at = @At("RETURN"), cancellable = true)
	private void injectIsItemValid(ItemStack itemStack, CallbackInfoReturnable<Boolean> info) {
		SlotItemList slotItemList = (SlotItemList)(Object)this;
		if (slotItemList.inventory instanceof TileEntityArcaneWorkbench && slotItemList.slotNumber == ContainerArcaneWorkbench.CRYSTAL_SLOT) {
			if (ItemChargedArtefact.VALID_ITEMS.contains(itemStack.getItem())) {
				info.setReturnValue(true);
			}
		}
	}

}
