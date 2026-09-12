package com.windanesz.ifspellpack.network.s2c;

import com.windanesz.ifspellpack.client.gui.GuiCallBeast;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class S2CPacketTeleport implements IMessageHandler<S2CPacketTeleport.Message, IMessage> {

	@Override
	public IMessage onMessage(Message message, MessageContext ctx){
		if(ctx.side.isClient()){
			Minecraft.getMinecraft().addScheduledTask(() -> {
				Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.id);
				entity.lastTickPosX = message.x;
				entity.lastTickPosY = message.y;
				entity.lastTickPosZ = message.z;
				entity.prevPosX = message.x;
				entity.prevPosY = message.y;
				entity.prevPosZ = message.z;
			});
		}
		return null;
	}

	public static class Message implements IMessage {

		private int id;
		private double x;
		private double y;
		private double z;

		public Message(){
		}

		public Message(int id, double x, double y, double z) {
			this.id = id;
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public void fromBytes(ByteBuf buf){
			this.id = buf.readInt();
			this.x = buf.readDouble();
			this.y = buf.readDouble();
			this.z = buf.readDouble();
		}

		@Override
		public void toBytes(ByteBuf buf){
			buf.writeInt(this.id);
			buf.writeDouble(this.x);
			buf.writeDouble(this.y);
			buf.writeDouble(this.z);
		}
	}
}
