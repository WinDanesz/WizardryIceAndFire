package com.windanesz.ifspellpack.network.c2s;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.spell.CallBeast;
import com.windanesz.ifspellpack.world.WorldData;
import electroblob.wizardry.data.WizardData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Map;
import java.util.UUID;

public class C2SPacketSummonBeast implements IMessageHandler<C2SPacketSummonBeast.Message, IMessage> {

	@Override
	public IMessage onMessage(Message message, MessageContext ctx){
		if(ctx.side.isServer()){
			final EntityPlayerMP player = ctx.getServerHandler().player;
			player.getServerWorld().addScheduledTask(() -> {
				WizardData wizardData = WizardData.get(player);
				if (wizardData != null) {
					Map<Integer, String> mounts = wizardData.getVariable(CallBeast.MOUNTS);
					if (mounts != null) {
						UUID uuid = null;
						for (Map.Entry<Integer, String> entry : mounts.entrySet()) {
							if (entry.getKey() == message.type) {
								try {
									//Check if the String is a valid UUID
									uuid = UUID.fromString(entry.getValue());
								} catch (IllegalArgumentException e) {
									//Keep the UUID as null if it isn't
								}
							}
						}
						if (uuid != null) {
							World world = player.world;
							Entity entity = world.getMinecraftServer().getEntityFromUuid(uuid);
							if (entity != null) {
								if (!CallBeast.summonBeast(player, entity)) {
									player.sendStatusMessage(new TextComponentTranslation("spell.ifspellpack:call_beast.no_space", entity.getDisplayName()), true);
								}
							} else {
								WorldData entityPosData = WorldData.get(world);
								if (entityPosData != null) {
									BlockPos pos = entityPosData.getEntityPos(uuid);
									if (pos == null) {
										mounts.put(message.type, CallBeast.DEAD);
										wizardData.setVariable(CallBeast.MOUNTS, mounts);
										player.sendStatusMessage(new TextComponentTranslation("spell.ifspellpack:call_beast.dead"), true);
									} else {
										ForgeChunkManager.Ticket ticket = ForgeChunkManager.requestPlayerTicket(IFSpellPack.instance, player.getName(), world, ForgeChunkManager.Type.NORMAL);
										ForgeChunkManager.forceChunk(ticket, new ChunkPos(pos));
										entity = world.getMinecraftServer().getEntityFromUuid(uuid);
										if (entity != null) {
											if (!CallBeast.summonBeast(player, entity)) {
												ForgeChunkManager.releaseTicket(ticket);
												player.sendStatusMessage(new TextComponentTranslation("spell.ifspellpack:call_beast.no_space", entity.getDisplayName()), true);
											} else {
												ForgeChunkManager.releaseTicket(ticket);
											}
										}
									}
								}
							}
						}
					}
				}
			});

		}
		return null;
	}

	public static class Message implements IMessage {

		private int type;

		public Message(){
		}

		public Message(int type){
			this.type = type;
		}

		@Override
		public void fromBytes(ByteBuf buf){
			this.type = buf.readInt();
		}

		@Override
		public void toBytes(ByteBuf buf){
			buf.writeInt(this.type);
		}
	}
}
