package com.windanesz.ifspellpack.network.c2s;

import com.windanesz.ifspellpack.inventory.ContainerSlayerMerchant;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class C2SPacketGuiSlayerMerchantReset implements IMessageHandler<C2SPacketGuiSlayerMerchantReset.Message, IMessage> {

	@Override
	public IMessage onMessage(Message message, MessageContext ctx){
		if(ctx.side.isServer()){
			final EntityPlayerMP player = ctx.getServerHandler().player;
			player.getServerWorld().addScheduledTask(() -> {
				Container container = player.openContainer;
				if (container instanceof ContainerSlayerMerchant) {
					((ContainerSlayerMerchant)container).resetTrade();
				}
			});
		}
		return null;
	}

	public static class Message implements IMessage {

		public Message(){
		}

		@Override
		public void fromBytes(ByteBuf buf){
		}

		@Override
		public void toBytes(ByteBuf buf){
		}
	}
}
