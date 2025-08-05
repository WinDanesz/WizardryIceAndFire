package com.windanesz.ifspellpack.registry;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.network.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class IFSPPackets {

	public static SimpleNetworkWrapper net;
	private static int nextPacketId = 0;

	public static void initPackets() {
		net = NetworkRegistry.INSTANCE.newSimpleChannel(IFSpellPack.MODID.toUpperCase());
		registerMessage(S2CPacketCallBeast.class, S2CPacketCallBeast.Message.class);
		registerMessage(S2CPacketCallDragon.class, S2CPacketCallDragon.Message.class);
		registerMessage(S2CPacketLiveWire.class, S2CPacketLiveWire.Message.class);
		registerMessage(C2SPacketSummonBeast.class, C2SPacketSummonBeast.Message.class);
		registerMessage(C2SPacketSummonDragon.class, C2SPacketSummonDragon.Message.class);
		registerMessage(C2SPacketPixieWingGlider.class, C2SPacketPixieWingGlider.Message.class);
	}

	private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> packet, Class<REQ> message) {
		net.registerMessage(packet, message, nextPacketId, Side.CLIENT);
		net.registerMessage(packet, message, nextPacketId, Side.SERVER);
		nextPacketId++;
	}

}
