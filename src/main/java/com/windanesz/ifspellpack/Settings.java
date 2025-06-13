package com.windanesz.ifspellpack;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = IFSpellPack.MODID, name = "IFSpellPack") // No fancy configs here so we can use the annotation, hurrah!
public class Settings {


	@Config.Name("General Settings")
	public static GeneralSettings generalSettings = new GeneralSettings();

	@SuppressWarnings("unused")
	@Mod.EventBusSubscriber(modid = IFSpellPack.MODID)
	private static class EventHandler {
		/**
		 * Inject the new values and save to the config file when the config has been changed from the GUI.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(IFSpellPack.MODID)) {
				ConfigManager.sync(IFSpellPack.MODID, Config.Type.INSTANCE);
			}
		}
	}

	public static class GeneralSettings {

		@Config.Name("Unused placeholder")
		@Config.Comment("Unused placeholder desc")
		@Config.RequiresMcRestart
		public boolean unused_placeholder = true;

	}
}
