package com.windanesz.ifspellpack.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public interface ISlayerMerchant {

    void setCustomer(@Nullable EntityPlayer player);

    @Nullable
    EntityPlayer getCustomer();

    ITextComponent getDisplayName();

    @Nullable
    SlayerMerchantTradeList getTrades();

    @Nullable
    void setTrades(SlayerMerchantTradeList trades);

    void purchaseItem(SlayerMerchantTrade recipe);

}