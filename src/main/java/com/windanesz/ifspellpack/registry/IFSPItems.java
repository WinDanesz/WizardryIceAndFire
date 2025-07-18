package com.windanesz.ifspellpack.registry;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.item.*;
import com.windanesz.wizardryutils.registry.ItemRegistry;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;
import scala.actors.threadpool.Arrays;


@SuppressWarnings("unchecked")
@ObjectHolder(IFSpellPack.MODID)
@Mod.EventBusSubscriber
public final class IFSPItems {

	//Artefacts

	public static final Item AMULET_DAMNED = new ItemChargedArtefact(EnumRarity.EPIC, ItemArtefact.Type.AMULET, 10000, Arrays.asList(new Item[] {IafItemRegistry.dread_shard}), 100, 200);
	public static final Item AMULET_DRAGON_SLAYER = new ItemArtefactIFSP(EnumRarity.RARE, ItemArtefact.Type.AMULET);
	public static final Item AMULET_LIGHTWARD = new ItemArtefactIFSP(EnumRarity.UNCOMMON, ItemArtefact.Type.AMULET);
	public static final Item BODY_FIRE_DRAGON_CORE = new ItemChargedArtefact(EnumRarity.EPIC, ItemArtefact.Type.BODY, 10000, Arrays.asList(new Item[] {IafItemRegistry.fire_dragon_heart}), 10000, 0);
	public static final Item BODY_ICE_DRAGON_CORE = new ItemChargedArtefact(EnumRarity.EPIC, ItemArtefact.Type.BODY, 10000, Arrays.asList(new Item[] {IafItemRegistry.ice_dragon_heart}), 10000, 0);
	public static final Item BODY_LIGHTNING_DRAGON_CORE = new ItemChargedArtefact(EnumRarity.EPIC, ItemArtefact.Type.BODY, 10000, Arrays.asList(new Item[] {IafItemRegistry.lightning_dragon_heart}), 10000, 0);
	public static final Item BODY_PIXIE_WING_GLIDER = new ItemChargedArtefact(EnumRarity.EPIC, ItemArtefact.Type.BODY, 25000, Arrays.asList(new Item[] {IafItemRegistry.pixie_wings}), 12500, 1);
	public static final Item BODY_REINFORCED_CHITINOUS_PLATING = new ItemBodyReinforcedChitinousPlating();
	public static final Item CHARM_DOVAHKRIID = new ItemArtefactIFSP(EnumRarity.RARE, ItemArtefact.Type.CHARM);
	public static final Item CHARM_DRAGON_TRANSFORM_BLOCK= new ItemArtefactIFSP(EnumRarity.RARE, ItemArtefact.Type.CHARM);
	public static final Item CHARM_DREAD_HEART = new ItemChargedArtefact(EnumRarity.EPIC, ItemArtefact.Type.CHARM, 10000, Arrays.asList(new Item[] {IafItemRegistry.dread_shard}), 100, 50);
	public static final Item CHARM_DWARVEN_GEARBOX = new ItemArtefactIFSP(EnumRarity.RARE, ItemArtefact.Type.CHARM);
	public static final Item CHARM_ENCHANTED_MANUSCRIPT = new ItemChargedArtefact(EnumRarity.RARE, ItemArtefact.Type.CHARM, 10000, Arrays.asList(new Item[] {IafItemRegistry.manuscript}), 100, 0);
	public static final Item CHARM_FRACTURE_CATALYST = new ItemArtefactIFSP(EnumRarity.RARE, ItemArtefact.Type.CHARM);
	public static final Item CHARM_REGENERATING_HEAD = new ItemChargedArtefact(EnumRarity.EPIC, ItemArtefact.Type.CHARM, 3, Arrays.asList(new Item[] {IafItemRegistry.hydra_heart}), 2, 1);
	public static final Item CHARM_SOUR_CANDY = new ItemArtefactIFSP(EnumRarity.UNCOMMON, ItemArtefact.Type.CHARM);
	public static final Item CHARM_STONEBREAKER_SIGIL = new ItemArtefactIFSP(EnumRarity.RARE, ItemArtefact.Type.CHARM);
	public static final Item CHARM_VOLATILE_RESIN = new ItemCharmVolatileResin();
	public static final Item CHARM_WILDCALLER_WHISTLE = new ItemArtefactIFSP(EnumRarity.UNCOMMON, ItemArtefact.Type.CHARM);
	public static final Item HEAD_COCKATRICE_COMB = new ItemChargedArtefact(EnumRarity.RARE, ItemArtefact.Type.HEAD, 1000, Arrays.asList(new Item[] {IafItemRegistry.cockatrice_eye}), 1000, 1);
	public static final Item HEAD_DREAD_CROWN = new ItemChargedArtefact(EnumRarity.RARE, ItemArtefact.Type.HEAD, 10000, Arrays.asList(new Item[] {IafItemRegistry.dread_shard}), 100, 25);
	public static final Item HEAD_GORGON_MASK = new ItemChargedArtefact(EnumRarity.EPIC, ItemArtefact.Type.HEAD, 1, Arrays.asList(new Item[] {IafItemRegistry.gorgon_head}), 1, 1);
	public static final Item HEAD_TRIADIC_SERPENT_CROWN = new ItemArtefactIFSP(EnumRarity.UNCOMMON, ItemArtefact.Type.HEAD);
	public static final Item RING_STINGER = new ItemChargedArtefact(EnumRarity.RARE, ItemArtefact.Type.RING, 10000, Arrays.asList(new Item[] {IafItemRegistry.myrmex_stinger}), 2000, 10);

