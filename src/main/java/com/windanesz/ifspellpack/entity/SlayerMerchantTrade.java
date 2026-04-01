package com.windanesz.ifspellpack.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SlayerMerchantTrade {

	public static final String STACK_KEY = "Stack";
	public static final String COST_KEY = "Cost";
	public static final String CURRENT_TRADE_USES_KEY = "CurrentTradeUses";
	public static final String MAX_TRADE_USES_KEY = "MaxTradeUses";

	private ItemStack stack;
	private int cost;
	private int currentTradeUses;
	private int maxTradeUses;

	public SlayerMerchantTrade(ItemStack stack, int cost) {
		this(stack, cost, 0, 3);
	}

	public SlayerMerchantTrade(ItemStack stack, int cost, int currentTradeUses, int maxTradeUses) {
		this.stack = stack;
		this.cost = cost;
		this.currentTradeUses = currentTradeUses;
		this.maxTradeUses = maxTradeUses;
	}

	public SlayerMerchantTrade(NBTTagCompound tagCompound)
	{
		this.stack = ItemStack.EMPTY;
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

	public int getMaxTradeUses() {
		return this.maxTradeUses;
	}

	public void incrementCurrentTradeUses() {
		this.currentTradeUses++;
	}

	public boolean isTradeDisabled() {
		return this.currentTradeUses >= this.maxTradeUses;
	}

	public void readFromTags(NBTTagCompound nbt) {
		this.stack = new ItemStack(nbt.getCompoundTag(STACK_KEY));
		this.cost = nbt.getInteger(COST_KEY);
		this.currentTradeUses = nbt.getInteger(CURRENT_TRADE_USES_KEY);
		this.maxTradeUses = nbt.getInteger(MAX_TRADE_USES_KEY);
	}

	public NBTTagCompound writeToTags() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setTag(STACK_KEY, this.stack.writeToNBT(new NBTTagCompound()));
		nbttagcompound.setInteger(COST_KEY, this.cost);
		nbttagcompound.setInteger(CURRENT_TRADE_USES_KEY, this.currentTradeUses);
		nbttagcompound.setInteger(MAX_TRADE_USES_KEY, this.maxTradeUses);
		return nbttagcompound;
	}

}
