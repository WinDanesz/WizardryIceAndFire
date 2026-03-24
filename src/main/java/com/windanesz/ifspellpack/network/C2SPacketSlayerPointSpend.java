package com.windanesz.ifspellpack.network;

import com.windanesz.ifspellpack.inventory.ContainerSlayerMerchant;
import com.windanesz.ifspellpack.world.SlayerTracker;
import electroblob.wizardry.data.WizardData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class C2SPacketSlayerPointSpend implements IMessageHandler<C2SPacketSlayerPointSpend.Message, IMessage> {

	@Override
	public IMessage onMessage(Message message, MessageContext ctx){
		if(ctx.side.isServer()){
			final EntityPlayerMP player = ctx.getServerHandler().player;
			player.getServerWorld().addScheduledTask(() -> {
				WizardData data = WizardData.get(player);
				if (data != null) {
					data.setVariable(SlayerTracker.POINT_TRACKER, message.points);
				}
			});
		}
		return null;
	}

	public static class Message implements IMessage {

		private int points;

		public Message(){
		}

		public Message(int points){
			this.points = points;
		}

		@Override
		public void fromBytes(ByteBuf buf){
			this.points = buf.readInt();
		}

		@Override
		public void toBytes(ByteBuf buf){
			buf.writeInt(this.points);
		}
	}
}
