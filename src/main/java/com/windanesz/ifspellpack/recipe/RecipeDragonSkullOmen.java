package com.windanesz.ifspellpack.recipe;

import com.github.alexthe666.iceandfire.item.ItemDragonSkull;
import com.windanesz.ifspellpack.item.ItemDragonSkullOmen;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;

public class RecipeDragonSkullOmen extends ShapedOreRecipe {

	public RecipeDragonSkullOmen(ResourceLocation group, @Nonnull ItemStack result, Object... recipe) {
		super(group, result, recipe);
	}

	public RecipeDragonSkullOmen(ResourceLocation group, @Nonnull ItemStack result, CraftingHelper.ShapedPrimer primer) {
		super(group, result, primer);
	}

	@Override
	@Nonnull
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting inventory) {
		ItemStack result = super.getCraftingResult(inventory).copy();
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack skull = inventory.getStackInSlot(i);
			if (skull.getItem() instanceof ItemDragonSkull) {
				NBTTagCompound oldNBT = skull.getTagCompound();
				NBTTagCompound newNBT = new NBTTagCompound();
				newNBT.setInteger(ItemDragonSkullOmen.STAGE_KEY, oldNBT.getInteger(ItemDragonSkullOmen.STAGE_KEY));
				newNBT.setInteger(ItemDragonSkullOmen.DRAGON_AGE_KEY, oldNBT.getInteger(ItemDragonSkullOmen.DRAGON_AGE_KEY));
				result.setTagCompound(newNBT);
				//Exit the search when it finds a skull
				break;
			}
		}
		return result;
	}

}
