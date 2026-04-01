package com.windanesz.ifspellpack.network;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.client.gui.GuiSlayerMerchant;
import com.windanesz.ifspellpack.entity.ISlayerMerchant;
import com.windanesz.ifspellpack.entity.SlayerMerchantTradeList;
import electroblob.wizardry.util.ParticleBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class S2CPacketGuiSlayerMerchantSync implements IMessageHandler<S2CPacketGuiSlayerMerchantSync.Message, IMessage> {

	@Override
	public IMessage onMessage(Message message, MessageContext ctx){
		if(ctx.side.isClient()){
			Minecraft.getMinecraft().addScheduledTask(() -> {
				Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.entityId);
				if (entity instanceof ISlayerMerchant) {
					ISlayerMerchant merchant = (ISlayerMerchant)entity;
					merchant.setTrades(message.trades);
				} else {
					IFSpellPack.logger.warn("Entity is not an ISlayerMerchant!");
				}
			});
		}
		return null;
	}

	public static class Message implements IMessage {

		private int entityId;
		private SlayerMerchantTradeList trades;

		public Message() {
		}

		public Message(int entityId, SlayerMerchantTradeList trades) {
			this.entityId = entityId;
			this.trades = trades;
		}

		@Override
		public void fromBytes(ByteBuf buf){
			this.entityId = buf.readInt();
			this.trades = SlayerMerchantTradeList.readFromBuf(buf);
		}

		@Override
		public void toBytes(ByteBuf buf){
			buf.writeInt(this.entityId);
			this.trades.writeToBuf(buf);
		}
	}
}
