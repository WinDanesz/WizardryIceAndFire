package com.windanesz.ifspellpack.inventory;

import com.windanesz.ifspellpack.item.ItemCharmDwarvenPocketForge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryDwarvenPocketForge extends InventoryBasic {

	private final ItemStack stack;

	public InventoryDwarvenPocketForge(ItemStack stack) {
		super("", true, 1);
		this.stack = stack;
	}

	public ItemStack getStack() {
		return stack;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		super.openInventory(player);
		NBTTagCompound nbt = this.stack.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
		}
		if (nbt.hasKey("Items")) {
			NBTTagList items = nbt.getTagList("Items", 10);
			for (int i = 0; i < items.tagCount(); ++i) {
				NBTTagCompound nbttagcompound = items.getCompoundTagAt(i);
				int j = nbttagcompound.getByte("Slot") & 255;
				if (j < this.getSizeInventory()) {
					this.setInventorySlotContents(j, new ItemStack(nbttagcompound));
				}
			}
		}
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		NBTTagCompound nbt = this.stack.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
		}
		NBTTagList items = new NBTTagList();
		for (int i = 0; i < this.getSizeInventory(); ++i) {
			ItemStack itemstack = this.getStackInSlot(i);
			if (!itemstack.isEmpty()) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte) i);
				itemstack.writeToNBT(nbttagcompound);
				items.appendTag(nbttagcompound);
			}
		}
		nbt.setTag("Items", items);
		stack.setTagCompound(nbt);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (ItemCharmDwarvenPocketForge.isItemValid(stack.getItem())) {
			return true;
		}
		return false;
	}
}