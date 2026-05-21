package com.windanesz.ifspellpack.entity.living;

import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.windanesz.ifspellpack.entity.SlayerMerchantTrade;
import com.windanesz.ifspellpack.entity.SlayerMerchantTradeList;
import com.windanesz.ifspellpack.registry.IFSPItems;
import com.windanesz.ifspellpack.registry.IFSPSpells;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.item.ItemWand;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.WandHelper;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EntitySlayerMaster extends EntitySlayerMage {

	public static final String VARIANT_KEY = "Variant";

	private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntitySlayerMaster.class, DataSerializers.VARINT);

	public EntitySlayerMaster(World worldIn) {
		super(worldIn);
	}

	@Override
	public SlayerMerchantTradeList initializeTrades() {
		SlayerMerchantTradeList trades = new SlayerMerchantTradeList();
		trades.add(new SlayerMerchantTrade(new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.SHACKLES.metadata()), 16 + this.getRNG().nextInt(8)));
		trades.add(new SlayerMerchantTrade(new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.NO_ESCAPE.metadata()), 32 + this.getRNG().nextInt(16)));
		trades.add(new SlayerMerchantTrade(new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.DRAGONREND.metadata()), 24 + this.getRNG().nextInt(12)));
		trades.add(new SlayerMerchantTrade(new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.DRAGONBANE.metadata()), 16 + this.getRNG().nextInt(8)));
		trades.add(new SlayerMerchantTrade(new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.LIVE_WIRE.metadata()), 16 + this.getRNG().nextInt(8)));
		trades.add(new SlayerMerchantTrade(new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.CALL_BEAST.metadata()), 24 + this.getRNG().nextInt(12)));
		trades.add(new SlayerMerchantTrade(new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.SERPENT_SLAYER.metadata()), 24 + this.getRNG().nextInt(12)));
		trades.add(new SlayerMerchantTrade(new ItemStack(IFSPItems.AMULET_DRAGON_SLAYER), 50 + this.getRNG().nextInt(10)));
		Collections.shuffle(trades);
		return trades;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(VARIANT, 0);
	}

	public int getVariant() {
		return this.dataManager.get(VARIANT);
	}

	public void setVariant(int variant) {
		this.dataManager.set(VARIANT, variant);
	}

	public void getVariantEquipment(int variant) {
		if (variant == 0) {
			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(EnumTroll.FOREST.helmet));
			this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(EnumTroll.FOREST.chestplate));
			this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(EnumTroll.FOREST.leggings));
			this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(EnumTroll.FOREST.boots));
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(IafItemRegistry.copper_sword));
		} else if (variant == 1) {
			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(EnumTroll.FROST.helmet));
			this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(EnumTroll.FROST.chestplate));
			this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(EnumTroll.FROST.leggings));
			this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(EnumTroll.FROST.boots));
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(IafItemRegistry.copper_sword));
		} else if (variant == 2) {
			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(EnumTroll.MOUNTAIN.helmet));
			this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(EnumTroll.MOUNTAIN.chestplate));
			this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(EnumTroll.MOUNTAIN.leggings));
			this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(EnumTroll.MOUNTAIN.boots));
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(IafItemRegistry.copper_sword));
		} else if (variant == 3) {
			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(IafItemRegistry.deathworm_red_helmet));
			this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(IafItemRegistry.deathworm_red_chestplate));
			this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(IafItemRegistry.deathworm_red_leggings));
			this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(IafItemRegistry.deathworm_red_boots));
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(IafItemRegistry.copper_sword));
		} else if (variant == 4) {
			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(IafItemRegistry.deathworm_white_helmet));
			this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(IafItemRegistry.deathworm_white_chestplate));
			this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(IafItemRegistry.deathworm_white_leggings));
			this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(IafItemRegistry.deathworm_white_boots));
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(IafItemRegistry.copper_sword));
		}
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		this.spells.addAll(Arrays.asList(IFSPSpells.SHACKLES, IFSPSpells.NO_ESCAPE, IFSPSpells.DRAGONREND, IFSPSpells.DRAGONBANE, IFSPSpells.LIVE_WIRE, IFSPSpells.SERPENT_SLAYER));
		ItemStack wand = new ItemStack(ItemWand.getWand(Tier.MASTER, Element.EARTH));
		WandHelper.setSpells(wand, this.spells.toArray(new Spell[0]));
		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, wand);
		this.getVariantEquipment(this.getRNG().nextInt(5));
		for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
			this.setDropChance(slot, 0.0f);
		}
		this.setHealCooldown(50);
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger(VARIANT_KEY, this.getVariant());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setVariant(compound.getInteger(VARIANT_KEY));
	}
}
