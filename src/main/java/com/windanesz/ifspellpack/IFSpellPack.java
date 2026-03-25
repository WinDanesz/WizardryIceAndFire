package com.windanesz.ifspellpack;

import com.windanesz.ifspellpack.client.IFSPGuiHandler;
import com.windanesz.ifspellpack.command.CommandSchoolInfo;
import com.windanesz.ifspellpack.command.CommandSlayerTracker;
import com.windanesz.ifspellpack.item.ItemCharmDwarvenPocketForge;
import com.windanesz.ifspellpack.registry.IFSPPackets;
import com.windanesz.ifspellpack.registry.IFSPLoot;
import com.windanesz.ifspellpack.registry.IFSPVillagerProfessions;
import com.windanesz.ifspellpack.school.School;
import com.windanesz.ifspellpack.world.MapGenSlayerVillage;
import com.windanesz.ifspellpack.world.SlayerTracker;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(modid = IFSpellPack.MODID, name = IFSpellPack.NAME, version = IFSpellPack.VERSION, acceptedMinecraftVersions = "[" + IFSpellPack.MCVERSION + "]",
		dependencies = "required-after:mixinbooter;required-after:ebwizardry@[" + IFSpellPack.WIZARDRY_VERSION + ",4.4);required-after:wizardryutils;after:pointer")
public class IFSpellPack {

	public static final String MODID = "ifspellpack";
	public static final String NAME = "Wizardry: Ice and Fire";
	public static final String VERSION = Tags.VERSION;
	public static final String MCVERSION = Tags.MCVERSION;
	public static final String WIZARDRY_VERSION = Tags.WIZARDRY_VERSION;

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
		GameRegistry.registerWorldGenerator(new MapGenSlayerVillage(60), 20);
		proxy.registerParticles();
		proxy.init();
		IFSPPackets.initPackets();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new IFSPGuiHandler());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.initialiseLayers();
		School.init();
		IFSPVillagerProfessions.init();
		SlayerTracker.init();
		ItemCharmDwarvenPocketForge.init();
	}

	@EventHandler
	public void serverStartup(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandSchoolInfo());
		event.registerServerCommand(new CommandSlayerTracker());
	}
}
