package com.windanesz.ifspellpack.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.item.IConjuredItem;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.util.InventoryUtils;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLightwardArmor extends ItemArmor implements IConjuredItem {

	public static final int BURN_TIME_PER_ARMOR = 2;

	public ItemLightwardArmor(EntityEquipmentSlot armourType){
		super(IafItemRegistry.silverMetal, 1, armourType);
		setCreativeTab(null);
		setMaxDamage(1200);
	}

	public static void igniteAttackers(EntityLivingBase defender, EntityLivingBase attacker) {
		int burnTime = 0;
		for (ItemStack armor : defender.getArmorInventoryList()) {
			if (armor.getItem() instanceof ItemLightwardArmor) {
				burnTime += BURN_TIME_PER_ARMOR;
			}
		}
		if (burnTime > 0) {
			attacker.setFire(burnTime);
		}
	}

	@Override
	public int getMaxDamage(ItemStack stack){
		return this.getMaxDamageFromNBT(stack, Spells.conjure_armour);
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack){
		return IConjuredItem.getTimerBarColour(stack);
	}

	// Overridden to stop the enchantment trick making the name turn blue.
	@Override
	public EnumRarity getRarity(ItemStack stack){
		return EnumRarity.COMMON;
	}

	@Override
	// This method allows the code for the item's timer to be greatly simplified by damaging it directly from
	// onUpdate() and removing the workaround that involved WizardData and all sorts of crazy stuff.
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){

		if(!oldStack.isEmpty() || !newStack.isEmpty()){
			// We only care about the situation where we specifically want the animation NOT to play.
			if(oldStack.getItem() == newStack.getItem() && !slotChanged) return false;
		}

		return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
	}

/*	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack){
		int damage = stack.getItemDamage();
		if(damage > stack.getMaxDamage()) player.inventory.clearMatchingItems(this, -1, 1, null);
		stack.setItemDamage(damage + 1);
	}*/

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected){
		int damage = stack.getItemDamage();
		if(damage > stack.getMaxDamage()) InventoryUtils.replaceItemInInventory(entity, slot, stack, ItemStack.EMPTY);
		stack.setItemDamage(damage + 1);
	}

	@Override
	public boolean getIsRepairable(ItemStack stack, ItemStack par2ItemStack){
		return false;
	}

	@Override
	public int getItemEnchantability(){
		return 0;
	}

	@Override
	public boolean isEnchantable(ItemStack stack){
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book){
		return false;
	}

	// Cannot be dropped
	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player){
		return false;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type){
		if (slot == EntityEquipmentSlot.LEGS) {
			return IFSpellPack.MODID + ":textures/armor/lightward_armor_legs.png";
		}
		return IFSpellPack.MODID + ":textures/armor/lightward_armor.png";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, net.minecraft.client.model.ModelBiped _default) {
		return (ModelBiped) IceAndFire.PROXY.getArmorModel(armorSlot == EntityEquipmentSlot.LEGS ? 15 : 14);
	}
}
