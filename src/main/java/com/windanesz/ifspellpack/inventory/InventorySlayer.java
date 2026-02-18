package com.windanesz.ifspellpack.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import javax.annotation.Nonnull;

public class InventorySlayer implements IInventory {
    private final IMerchant merchant;
    @Nonnull
    private ItemStack stack = ItemStack.EMPTY;
    private final EntityPlayer player;
    private MerchantRecipe currentRecipe;
    private int currentRecipeIndex;

    public InventorySlayer(EntityPlayer thePlayerIn, IMerchant theMerchantIn) {
        this.player = thePlayerIn;
        this.merchant = theMerchantIn;
    }

    public int getSizeInventory()
    {
        return 1;
    }

    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    public ItemStack getStackInSlot(int index)
    {
        return this.stack;
    }

    public ItemStack decrStackSize(int index, int count) {
        return this.stack.splitStack(count);
    }

    public ItemStack removeStackFromSlot(int index) {
        ItemStack itemStack = this.stack.copy();
        this.stack = ItemStack.EMPTY;
        return itemStack;
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        this.stack = stack;
        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }
    }

    public String getName()
    {
        return "mob.slayer";
    }

    public boolean hasCustomName()
    {
        return false;
    }

    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return this.merchant.getCustomer() == player;
    }

    public void openInventory(EntityPlayer player) {
    }

    public void closeInventory(EntityPlayer player) {
    }

    //see what this does
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    public void markDirty() {
    }

    public MerchantRecipe getCurrentRecipe()
    {
        return this.currentRecipe;
    }

    public void setCurrentRecipeIndex(int currentRecipeIndexIn)
    {
        this.currentRecipeIndex = currentRecipeIndexIn;
    }

    public int getField(int id)
    {
        return 0;
    }

    public void setField(int id, int value) {
    }

    public int getFieldCount() {
        return 0;
    }

    public void clear() {
        this.stack = ItemStack.EMPTY;
    }
}