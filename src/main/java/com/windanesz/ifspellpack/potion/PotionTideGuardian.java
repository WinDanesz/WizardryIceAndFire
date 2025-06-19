package com.windanesz.ifspellpack.potion;

import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.potion.PotionMagicEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class PotionTideGuardian extends PotionMagicEffect {

    public PotionTideGuardian() {
        super(false, 0x001482, new ResourceLocation(IFSpellPack.MODID, "textures/potions/tide_guardian.png"));
    }

    @Override
    public void performEffect(EntityLivingBase entitylivingbase, int strength) {
        entitylivingbase.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 50));
        if (entitylivingbase.isWet()) {
            entitylivingbase.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 50, strength));
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }
}
