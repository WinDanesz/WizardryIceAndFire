package com.windanesz.ifspellpack;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = IFSpellPack.MODID, name = "IFSpellPack") // No fancy configs here so we can use the annotation, hurrah!
public class Settings {

	public boolean dragonBreathSpellsPowerForge = generalSettings.dragonBreathSpellsPowerForge;
	public float dragonrendBonusDamage = generalSettings.dragonrendBonusDamage;
	public float iafPotencyModifier = generalSettings.iafPotencyModifier;
	public String schoolinfo = generalSettings.schoolinfo;

	@Config.Name("General Settings")
	public static GeneralSettings generalSettings = new GeneralSettings();

	public float heartOfDreadDurationMultiplier = artefactSettings.heartOfDreadDurationMultiplier;

	@Config.Name("Artefact Settings")
	public static ArtefactSettings artefactSettings = new ArtefactSettings();

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

		@Config.Name("Dragon Breath Spells Power Forge")
		@Config.Comment("Do dragon breath spells power their respective forges")
		public boolean dragonBreathSpellsPowerForge = true;

		@Config.Name("Dragonrend Damage Increase")
		@Config.Comment("Bonus damage applied to dragons with the dragonrend effect with the horn of dovakriid")
		public float dragonrendBonusDamage = 0.15f;

		@Config.Name("IAF Potency Modifier")
		@Config.Comment("Multiplies the global potency for all IFSpellpack spells")
		public float iafPotencyModifier = 1f;

		@Config.Name("School Info Name")
		@Config.Comment("The command for searching school info")
		public String schoolinfo = "schoolinfo";

	}

	public static class ArtefactSettings {

		@Config.Name("Heart of Dread Duration Increase")
		@Config.Comment("Duration increase of the heart of dread charm")
		public float heartOfDreadDurationMultiplier = 2f;

	}
}