	//Magic Gear

	//Misc Items

	public static final Item DREAD_STEEL_CLOTH = new ItemIFSP();
	public static final Item DREAD_STEEL_INGOT = new ItemIFSP();
	public static final Item DREAD_STEEL_NUGGET = new ItemIFSP();

	private IFSPItems() {
	} // No instances!

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		//Artefacts

		ItemRegistry.registerItem(registry, "amulet_damned", IFSpellPack.MODID, AMULET_DAMNED);
		ItemRegistry.registerItemArtefact(registry, "amulet_dragon_slayer", IFSpellPack.MODID, AMULET_DRAGON_SLAYER);
		ItemRegistry.registerItemArtefact(registry, "amulet_lightward", IFSpellPack.MODID, AMULET_LIGHTWARD);
		ItemRegistry.registerItem(registry, "body_fire_dragon_core", IFSpellPack.MODID, BODY_FIRE_DRAGON_CORE);
		ItemRegistry.registerItem(registry, "body_ice_dragon_core", IFSpellPack.MODID, BODY_ICE_DRAGON_CORE);
		ItemRegistry.registerItem(registry, "body_lightning_dragon_core", IFSpellPack.MODID, BODY_LIGHTNING_DRAGON_CORE);
		ItemRegistry.registerItem(registry, "body_pixie_wing_glider", IFSpellPack.MODID, BODY_PIXIE_WING_GLIDER);
		ItemRegistry.registerItem(registry, "body_reinforced_chitinous_plating", IFSpellPack.MODID, BODY_REINFORCED_CHITINOUS_PLATING);
		ItemRegistry.registerItemArtefact(registry, "charm_dovahkriid", IFSpellPack.MODID, CHARM_DOVAHKRIID);
		ItemRegistry.registerItemArtefact(registry, "charm_dragon_transform_block", IFSpellPack.MODID, CHARM_DRAGON_TRANSFORM_BLOCK);
		ItemRegistry.registerItem(registry, "charm_dread_heart", IFSpellPack.MODID, CHARM_DREAD_HEART);
		ItemRegistry.registerItemArtefact(registry, "charm_dwarven_gearbox", IFSpellPack.MODID, CHARM_DWARVEN_GEARBOX);
		ItemRegistry.registerItem(registry, "charm_enchanted_manuscript", IFSpellPack.MODID, CHARM_ENCHANTED_MANUSCRIPT);
		ItemRegistry.registerItemArtefact(registry, "charm_fracture_catalyst", IFSpellPack.MODID, CHARM_FRACTURE_CATALYST);
		ItemRegistry.registerItem(registry, "charm_regenerating_head", IFSpellPack.MODID, CHARM_REGENERATING_HEAD);
		ItemRegistry.registerItemArtefact(registry, "charm_sour_candy", IFSpellPack.MODID, CHARM_SOUR_CANDY);
		ItemRegistry.registerItemArtefact(registry, "charm_stonebreaker_sigil", IFSpellPack.MODID, CHARM_STONEBREAKER_SIGIL);
		ItemRegistry.registerItem(registry, "charm_volatile_resin", IFSpellPack.MODID, CHARM_VOLATILE_RESIN);
		ItemRegistry.registerItemArtefact(registry, "charm_wildcaller_whistle", IFSpellPack.MODID, CHARM_WILDCALLER_WHISTLE);
		ItemRegistry.registerItem(registry, "head_cockatrice_comb", IFSpellPack.MODID, HEAD_COCKATRICE_COMB);
		ItemRegistry.registerItem(registry, "head_dread_crown", IFSpellPack.MODID, HEAD_DREAD_CROWN);
		ItemRegistry.registerItem(registry, "head_gorgon_mask", IFSpellPack.MODID, HEAD_GORGON_MASK);
		ItemRegistry.registerItemArtefact(registry, "head_triadic_serpent_crown", IFSpellPack.MODID, HEAD_TRIADIC_SERPENT_CROWN);
		ItemRegistry.registerItem(registry, "ring_stinger", IFSpellPack.MODID, RING_STINGER);

		//Misc Items

		ItemRegistry.registerItem(registry, "dread_steel_cloth", IFSpellPack.MODID, DREAD_STEEL_CLOTH);
		ItemRegistry.registerItem(registry, "dread_steel_ingot", IFSpellPack.MODID, DREAD_STEEL_INGOT);
		ItemRegistry.registerItem(registry,"dread_steel_nugget", IFSpellPack.MODID, DREAD_STEEL_NUGGET);
	}

}