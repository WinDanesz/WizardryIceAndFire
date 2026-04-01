package com.windanesz.ifspellpack.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;

public class SlayerMerchantTradeList extends ArrayList<SlayerMerchantTrade> {

	public static final String RECIPES_KEY = "Recipes";

	public SlayerMerchantTradeList() { }

	public SlayerMerchantTradeList(NBTTagCompound compound) {
		this.readRecipiesFromTags(compound);
	}

	public void readRecipiesFromTags(NBTTagCompound compound) {
		NBTTagList nbttaglist = compound.getTagList(RECIPES_KEY, 10);
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
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
		nbttagcompound.setTag(RECIPES_KEY, nbttaglist);
		return nbttagcompound;
	}

	public void writeToBuf(ByteBuf buf) {
		buf.writeByte((byte)(this.size() & 255));
		for (SlayerMerchantTrade trade : this) {
			ByteBufUtils.writeItemStack(buf, trade.getStack());
			buf.writeInt(trade.getCost());
			buf.writeInt(trade.getCurrentTradeUses());
			buf.writeInt(trade.getMaxTradeUses());
		}
	}

	@SideOnly(Side.CLIENT)
	public static SlayerMerchantTradeList readFromBuf(ByteBuf buf) {
		SlayerMerchantTradeList trades = new SlayerMerchantTradeList();
		int i = buf.readByte() & 255;
		for (int j = 0; j < i; ++j) {
			ItemStack itemstack = ByteBufUtils.readItemStack(buf);
			int cost = buf.readInt();
			int currentTradeUses = buf.readInt();
			int maxTradeUses = buf.readInt();
			SlayerMerchantTrade trade = new SlayerMerchantTrade(itemstack, cost, currentTradeUses, maxTradeUses);
			trades.add(trade);
		}
		return trades;
	}

}
