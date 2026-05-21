package com.windanesz.ifspellpack.entity.living;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.windanesz.ifspellpack.entity.SlayerMerchantTrade;
import com.windanesz.ifspellpack.entity.SlayerMerchantTradeList;
import com.windanesz.ifspellpack.registry.IFSPItems;
import com.windanesz.ifspellpack.registry.IFSPSpells;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.item.ItemWand;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.WandHelper;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class EntityLightwardBattlemage extends EntitySlayerMage {


	public EntityLightwardBattlemage(World worldIn) {
		super(worldIn);
		//Lightward battlemages heal twice as much
		this.healFactor = 2f;
	}

	@Override
	public SlayerMerchantTradeList initializeTrades() {
		SlayerMerchantTradeList trades = new SlayerMerchantTradeList();
		trades.add(new SlayerMerchantTrade(new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.SILVER_LINING.metadata()), 8 + this.getRNG().nextInt(4)));
		trades.add(new SlayerMerchantTrade(new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.LIGHTWARD_ARMAMENT.metadata()), 16 + this.getRNG().nextInt(8)));
		trades.add(new SlayerMerchantTrade(new ItemStack(IFSPItems.AMULET_LIGHTWARD), 25 + this.getRNG().nextInt(5)));
		Collections.shuffle(trades);
		return trades;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30);
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		this.spells.add(IFSPSpells.SILVER_LINING);
		this.spells.add(Spells.celestial_smite);
		this.spells.add(Spells.radiant_totem);
		this.spells.add(Spells.healing_aura);
		this.spells.add(Spells.empowering_presence);
		ItemStack wand = new ItemStack(ItemWand.getWand(Tier.MASTER, Element.HEALING));
		WandHelper.setSpells(wand, this.spells.toArray(new Spell[0]));
		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, wand);
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(IafItemRegistry.silver_helmet));
		this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(IafItemRegistry.silver_chestplate));
		this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(IafItemRegistry.silver_leggings));
		this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(IafItemRegistry.silver_boots));
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(IafItemRegistry.silver_sword));
		for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
			this.setDropChance(slot, 0.0f);
		}
		this.setHealCooldown(50);
		return super.onInitialSpawn(difficulty, livingdata);
	}
}
