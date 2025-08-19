package com.windanesz.ifspellpack.client;

import com.windanesz.ifspellpack.client.gui.GuiDwarvenPocketForge;
import com.windanesz.ifspellpack.inventory.ContainerDwarvenPocketForge;
import com.windanesz.ifspellpack.inventory.InventoryDwarvenPocketForge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class IFSPGuiHandler implements IGuiHandler {

	private static int nextGuiId = 0;

	public static final int DWARVEN_POCKET_FORGE = nextGuiId++;

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == DWARVEN_POCKET_FORGE) {
			ItemStack stack = player.getHeldItem(EnumHand.values()[x]);
			InventoryDwarvenPocketForge inventory = new InventoryDwarvenPocketForge(stack);
			return new ContainerDwarvenPocketForge(player.inventory, inventory, player);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == DWARVEN_POCKET_FORGE) {
			ItemStack stack = player.getHeldItem(EnumHand.values()[x]);
			InventoryDwarvenPocketForge inventory = new InventoryDwarvenPocketForge(stack);
			return new GuiDwarvenPocketForge(inventory, player);
		}
		return null;
	}

}


