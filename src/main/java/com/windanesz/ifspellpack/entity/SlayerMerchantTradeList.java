package com.windanesz.ifspellpack.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;
import java.util.ArrayList;

public class SlayerMerchantTradeList extends ArrayList<SlayerMerchantTrade> {

	public SlayerMerchantTradeList() {
	}

	public SlayerMerchantTradeList(NBTTagCompound compound) {
		this.readRecipiesFromTags(compound);
	}

	public static SlayerMerchantTradeList readFromBuf(PacketBuffer buffer) throws IOException {
		SlayerMerchantTradeList merchantRecipes = new SlayerMerchantTradeList();
		int i = buffer.readByte() & 255;
		for (int j = 0; j < i; ++j) {
			ItemStack itemstack = buffer.readItemStack();
			int cost = buffer.readInt();
			SlayerMerchantTrade merchantrecipe = new SlayerMerchantTrade(itemstack, cost);
			merchantRecipes.add(merchantrecipe);
		}
		return merchantRecipes;
	}

	public void writeToBuf(PacketBuffer buffer) {
		buffer.writeByte((byte)(this.size() & 255));
		for (SlayerMerchantTrade merchantRecipe : this) {
			buffer.writeItemStack(merchantRecipe.getStack());
			buffer.writeInt(merchantRecipe.getCost());
		}
	}

	public void readRecipiesFromTags(NBTTagCompound compound) {
		NBTTagList nbttaglist = compound.getTagList("Recipes", 10);

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
			this.add(new SlayerMerchantTrade(nbttagcompound));
		}
	}

	public NBTTagCompound getRecipiesAsTags() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		NBTTagList nbttaglist = new NBTTagList();
		for (SlayerMerchantTrade slayerMerchantRecipe : this) {
			nbttaglist.appendTag(slayerMerchantRecipe.writeToTags());
		}
		nbttagcompound.setTag("Recipes", nbttaglist);
		return nbttagcompound;
	}

}
