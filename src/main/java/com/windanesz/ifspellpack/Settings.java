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

	public boolean dragonBreathSpellsPowerForge = generalSettings.dragonBreathSpellsPowerForge;
	public float dragonrendBonusDamage = generalSettings.dragonrendBonusDamage;
	public float iafPotencyModifier = generalSettings.iafPotencyModifier;
	public String schoolInfo = generalSettings.schoolInfo;
	public String slayerTrackerInfo = generalSettings.slayerTrackerInfo;
	public String[] slayerTrackerEntities = generalSettings.slayerTrackerEntities;

	@Config.Name("Artefact Settings")
	public static ArtefactSettings artefactSettings = new ArtefactSettings();

	public float heartOfDreadDurationMultiplier = artefactSettings.heartOfDreadDurationMultiplier;

	@Config.Name("World Generation Settings")
	public static WorldGenerationSettings worldGenerationSettings = new WorldGenerationSettings();

	public int slayerVillageChance = worldGenerationSettings.slayerVillageChance;

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
		public boolean dragonBreathSpellsPowerForge = false;

		@Config.Name("Dragonrend Damage Increase")
		@Config.Comment("Bonus damage applied to dragons with the dragonrend effect with the horn of dovahkriid")
		public float dragonrendBonusDamage = 0.15f;

		@Config.Name("IAF Potency Modifier")
		@Config.Comment("Multiplies the global potency for all " + IFSpellPack.NAME + " spells")
		public float iafPotencyModifier = 1f;

		@Config.Name("School Info Name")
		@Config.Comment("The command for searching school info")
		public String schoolInfo = "schoolinfo";

		@Config.Name("Slayer Tracker Name")
		@Config.Comment("The command for searching slayer tracker stats")
		public String slayerTrackerInfo = "slayertracker";

		@Config.Name("Slayer Monster List")
		@Config.Comment("The list of monsters to be tracked for slayer and points per kill")
		@Config.RequiresWorldRestart
		public String[] slayerTrackerEntities = new String[]{"iceandfire:firedragon 5", "iceandfire:icedragon 5", "iceandfire:hippogryph 2", "iceandfire:gorgon 3", "iceandfire:if_pixie 1", "iceandfire:cyclops 3", "iceandfire:siren 2", "iceandfire:hippocampus 1", "iceandfire:deathworm 2", "iceandfire:if_cockatrice 2", "iceandfire:stymphalianbird 2", "iceandfire:if_troll 2", "iceandfire:myrmex_worker 1", "iceandfire:myrmex_soldier 2", "iceandfire:myrmex_sentinel 3", "iceandfire:myrmex_royal 4", "iceandfire:myrmex_queen 5", "iceandfire:seaserpent 4", "iceandfire:dread_thrall 1", "iceandfire:dread_ghoul 2", "iceandfire:dread_beast 2", "iceandfire:dread_scuttler 3", "iceandfire:dread_lich 4", "iceandfire:dread_knight 3", "iceandfire:dread_horse 1", "iceandfire:if_hydra 4", "iceandfire:lightningdragon 5", "iceandfire:blackfrost 5", "iceandfire:dread_queen 5"};

	}

	public static class ArtefactSettings {

		@Config.Name("Heart of Dread Duration Increase")
		@Config.Comment("Duration increase of the heart of dread charm")
		public float heartOfDreadDurationMultiplier = 2f;

	}

	public static class WorldGenerationSettings {

		@Config.Name("Slayer Village Spawn Chance")
		@Config.Comment("Rarity of slayer villages. 1 in this many chunks will contain a slayer village, meaning higher numbers are rarer.")
		public int slayerVillageChance = 1;
		//public int slayerVillageChance = 100;

	}
}
