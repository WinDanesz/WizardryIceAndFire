package com.windanesz.ifspellpack.spell;

import com.google.common.collect.ImmutableMap;
import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.item.IConjuredItem;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellConjuration;
import electroblob.wizardry.util.InventoryUtils;
import electroblob.wizardry.util.NBTExtras;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;

import java.util.Map;

public class LightwardArmament extends SpellConjuration {

	private static final Map<EntityEquipmentSlot, Item> LIGHTWARD_ARMAMENT_MAP = ImmutableMap.of(
			EntityEquipmentSlot.HEAD, WizardryItems.spectral_helmet,
			EntityEquipmentSlot.CHEST, WizardryItems.spectral_chestplate,
			EntityEquipmentSlot.LEGS, WizardryItems.spectral_leggings,
			EntityEquipmentSlot.FEET, WizardryItems.spectral_boots);

	public LightwardArmament(){
		super(IFSpellPack.MODID, "lightward_armament", WizardryItems.spectral_sword);
	}

	@Override
	protected boolean conjureItem(EntityPlayer caster, SpellModifiers modifiers){
		ItemStack armour;
		boolean flag = false;
		// Used this rather than getArmorInventoryList because I need to access the slot itself
		for(EntityEquipmentSlot slot : InventoryUtils.ARMOUR_SLOTS){
			if(caster.getItemStackFromSlot(slot).isEmpty() && !InventoryUtils.doesPlayerHaveItem(caster, LIGHTWARD_ARMAMENT_MAP.get(slot))){
				armour = new ItemStack(LIGHTWARD_ARMAMENT_MAP.get(slot));
				IConjuredItem.setDurationMultiplier(armour, modifiers.get(WizardryItems.duration_upgrade));
				// Sets a blank "ench" tag to trick the renderer into showing the enchantment effect on the armour model
				NBTExtras.storeTagSafely(armour.getTagCompound(), "ench", new NBTTagList());
				caster.setItemStackToSlot(slot, armour);
				flag = true;
			}
		}
		//Using 1 | is necessary as the super should be checked even if flag is true
		return flag | super.conjureItem(caster, modifiers);
	}

}
