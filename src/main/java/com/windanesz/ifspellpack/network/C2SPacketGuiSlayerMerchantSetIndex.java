package com.windanesz.ifspellpack.network;

import com.windanesz.ifspellpack.inventory.ContainerSlayerMerchant;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class C2SPacketGuiSlayerMerchantSetIndex implements IMessageHandler<C2SPacketGuiSlayerMerchantSetIndex.Message, IMessage> {

	@Override
	public IMessage onMessage(Message message, MessageContext ctx){
		if(ctx.side.isServer()){
			final EntityPlayerMP player = ctx.getServerHandler().player;
			player.getServerWorld().addScheduledTask(() -> {
				Container container = player.openContainer;
				if (container instanceof ContainerSlayerMerchant) {
					((ContainerSlayerMerchant)container).setCurrentRecipeIndex(message.selectedTrade);
					((ContainerSlayerMerchant)container).resetTrade();
				}
			});
		}
		return null;
	}

	public static class Message implements IMessage {

		private int selectedTrade;

		public Message(){
		}

		public Message(int selectedTrade){
			this.selectedTrade = selectedTrade;
		}

		@Override
		public void fromBytes(ByteBuf buf){
			this.selectedTrade = buf.readInt();
		}

		@Override
		public void toBytes(ByteBuf buf){
			buf.writeInt(this.selectedTrade);
		}
	}
}
