package com.windanesz.ifspellpack.registry;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.item.ItemArtefactIFSP;
import com.windanesz.ifspellpack.item.ItemChargedArtefact;
import com.windanesz.wizardryutils.registry.ItemRegistry;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(IFSpellPack.MODID)
@Mod.EventBusSubscriber
public final class IFSPItems {

	public static final Item AMULET_DAMNED = new ItemChargedArtefact(EnumRarity.EPIC, ItemArtefact.Type.AMULET, 10000, IafItemRegistry.dread_shard, 100, 200);
	public static final Item CHARM_DOVAHKRIID = new ItemArtefactIFSP(EnumRarity.RARE, ItemArtefact.Type.CHARM);
	public static final Item CHARM_DRAGON_TRANSFORM_BLOCK= new ItemArtefactIFSP(EnumRarity.RARE, ItemArtefact.Type.CHARM);
	public static final Item CHARM_DREAD_HEART = new ItemChargedArtefact(EnumRarity.EPIC, ItemArtefact.Type.CHARM, 10000, IafItemRegistry.dread_shard, 100, 50);
	public static final Item CHARM_DWARVEN_GEARBOX = new ItemArtefactIFSP(EnumRarity.RARE, ItemArtefact.Type.CHARM);
	public static final Item CHARM_ENCHANTED_MANUSCRIPT = new ItemChargedArtefact(EnumRarity.RARE, ItemArtefact.Type.CHARM, 10000, IafItemRegistry.hydra_heart, 100, 0);
	public static final Item CHARM_FRACTURE_CATALYST = new ItemArtefactIFSP(EnumRarity.RARE, ItemArtefact.Type.CHARM);
	public static final Item CHARM_REGENERATING_HEAD = new ItemChargedArtefact(EnumRarity.EPIC, ItemArtefact.Type.CHARM, 3, IafItemRegistry.hydra_heart, 2, 1);
	public static final Item CHARM_STONEBREAKER_SIGIL = new ItemArtefactIFSP(EnumRarity.RARE, ItemArtefact.Type.CHARM);
	public static final Item HEAD_COCKATRICE_COMB = new ItemChargedArtefact(EnumRarity.RARE, ItemArtefact.Type.HEAD, 1000, IafItemRegistry.cockatrice_eye, 1000, 1);
	public static final Item HEAD_DREAD_CROWN = new ItemChargedArtefact(EnumRarity.RARE, ItemArtefact.Type.HEAD, 10000, IafItemRegistry.dread_shard, 100, 25);
	public static final Item HEAD_GORGON_MASK = new ItemChargedArtefact(EnumRarity.EPIC, ItemArtefact.Type.HEAD, 1, IafItemRegistry.gorgon_head, 1, 1);

	private IFSPItems() {
	} // No instances!

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		ItemRegistry.registerItem(registry, "amulet_damned", IFSpellPack.MODID, AMULET_DAMNED);
		ItemRegistry.registerItemArtefact(registry, "charm_dovahkriid", IFSpellPack.MODID, CHARM_DOVAHKRIID);
		ItemRegistry.registerItemArtefact(registry, "charm_dragon_transform_block", IFSpellPack.MODID, CHARM_DRAGON_TRANSFORM_BLOCK);
		ItemRegistry.registerItem(registry, "charm_dread_heart", IFSpellPack.MODID, CHARM_DREAD_HEART);
		ItemRegistry.registerItemArtefact(registry, "charm_dwarven_gearbox", IFSpellPack.MODID, CHARM_DWARVEN_GEARBOX);
		ItemRegistry.registerItem(registry, "charm_enchanted_manuscript", IFSpellPack.MODID, CHARM_ENCHANTED_MANUSCRIPT);
		ItemRegistry.registerItemArtefact(registry, "charm_fracture_catalyst", IFSpellPack.MODID, CHARM_FRACTURE_CATALYST);
		ItemRegistry.registerItem(registry, "charm_regenerating_head", IFSpellPack.MODID, CHARM_REGENERATING_HEAD);
		ItemRegistry.registerItemArtefact(registry, "charm_stonebreaker_sigil", IFSpellPack.MODID, CHARM_STONEBREAKER_SIGIL);
		ItemRegistry.registerItem(registry, "head_cockatrice_comb", IFSpellPack.MODID, HEAD_COCKATRICE_COMB);
		ItemRegistry.registerItem(registry, "head_dread_crown", IFSpellPack.MODID, HEAD_DREAD_CROWN);
		ItemRegistry.registerItem(registry, "head_gorgon_mask", IFSpellPack.MODID, HEAD_GORGON_MASK);
	}

}