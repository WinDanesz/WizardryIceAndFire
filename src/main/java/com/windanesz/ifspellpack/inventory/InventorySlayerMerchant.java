package com.windanesz.ifspellpack.inventory;

import com.windanesz.ifspellpack.entity.ISlayerMerchant;
import com.windanesz.ifspellpack.entity.SlayerMerchantTrade;
import com.windanesz.ifspellpack.entity.SlayerMerchantTradeList;
import com.windanesz.ifspellpack.world.SlayerTracker;
import electroblob.wizardry.data.WizardData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InventorySlayerMerchant implements IInventory {
    @Nonnull
    private ItemStack stack = ItemStack.EMPTY;
    private final EntityPlayer player;
    private final ISlayerMerchant merchant;
    @Nullable
    private SlayerMerchantTrade currentRecipe;
    private int currentRecipeIndex;

    public InventorySlayerMerchant(EntityPlayer player, ISlayerMerchant merchant) {
        this.player = player;
        this.merchant = merchant;
    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return this.stack;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return this.stack.splitStack(count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack itemStack = this.stack.copy();
        this.stack = ItemStack.EMPTY;
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.stack = stack;
        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }
    }

    @Override
    public String getName()
    {
        return "mob.slayer";
    }

    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return this.merchant.getCustomer() == player;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    @Override
    public void markDirty() {
        //Sync client and server trades or something :)
        this.resetTrade();
    }

    public void resetTrade() {
        this.currentRecipe = null;
        this.stack = ItemStack.EMPTY;
        SlayerMerchantTradeList trades = this.merchant.getTrades();
        if (trades != null) {
            if (this.currentRecipeIndex >= 0 && this.currentRecipeIndex < trades.size()) {
                this.currentRecipe = trades.get(this.currentRecipeIndex);
                WizardData data = WizardData.get(this.player);
                if (data != null) {
                    Integer points = data.getVariable(SlayerTracker.POINT_TRACKER);
                    if (points != null) {
                        if (points >= this.currentRecipe.getCost() && !this.currentRecipe.isTradeDisabled()) {
                            ItemStack stack = currentRecipe.getStack().copy();
                            this.setInventorySlotContents(0, stack);
                        }
                    }
                }
            }
        }
    }

    public SlayerMerchantTrade getCurrentRecipe() {
        return this.currentRecipe;
    }

    public void setCurrentRecipeIndex(int currentRecipeIndexIn) {
        this.currentRecipeIndex = currentRecipeIndexIn;
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        this.stack = ItemStack.EMPTY;
    }

}