package com.windanesz.ifspellpack.item;

import electroblob.wizardry.item.IWorkbenchItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemChargedArtefact extends ItemArtefactIFSP implements IWorkbenchItem {

	private final Item chargeItem;
	private final int chargePerItem;
	private final int chargePerUse;

	public ItemChargedArtefact(EnumRarity rarity, Type type, int maxCharges, Item chargeItem, int chargePerItem, int chargePerUse) {
		super(rarity, type);
		this.setMaxDamage(maxCharges);
		this.chargeItem = chargeItem;
		this.chargePerItem = chargePerItem;
		this.chargePerUse = chargePerUse;
	}

	public Item getChargeItem() {
		return this.chargeItem;
	}

	public int getChargePerItem() {
		return this.chargePerItem;
	}

	public int getChargePerUse() {
		return this.chargePerUse;
	}

	public static boolean consumeCharge(ItemStack itemStack) {
		if (itemStack.getItem() instanceof ItemChargedArtefact) {
			ItemChargedArtefact itemChargedArtefact = (ItemChargedArtefact)itemStack.getItem();
			if (itemChargedArtefact.getMaxDamage(itemStack) - itemChargedArtefact.getDamage(itemStack) >= itemChargedArtefact.chargePerUse) {
				itemChargedArtefact.setDamage(itemStack, itemChargedArtefact.getDamage(itemStack) + itemChargedArtefact.chargePerUse);
				return true;
			}
		}
		return false;
	}

	@Override
	public int getSpellSlotCount(ItemStack stack) {
		return 0;
	}

	@Override
	public boolean onApplyButtonPressed(EntityPlayer player, Slot centre, Slot crystals, Slot upgrade, Slot[] spellBooks) {
		if (crystals.getStack().getItem() == this.getChargeItem()) {
			int chargePerItem = this.getChargePerItem();
			int chargeCount = crystals.getStack().getCount() * chargePerItem;
			int threshold = this.getMaxDamage(centre.getStack());
			if (chargeCount <= threshold) {
				centre.getStack().setItemDamage(this.getMaxDamage(centre.getStack()) - chargeCount);
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
