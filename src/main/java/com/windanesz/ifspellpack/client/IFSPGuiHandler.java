package com.windanesz.ifspellpack.client;

import com.windanesz.ifspellpack.client.gui.GuiCallBeast;
import com.windanesz.ifspellpack.spell.CallBeast;
import electroblob.wizardry.data.WizardData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import java.util.Map;
import java.util.UUID;

public class IFSPGuiHandler implements IGuiHandler {

	/**
	 * Incrementable index for the gui ID
	 */
	private static int nextGuiId = 0;

	public static final int CALL_BEAST = nextGuiId++;


	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

}


