package com.windanesz.ifspellpack.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SlayerMerchantTrade {

	private ItemStack stack;
	private int cost;
	private int currentTradeUses;
	private int maxTradeUses;

	public SlayerMerchantTrade(ItemStack stack, int cost) {
		this(stack, cost, 3);
	}

	public SlayerMerchantTrade(ItemStack stack, int cost, int maxTradeUses) {
		this.stack = stack;
		this.cost = cost;
		this.maxTradeUses = maxTradeUses;
	}

	public SlayerMerchantTrade(NBTTagCompound tagCompound)
	{
		this.stack = ItemStack.EMPTY;
		this.cost = 0;
		this.readFromTags(tagCompound);
	}

	public ItemStack getStack() {
		return this.stack;
	}

	public void setStack(ItemStack stack) {
		this.stack = stack;
	}

	public int getCost() {
		return this.cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getCurrentTradeUses() {
		return this.currentTradeUses;
	}

	public void setCurrentTradeUses(int currentTradeUses) {
		this.currentTradeUses = currentTradeUses;
	}

	public void incrementCurrentTradeUses() {
		this.currentTradeUses++;
	}

	public boolean isTradeDisabled() {
		return this.currentTradeUses >= this.maxTradeUses;
	}

	public void readFromTags(NBTTagCompound nbt) {
		this.stack = new ItemStack(nbt.getCompoundTag("stack"));
		this.cost = nbt.getInteger("cost");
	}

	public NBTTagCompound writeToTags() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setTag("stack", this.stack.writeToNBT(new NBTTagCompound()));
		nbttagcompound.setInteger("cost", this.cost);
		return nbttagcompound;
	}

}
