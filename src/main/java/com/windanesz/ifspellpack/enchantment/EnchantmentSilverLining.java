package com.windanesz.ifspellpack.enchantment;

import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.enchantment.Imbuement;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentSilverLining extends EnchantmentDamage implements Imbuement {

	public static final float DAMAGE_INCREASE = 0.25f;
	public static final int BURN_TIME = 5;

	public EnchantmentSilverLining() {
		super(Enchantment.Rarity.COMMON, 0, EntityEquipmentSlot.MAINHAND);
		this.setRegistryName(IFSpellPack.MODID, "silver_lining");
	}

	@Override
	public boolean canApply(ItemStack stack){
		return false;
	}

	@Override
	public int getMaxLevel(){
		return 4;
	}

	@Override
	public float calcDamageByCreature(int level, EnumCreatureAttribute enumCreatureAttribute){
		float increase = 0f;
		if (enumCreatureAttribute == EnumCreatureAttribute.UNDEAD) {
			increase += level * 2f;
		}
		return increase;
	}

	@Override
	public String getName(){
		return "enchantment." + this.getRegistryName();
	}

	@Override
	public boolean isAllowedOnBooks(){
		return false;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack){
		return false;
	}

}
