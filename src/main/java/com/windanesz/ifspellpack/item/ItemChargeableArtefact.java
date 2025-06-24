package com.windanesz.ifspellpack.item;

import electroblob.wizardry.item.IWorkbenchItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemChargeableArtefact extends ItemArtefactIFSP implements IWorkbenchItem {

	private final ItemChargedArtefact chargedItem;

	public ItemChargeableArtefact(EnumRarity rarity, Type type, ItemChargedArtefact chargedItem) {
		super(rarity, type);
		this.chargedItem = chargedItem;
	}

	public Item getChargedItem() {
		return this.chargedItem;
	}

	@Override
	public int getSpellSlotCount(ItemStack stack) {
		return 0;
	}

	@Override
	public boolean onApplyButtonPressed(EntityPlayer player, Slot centre, Slot crystals, Slot upgrade, Slot[] spellBooks) {
		ItemChargedArtefact chargedArtefact = this.chargedItem;
		if (crystals.getStack().getItem() == chargedArtefact.getChargeItem()) {
			int chargePerItem = chargedArtefact.getChargePerItem();
			int chargeCount = crystals.getStack().getCount() * chargePerItem;
			int threshold = chargedArtefact.getMaxDamage(centre.getStack());
			centre.putStack(new ItemStack(chargedArtefact));
			if (chargeCount <= threshold) {
				centre.getStack().setItemDamage(chargedArtefact.getMaxDamage(centre.getStack()) - chargeCount);
				crystals.decrStackSize(crystals.getStack().getCount());
			} else {
				centre.getStack().setItemDamage(0);
				crystals.decrStackSize((int)Math.ceil(((double)threshold) / chargePerItem));
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean showTooltip(ItemStack stack) {
		return false;
	}
}
