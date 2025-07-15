package com.windanesz.ifspellpack.event;

import baubles.api.BaublesApi;
import com.windanesz.ifspellpack.item.ItemChargedArtefact;
import com.windanesz.ifspellpack.network.IFSPPacketHandler;
import com.windanesz.ifspellpack.network.PacketPixieWingGlider;
import com.windanesz.ifspellpack.registry.IFSPItems;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class IFSPClientEvents {

	@SubscribeEvent
	public static void onInputUpdateEvent(InputUpdateEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		//Pixie wing glider
		if (event.getMovementInput().jump && ItemArtefact.isArtefactActive(player, IFSPItems.BODY_PIXIE_WING_GLIDER)) {
			if (player.isCreative() || ItemChargedArtefact.consumeCharge(BaublesApi.getBaublesHandler(player).getStackInSlot(5))) {
				IMessage msg = new PacketPixieWingGlider.Message(true);
				IFSPPacketHandler.net.sendToServer(msg);
				Spells.glide.cast(player.world, player, EnumHand.MAIN_HAND, player.ticksExisted, new SpellModifiers());
			}
		}
	}

}
