package com.windanesz.ifspellpack.potion;

import electroblob.wizardry.potion.PotionMagicEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class PotionTideGuardian extends PotionMagicEffect {

    //This is an invisible potion intended to apply other potions
    public PotionTideGuardian() {
        super(false, 0x001482, null);
    }

    @Override
    public void performEffect(EntityLivingBase entitylivingbase, int strength) {
        entitylivingbase.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 50));
        if (entitylivingbase.isWet()) {
            entitylivingbase.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 50, strength));
        }
    }

    //No visible potion icon in inventory
    @Override
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
    }

    @Override
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
    }

    @Override
    protected void drawIcon(int x, int y, PotionEffect effect, Minecraft mc) {
    }
}
