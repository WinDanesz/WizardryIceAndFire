package com.windanesz.ifspellpack.network.s2c;

import electroblob.wizardry.util.ParticleBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class S2CPacketLiveWire implements IMessageHandler<S2CPacketLiveWire.Message, IMessage> {

	@Override
	public IMessage onMessage(Message message, MessageContext ctx){
		if(ctx.side.isClient()){
			net.minecraft.client.Minecraft.getMinecraft().addScheduledTask(() -> {
				ParticleBuilder.create(ParticleBuilder.Type.SPARK).pos(message.vec3d).spawn(Minecraft.getMinecraft().world);
			});
		}
		return null;
	}

	public static class Message implements IMessage {

		private Vec3d vec3d;

		public Message(){
		}

		public Message(Vec3d vec3d){
			this.vec3d = vec3d;
		}

		@Override
		public void fromBytes(ByteBuf buf){
			this.vec3d = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
		}

		@Override
		public void toBytes(ByteBuf buf){
			buf.writeDouble(vec3d.x);
			buf.writeDouble(vec3d.y);
			buf.writeDouble(vec3d.z);
		}
	}
}
