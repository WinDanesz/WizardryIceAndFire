package com.windanesz.ifspellpack.network;

import baubles.api.BaublesApi;
import com.windanesz.ifspellpack.item.ItemChargedArtefact;
import com.windanesz.ifspellpack.registry.IFSPItems;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.util.SpellModifiers;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketPixieWingGlider implements IMessageHandler<PacketPixieWingGlider.Message, IMessage> {

	@Override
	public IMessage onMessage(Message message, MessageContext ctx){
		if(ctx.side.isServer()){
			final EntityPlayerMP player = ctx.getServerHandler().player;
			player.getServerWorld().addScheduledTask(() -> {
				if (ItemArtefact.isArtefactActive(player, IFSPItems.BODY_PIXIE_WING_GLIDER)) {
					if (player.isCreative() || ItemChargedArtefact.consumeCharge(BaublesApi.getBaublesHandler(player).getStackInSlot(5))) {
						Spells.glide.cast(player.world, player, EnumHand.MAIN_HAND, player.ticksExisted, new SpellModifiers());
					}
				}
			});

		}
		return null;
	}

	public static class Message implements IMessage {

		private boolean active;

		public Message(){
		}

		public Message(boolean active){
			this.active = active;
		}

		@Override
		public void fromBytes(ByteBuf buf){
			this.active = buf.readBoolean();
		}

		@Override
		public void toBytes(ByteBuf buf){
			buf.writeBoolean(this.active);
		}
	}
}
