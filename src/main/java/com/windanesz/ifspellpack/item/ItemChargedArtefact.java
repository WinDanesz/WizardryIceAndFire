package com.windanesz.ifspellpack.item;

import electroblob.wizardry.item.IWorkbenchItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemChargedArtefact extends ItemArtefactIFSP implements IWorkbenchItem {

	private final List<Item> chargeItems;
	private final int chargePerItem;
	private final int chargePerUse;
	public static final Set<Item> VALID_ITEMS = new HashSet<>();

	public ItemChargedArtefact(EnumRarity rarity, Type type, int maxCharges, List<Item> chargeItems, int chargePerItem, int chargePerUse) {
		super(rarity, type);
		this.setMaxDamage(maxCharges);
		this.chargeItems = chargeItems;
		this.chargePerItem = chargePerItem;
		this.chargePerUse = chargePerUse;
		VALID_ITEMS.addAll(chargeItems);
		this.addReadinessPropertyOverride();
	}

	public void addReadinessPropertyOverride() {
		this.addPropertyOverride(new ResourceLocation("charged"), new IItemPropertyGetter() {
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				return isCharged(stack) ? 0f : 1f;
			}
		});
	}

	public List<Item> getChargeItems() {
		return this.chargeItems;
	}

	public int getChargePerItem() {
		return this.chargePerItem;
	}

	public int getChargePerUse() {
		return this.chargePerUse;
	}

	//Consumes charge with default item charge value
	public static boolean consumeCharge(ItemStack itemStack) {
		if (itemStack.getItem() instanceof ItemChargedArtefact) {
			ItemChargedArtefact itemChargedArtefact = (ItemChargedArtefact)itemStack.getItem();
			int remainingCharge = itemChargedArtefact.getMaxDamage(itemStack) - itemChargedArtefact.getDamage(itemStack);
			if (remainingCharge >= itemChargedArtefact.chargePerUse && remainingCharge > 0) {
				itemChargedArtefact.setDamage(itemStack, itemChargedArtefact.getDamage(itemStack) + itemChargedArtefact.chargePerUse);
				return true;
			}
		}
		return false;
	}

	//Consumes charge with modifiable charge value
	public static boolean consumeCharge(ItemStack itemStack, int charge) {
		if (itemStack.getItem() instanceof ItemChargedArtefact) {
			ItemChargedArtefact itemChargedArtefact = (ItemChargedArtefact)itemStack.getItem();
			int remainingCharge = itemChargedArtefact.getMaxDamage(itemStack) - itemChargedArtefact.getDamage(itemStack);
			if (remainingCharge >= charge && remainingCharge > 0) {
				itemChargedArtefact.setDamage(itemStack, itemChargedArtefact.getDamage(itemStack) + charge);
				return true;
			}
		}
		return false;
	}

	//Taken from ItemWand.getDistributedCost
	public static int getDistributedCost(int cost, int castingTick){
		int partialCost;
		if(castingTick % 20 == 0){
			partialCost = cost / 2 + cost % 2;
		}else if(castingTick % 10 == 0){
			partialCost = cost / 2;
		}else{
			partialCost = 0;
		}
		return partialCost;
	}

	public boolean isCharged(ItemStack stack) {
		return stack.getItemDamage() < stack.getMaxDamage();
	}

	@Override
	public int getSpellSlotCount(ItemStack stack) {
		return 0;
	}

	@Override
	public boolean onApplyButtonPressed(EntityPlayer player, Slot centre, Slot crystals, Slot upgrade, Slot[] spellBooks) {
		if (this.getChargeItems().contains(crystals.getStack().getItem())) {
			int chargePerItem = this.getChargePerItem();
			int chargeCount = crystals.getStack().getCount() * chargePerItem;
			int chargeMissing = this.getDamage(centre.getStack());
			if (chargeMissing == 0) {
				return false;
			}
			if (chargeCount <= chargeMissing) {
				centre.getStack().setItemDamage(chargeMissing - chargeCount);
				crystals.decrStackSize(crystals.getStack().getCount());
			} else {
				centre.getStack().setItemDamage(0);
				crystals.decrStackSize((int)Math.ceil(((double)chargeMissing) / chargePerItem));
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean showTooltip(ItemStack stack) {
		return false;
	}
}
