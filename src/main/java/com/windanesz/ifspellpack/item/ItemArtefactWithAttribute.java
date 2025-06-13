package com.windanesz.ifspellpack.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemArtefactWithAttribute extends ItemArtefactIFSP implements IBauble {
	private final Multimap<IAttribute, AttributeModifier> modifiers;
	Type type;

	public ItemArtefactWithAttribute(EnumRarity rarity, Type type,  Multimap<IAttribute, AttributeModifier> modifiers) {
		super(rarity, type );
		this.type = type;
		this.modifiers = modifiers;

	}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		for (IAttribute attribute : modifiers.keySet()) {
			for (AttributeModifier modifier : modifiers.get(attribute)) {
				if (!player.getEntityAttribute(attribute).hasModifier(modifier)) {
					player.getEntityAttribute(attribute).applyModifier(modifier);
				}
			}
		}
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
		for (IAttribute attribute : modifiers.keySet()) {
			for (AttributeModifier modifier : modifiers.get(attribute)) {
				if (player.getEntityAttribute(attribute).hasModifier(modifier)) {
					player.getEntityAttribute(attribute).removeModifier(modifier);
				}
			}
		}
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		switch (type) {
			case CHARM:
				return BaubleType.CHARM;
			case RING:
				return BaubleType.RING;
			case AMULET:
				return BaubleType.AMULET;
			case HEAD:
				return BaubleType.HEAD;
			case BELT:
				return BaubleType.BELT;
			default:
				return BaubleType.BODY;
		}
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if (!modifiers.isEmpty()) {
			for (IAttribute attribute : modifiers.keySet()) {
				for (AttributeModifier modifier : modifiers.get(attribute)) {
					tooltip.add(TextFormatting.BLUE + modifier.getName() + ": " + (int) modifier.getAmount());
				}
			}
		}
	}


	@Nullable
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return null;
	}
}