package com.windanesz.ifspellpack.item;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.client.IFSPGuiHandler;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.util.InventoryUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCharmDwarvenPocketForge extends ItemArtefactIFSP {

	public static final List<Item> VALID_ITEMS_OLD = Arrays.asList(Items.IRON_INGOT, Items.GOLD_INGOT, Items.DIAMOND, IafItemRegistry.silverIngot, IafItemRegistry.dragonsteel_fire_ingot, IafItemRegistry.dragonsteel_ice_ingot, IafItemRegistry.copperIngot, IafItemRegistry.dragonsteel_lightning_ingot);

	public static final Map<Item, Item> VALID_ITEMS = new HashMap<>();
	public static final int INGOTS_CONSUMED_PER_USE = 1;

	@Override
	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
		return super.shouldCauseBlockBreakReset(oldStack, newStack);
	}

	public static final int MAX_STACK_SIZE = 64;

	public ItemCharmDwarvenPocketForge() {
		super(EnumRarity.RARE, Type.CHARM);
		this.setMaxDamage(MAX_STACK_SIZE);
	}

	public static void init() {
		for (String string : IFSpellPack.settings.dwarvenPocketForgeValidItems) {
			String[] args = string.split(" ");
			if (args.length != 2) {
				IFSpellPack.logger.warn("Invalid entry in dwarven pocket forge: {}", string);
			} else {
				Item material = Item.REGISTRY.getObject(new ResourceLocation(args[0]));
				if (material == null) {
					IFSpellPack.logger.warn("Invalid material item in dwarven pocket forge: {}", string);
				} else {
					Item armor = Item.REGISTRY.getObject(new ResourceLocation(args[1]));
					if (armor == null) {
						IFSpellPack.logger.warn("Invalid armor item in dwarven pocket forge: {}", string);
					} else {
						VALID_ITEMS.put(material, armor);
					}
				}
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.openGui(IFSpellPack.MODID, IFSPGuiHandler.DWARVEN_POCKET_FORGE, world, hand.ordinal(), 0, 0);
		return super.onItemRightClick(world, player, hand);
	}

	public static boolean armorDragon(EntityPlayer player, EntityDragonBase dragon, ItemStack stack) {
		if (stack.getItem() instanceof ItemCharmDwarvenPocketForge) {
			NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
			if (nbt.hasKey("Items")) {
				NBTTagList items = nbt.getTagList("Items", 10);
				NBTTagCompound nbttagcompound = items.getCompoundTagAt(0);
				ItemStack ingotStack = new ItemStack(nbttagcompound);
				if (ingotStack.getCount() >= INGOTS_CONSUMED_PER_USE) {
					Item armor = armorForIngot(ingotStack.getItem());
					if (armor != null) {
						EntityEquipmentSlot[] slots = InventoryUtils.ARMOUR_SLOTS;
						for (int i = 0; i < slots.length; i++) {
							dragon.setItemStackToSlot(slots[i], new ItemStack(armor, 1, i));
						}
						//Do not consume ingots if the player is in creative
						if (player.isCreative()) {
							return true;
						}
						ingotStack.shrink(INGOTS_CONSUMED_PER_USE);
						items.set(0, ingotStack.writeToNBT(new NBTTagCompound()));
						nbt.setTag("Items", items);
						stack.setTagCompound(nbt);
						return true;
					}
				}
			}
		}
		return false;
	}

	@Nullable
	public static Item armorForIngot(Item item) {
		return VALID_ITEMS.get(item);
	}

	@Override
	public int getDamage(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Items")) {
			NBTTagList items = stack.getTagCompound().getTagList("Items", 10);
			NBTTagCompound nbttagcompound = items.getCompoundTagAt(0);
			ItemStack ingots = new ItemStack(nbttagcompound);
			return MAX_STACK_SIZE - ingots.getCount();
		}
		return MAX_STACK_SIZE;
	}

	@Override
	public boolean isDamaged(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Items")) {
			NBTTagList items = stack.getTagCompound().getTagList("Items", 10);
			NBTTagCompound nbttagcompound = items.getCompoundTagAt(0);
			ItemStack ingots = new ItemStack(nbttagcompound);
			return ingots.getCount() < MAX_STACK_SIZE;
		}
		return true;
	}

	public static boolean isItemValid(Item item) {
		return VALID_ITEMS.containsKey(item);
	}
}
