package com.windanesz.ifspellpack;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = IFSpellPack.MODID, name = "IFSpellPack") // No fancy configs here so we can use the annotation, hurrah!
public class Settings {

	public float dragonrendBonusDamage = generalSettings.dragonrendBonusDamage;
	public float iafPotencyModifier = generalSettings.iafPotencyModifier;

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

		@Config.Name("Dragonrend Damage Increase")
		@Config.Comment("Bonus damage applied to dragons with the dragonrend effect with the horn of dovakriid")
		//@Config.RequiresMcRestart do I need this?
		public float dragonrendBonusDamage = 0.15f;

		@Config.Name("IAF Potency Modifier")
		@Config.Comment("Multiplies the global potency for all IFSpellpack spells")
		//@Config.RequiresMcRestart do I need this?
		public float iafPotencyModifier = 1f;

	}
}
