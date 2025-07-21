package com.windanesz.ifspellpack.network;

import com.windanesz.ifspellpack.client.gui.GuiCallBeast;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class S2CPacketCallBeast implements IMessageHandler<S2CPacketCallBeast.Message, IMessage> {

	@Override
	public IMessage onMessage(Message message, MessageContext ctx){
		if(ctx.side.isClient()){
			Minecraft.getMinecraft().addScheduledTask(() -> {
				Minecraft.getMinecraft().displayGuiScreen(new GuiCallBeast(message.enabledMounts));
			});
		}
		return null;
	}

	public static class Message implements IMessage {

		private boolean[] enabledMounts;

		public Message(){
		}

		public Message(boolean[] enabledMounts){
			this.enabledMounts = enabledMounts;
		}

		@Override
		public void fromBytes(ByteBuf buf){
			int length = buf.readInt();
			this.enabledMounts = new boolean[length];
			for (int i = 0; i < length; i++) {
				this.enabledMounts[i] = buf.readBoolean();
			}
		}

		@Override
		public void toBytes(ByteBuf buf){
			buf.writeInt(this.enabledMounts.length);
			for (boolean value : this.enabledMounts) {
				buf.writeBoolean(value);
			}
		}
	}
}
