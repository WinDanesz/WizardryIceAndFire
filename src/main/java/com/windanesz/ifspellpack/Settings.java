package com.windanesz.ifspellpack;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

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
	public boolean shouldSlayerKillsScale = generalSettings.shouldSlayerKillsScale;
	public int tradeResetTimer = generalSettings.tradeResetTimer;

	@Config.Name("Artefact Settings")
	public static ArtefactSettings artefactSettings = new ArtefactSettings();

	public float heartOfDreadDurationMultiplier = artefactSettings.heartOfDreadDurationMultiplier;
	public String[] dwarvenPocketForgeValidItems = artefactSettings.dwarvenPocketForgeValidItems;

	@Config.Name("World Generation Settings")
	public static WorldGenerationSettings worldGenerationSettings = new WorldGenerationSettings();

	public int slayerVillageChance = worldGenerationSettings.slayerVillageChance;
	public boolean cultistPatrolNeedsOwner = worldGenerationSettings.cultistPatrolNeedsOwner;
	public double skullOmenRange = worldGenerationSettings.skullOmenRange;
	public int cultistPatrolCooldownUpperbound = worldGenerationSettings.cultistPatrolCooldownUpperbound;
	public int cultistPatrolCooldownLowerbound = worldGenerationSettings.cultistPatrolCooldownLowerbound;
	public int cultistPatrolGlobalCooldown = worldGenerationSettings.cultistPatrolGlobalCooldown;

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
		public String[] slayerTrackerEntities = new String[]{"iceandfire:firedragon 5", "iceandfire:icedragon 5", "iceandfire:hippogryph 3", "iceandfire:gorgon 10", "iceandfire:if_pixie 1", "iceandfire:cyclops 3", "iceandfire:siren 3", "iceandfire:hippocampus 1", "iceandfire:deathworm 4", "iceandfire:if_cockatrice 2", "iceandfire:stymphalianbird 2", "iceandfire:if_troll 2", "iceandfire:myrmex_worker 1", "iceandfire:myrmex_soldier 2", "iceandfire:myrmex_sentinel 5", "iceandfire:myrmex_royal 5", "iceandfire:myrmex_queen 10", "iceandfire:seaserpent 2", "iceandfire:dread_thrall 1", "iceandfire:dread_ghoul 3", "iceandfire:dread_beast 3", "iceandfire:dread_scuttler 5", "iceandfire:dread_lich 10", "iceandfire:dread_knight 5", "iceandfire:dread_horse 1", "iceandfire:if_hydra 10", "iceandfire:lightningdragon 5", "iceandfire:blackfrost 40", "iceandfire:dread_queen 50"};

		@Config.Name("Slayer Tracker Points Scaling")
		@Config.Comment("Should slayer tracker points scale with the size of the creature for sized creatures (1 - 5 for dragons, 1.5 - 9 for sea serpents, 0.25 - 2.4 for death worms, rounded up). This will reward you more for harder kills")
		public boolean shouldSlayerKillsScale = true;

		@Config.Name("Trade Reset Timer")
		@Config.Comment("The time it takes for a trade to reset after the first is disabled")
		public int tradeResetTimer = 12000;

	}

	public static class ArtefactSettings {

		@Config.Name("Heart of Dread Duration Increase")
		@Config.Comment("Duration increase of the heart of dread charm")
		public float heartOfDreadDurationMultiplier = 2f;

		@Config.Name("Dwarven Pocket Forge Valid Items")
		@Config.Comment("Map of dragon armor materials and respective dragon armor")
		@Config.RequiresWorldRestart
		public String[] dwarvenPocketForgeValidItems = new String[]{"minecraft:iron_ingot iceandfire:dragonarmor_iron", "minecraft:gold_ingot iceandfire:dragonarmor_gold", "minecraft:diamond iceandfire:dragonarmor_diamond", "iceandfire:silver_ingot iceandfire:dragonarmor_silver", "iceandfire:dragonsteel_fire_ingot iceandfire:dragonarmor_dragonsteel_fire", "iceandfire:dragonsteel_ice_ingot iceandfire:dragonarmor_dragonsteel_ice", "iceandfire:copper_ingot iceandfire:dragonarmor_copper", "iceandfire:dragonsteel_lightning_ingot iceandfire:dragonarmor_dragonsteel_lightning"};

	}

	public static class WorldGenerationSettings {

		@Config.Name("Slayer Village Spawn Chance")
		@Config.Comment("Rarity of slayer villages. 1 in this many chunks will contain a slayer village, meaning higher numbers are rarer.")
		public int slayerVillageChance = 100;

		@Config.Name("Cultist Patrol Needs Owner")
		@Config.Comment("True if the owner of a dragon skull omen needs to be present as opposed to any player in order to trigger a cultist patrol. Enable this to ensure the player who placed the skull will trigger the patrol or to prevent trolling :)")
		public boolean cultistPatrolNeedsOwner = true;

		@Config.Name("Skull Omen Range")
		@Config.Comment("The range at which skull omens will search for players in order to trigger a cultist patrol (Set to 0 to disable cultist patrols)")
		@Config.RangeDouble(min = 0)
		public double skullOmenRange = 32;

		@Config.Name("Cultist Patrol Cooldown Upperbound")
		@Config.Comment("The maximum duration until a dragon skull omen calls another cultist patrol. Ensure this is greater than the lower bound or it will default to the lower bound")
		@Config.RangeInt(min = 0)
		public int cultistPatrolCooldownUpperbound = 120000;

		@Config.Name("Cultist Patrol Cooldown Lowerbound")
		@Config.Comment("The minimum duration until a dragon skull omen calls another cultist patrol")
		@Config.RangeInt(min = 0)
		public int cultistPatrolCooldownLowerbound = 72000;

		@Config.Name("Cultist Patrol Global Cooldown")
		@Config.Comment("The cooldown for the next cultist patrol to occur in the world")
		@Config.RangeInt(min = 0)
		public int cultistPatrolGlobalCooldown = 24000;

	}

	@SuppressWarnings("unused")
	@Mod.EventBusSubscriber(modid = IFSpellPack.MODID)
	private static class EventHandler {

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(IFSpellPack.MODID)) {
				ConfigManager.sync(IFSpellPack.MODID, Config.Type.INSTANCE);
			}
		}
	}
}
