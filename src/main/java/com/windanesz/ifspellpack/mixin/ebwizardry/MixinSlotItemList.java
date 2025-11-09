package com.windanesz.ifspellpack.mixin.ebwizardry;

import com.windanesz.ifspellpack.item.ItemChargedArtefact;
import electroblob.wizardry.inventory.SlotItemList;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SlotItemList.class, remap = false)
public class MixinSlotItemList {

	@Inject(method = "isItemValid(Lnet/minecraft/item/ItemStack;)Z", at = @At("RETURN"), cancellable = true)
	private void injectIsItemValid(ItemStack itemStack, CallbackInfoReturnable<Boolean> info) {
		// ContainerArcaneWorkbench.CRYSTAL_SLOT = 8;
		if (((Slot) (Object) this).slotNumber == 8) {
			if (ItemChargedArtefact.VALID_ITEMS.contains(itemStack.getItem())) {
				info.setReturnValue(true);
			}
		}
	}

}
