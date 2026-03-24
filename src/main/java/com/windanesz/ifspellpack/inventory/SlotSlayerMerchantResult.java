package com.windanesz.ifspellpack.inventory;

import com.windanesz.ifspellpack.entity.ISlayerMerchant;
import com.windanesz.ifspellpack.entity.SlayerMerchantTrade;
import com.windanesz.ifspellpack.network.C2SPacketSlayerPointSpend;
import com.windanesz.ifspellpack.network.C2SPacketSummonBeast;
import com.windanesz.ifspellpack.registry.IFSPPackets;
import com.windanesz.ifspellpack.world.SlayerTracker;
import electroblob.wizardry.data.WizardData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotSlayerMerchantResult extends Slot {

    private final EntityPlayer player;
    private final ISlayerMerchant merchant;
    private final InventorySlayerMerchant merchantInventory;
    private int removeCount;

    public SlotSlayerMerchantResult(EntityPlayer player, ISlayerMerchant merchant, InventorySlayerMerchant merchantInventory, int slotIndex, int xPosition, int yPosition) {
        super(merchantInventory, slotIndex, xPosition, yPosition);
        this.player = player;
        this.merchant = merchant;
        this.merchantInventory = merchantInventory;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        if (this.getHasStack()) {
            this.removeCount += Math.min(amount, this.getStack().getCount());
        }
        return super.decrStackSize(amount);
    }

    @Override
    protected void onCrafting(ItemStack stack, int amount)
    {
        this.removeCount += amount;
        this.onCrafting(stack);
    }

    @Override
    protected void onCrafting(ItemStack stack)
    {
        stack.onCrafting(this.player.world, this.player, this.removeCount);
        this.removeCount = 0;
    }

    @Override
    public ItemStack onTake(EntityPlayer player, ItemStack stack) {
        this.onCrafting(stack);
        SlayerMerchantTrade merchantRecipe = this.merchantInventory.getCurrentRecipe();
        if (merchantRecipe != null) {
            WizardData data = WizardData.get(player);
            if (data != null) {
                Integer points = data.getVariable(SlayerTracker.POINT_TRACKER);
                if (points != null) {
                    if (points >= merchantRecipe.getCost()) {
                        points -= merchantRecipe.getCost();
                        data.setVariable(SlayerTracker.POINT_TRACKER, points);
                        IFSPPackets.net.sendToServer(new C2SPacketSlayerPointSpend.Message(points));
                        this.merchant.purchaseItem(merchantRecipe);
                        this.merchantInventory.resetTrade();
                    }
                }
            }
        }
        return stack;
    }

}