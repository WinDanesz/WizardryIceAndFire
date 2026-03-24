package com.windanesz.ifspellpack.inventory;

import com.windanesz.ifspellpack.entity.ISlayerMerchant;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerSlayerMerchant extends Container
{
    private final ISlayerMerchant merchant;
    private final InventorySlayerMerchant merchantInventory;

    public ContainerSlayerMerchant(InventoryPlayer playerInventory, ISlayerMerchant merchant) {
        this.merchant = merchant;
        this.merchantInventory = new InventorySlayerMerchant(playerInventory.player, merchant);
        this.addSlotToContainer(new SlotSlayerMerchantResult(playerInventory.player, merchant, this.merchantInventory, 0, 120, 54));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public InventorySlayerMerchant getMerchantInventory()
    {
        return this.merchantInventory;
    }

    public void setCurrentRecipeIndex(int currentRecipeIndex) {
        this.merchantInventory.setCurrentRecipeIndex(currentRecipeIndex);
    }

    public void resetTrade() {
        this.merchantInventory.resetTrade();
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.merchant.getCustomer() == playerIn;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 0) {
                if (!this.mergeItemStack(itemstack1, 1, 36, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack1, itemstack);
            }
            else {
                if (index >= 1 && index <= 27) {
                    if (!this.mergeItemStack(itemstack1, 27, 6, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 27 && index <= 36 && !this.mergeItemStack(itemstack1, 1, 27, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            else {
                slot.onSlotChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemstack1);
        }
        return itemstack;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        //Do I need both?
        super.onContainerClosed(playerIn);
        this.merchant.setCustomer(null);
        super.onContainerClosed(playerIn);
    }
}