package com.windanesz.ifspellpack.client;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.client.gui.GuiDwarvenPocketForge;
import com.windanesz.ifspellpack.client.gui.GuiSlayerMerchant;
import com.windanesz.ifspellpack.entity.ISlayerMerchant;
import com.windanesz.ifspellpack.inventory.ContainerDwarvenPocketForge;
import com.windanesz.ifspellpack.inventory.ContainerSlayerMerchant;
import com.windanesz.ifspellpack.inventory.InventoryDwarvenPocketForge;
import com.windanesz.ifspellpack.network.s2c.S2CPacketGuiSlayerMerchantSync;
import com.windanesz.ifspellpack.registry.IFSPPackets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class IFSPGuiHandler implements IGuiHandler {

	private static int nextGuiId = 0;

	public static final int DWARVEN_POCKET_FORGE = nextGuiId++;
	public static final int SLAYER_TRADE = nextGuiId++;

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == DWARVEN_POCKET_FORGE) {
			ItemStack stack = player.getHeldItem(EnumHand.values()[x]);
			InventoryDwarvenPocketForge inventory = new InventoryDwarvenPocketForge(stack);
			return new ContainerDwarvenPocketForge(player.inventory, inventory, player);
		} else if (id == SLAYER_TRADE) {
			Entity entity = world.getEntityByID(x);
			if (entity instanceof ISlayerMerchant) {
				ISlayerMerchant slayerMerchant = (ISlayerMerchant)entity;
				IFSPPackets.net.sendToAll(new S2CPacketGuiSlayerMerchantSync.Message(x, slayerMerchant.getTrades()));
				return new ContainerSlayerMerchant(player.inventory, slayerMerchant);
			} else {
				IFSpellPack.logger.warn("Trading partner is not an ISlayerMerchant!");
				return null;
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == DWARVEN_POCKET_FORGE) {
			ItemStack stack = player.getHeldItem(EnumHand.values()[x]);
			InventoryDwarvenPocketForge inventory = new InventoryDwarvenPocketForge(stack);
			return new GuiDwarvenPocketForge(inventory, player);
		} else if (id == SLAYER_TRADE) {
			Entity entity = world.getEntityByID(x);
			if (entity instanceof ISlayerMerchant) {
				ISlayerMerchant slayerMerchant = (ISlayerMerchant)entity;
				return new GuiSlayerMerchant(player.inventory, slayerMerchant);
			} else {
				IFSpellPack.logger.warn("Trading partner is not an ISlayerMerchant!");
				return null;
			}
		}
		return null;
	}

}


