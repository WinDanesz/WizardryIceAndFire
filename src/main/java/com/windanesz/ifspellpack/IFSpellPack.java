package com.windanesz.ifspellpack;

import com.windanesz.ifspellpack.client.IFSPGuiHandler;
import com.windanesz.ifspellpack.command.CommandSchoolInfo;
import com.windanesz.ifspellpack.network.IFSPPacketHandler;
import com.windanesz.ifspellpack.registry.IFSPLoot;
import com.windanesz.ifspellpack.registry.IFSPVillagerProfessions;
import com.windanesz.ifspellpack.school.School;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(modid = IFSpellPack.MODID, name = IFSpellPack.NAME, version = "@VERSION@", acceptedMinecraftVersions = "[@MCVERSION@]",
		dependencies = "required-after:mixinbooter;required-after:ebwizardry@[@WIZARDRY_VERSION@,4.4);required-after:wizardryutils;after:pointer")
public class IFSpellPack {

	public static final String MODID = "ifspellpack";
	public static final String NAME = "Wizardry: Ice and Fire";

	public static final Random rand = new Random();

	public static Logger logger;
	public static Settings settings;

	// The instance of wizardry that Forge uses.
	@Mod.Instance(IFSpellPack.MODID)
	public static IFSpellPack instance;

	// Location of the proxy code, used by Forge.
	@SidedProxy(clientSide = "com.windanesz.ifspellpack.client.ClientProxy", serverSide = "com.windanesz.ifspellpack.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		settings = new Settings();
		proxy.registerRenderers();

		// Loot
		IFSPLoot.preInit();

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		MinecraftForge.EVENT_BUS.register(instance);
		proxy.registerParticles();
		proxy.init();
		IFSPPacketHandler.initPackets();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new IFSPGuiHandler());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.initialiseLayers();
		School.init();
		IFSPVillagerProfessions.init();
	}

	@EventHandler
	public void serverStartup(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandSchoolInfo());
	}
}
