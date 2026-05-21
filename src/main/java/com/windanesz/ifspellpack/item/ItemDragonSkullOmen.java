package com.windanesz.ifspellpack.item;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.construct.EntityDragonSkullOmen;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.item.IMultiTexturedItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDragonSkullOmen extends ItemIFSP implements IMultiTexturedItem {

	public static final String STAGE_KEY = "Stage";
	public static final String DRAGON_AGE_KEY = "DragonAge";

	public ItemDragonSkullOmen() {
		this.maxStackSize = 1;
		this.setHasSubtypes(true);
	}

	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
		itemStack.setTagCompound(new NBTTagCompound());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			items.add(new ItemStack(this, 1, 0));
			items.add(new ItemStack(this, 1, 1));
			items.add(new ItemStack(this, 1, 2));
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int f, boolean f1) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger(STAGE_KEY, 4);
			stack.getTagCompound().setInteger(DRAGON_AGE_KEY, 75);
		}
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		String iceorfire = stack.getMetadata() == 0 ? "dragon.fire" : stack.getMetadata() == 1 ? "dragon.ice" : "dragon.lightning";
		tooltip.add(I18n.format(iceorfire));
		if (stack.getTagCompound() != null) {
			tooltip.add(I18n.format("dragon.stage") + stack.getTagCompound().getInteger(STAGE_KEY));
		}
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if (stack.getTagCompound() != null) {
			EntityDragonSkullOmen skull = new EntityDragonSkullOmen(worldIn);
			skull.setType(stack.getMetadata());
			skull.setStage(stack.getTagCompound().getInteger(STAGE_KEY));
			skull.setDragonAge(stack.getTagCompound().getInteger(DRAGON_AGE_KEY));
			skull.setOwnerId(player.getUniqueID());
			BlockPos offset = pos.offset(side, 1);
			skull.setLocationAndAngles(offset.getX() + 0.5, offset.getY(), offset.getZ() + 0.5, 0, 0);
			float yaw = player.rotationYaw;
			if (side != EnumFacing.UP) {
				yaw = player.getHorizontalFacing().getHorizontalAngle();
			}
			skull.setYaw(yaw);
			if (!worldIn.isRemote) {
				worldIn.spawnEntity(skull);
			}
			if (!player.capabilities.isCreativeMode) {
				stack.shrink(1);
			}
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
	public ResourceLocation getModelName(ItemStack stack) {
		int metadata = stack.getMetadata();
		if (metadata == 0) {
			return new ResourceLocation(IFSpellPack.MODID, "dragon_skull_omen_fire");
		} else if (metadata == 1) {
			return new ResourceLocation(IFSpellPack.MODID, "dragon_skull_omen_ice");
		} else {
			return new ResourceLocation(IFSpellPack.MODID, "dragon_skull_omen_lightning");
		}
	}
}
