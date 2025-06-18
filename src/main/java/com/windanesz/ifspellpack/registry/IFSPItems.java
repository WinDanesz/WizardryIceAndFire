package com.windanesz.ifspellpack.registry;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.item.ItemArtefactIFSP;
import com.windanesz.wizardryutils.registry.ItemRegistry;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@ObjectHolder(IFSpellPack.MODID)
@Mod.EventBusSubscriber
public final class IFSPItems {

	public static final Item CHARM_STONEBREAKER_SIGIL = new ItemArtefactIFSP(EnumRarity.RARE, ItemArtefact.Type.CHARM);
	public static final Item HEAD_COCKATRICES_COMB = new ItemArtefactIFSP(EnumRarity.RARE, ItemArtefact.Type.HEAD);

	private IFSPItems() {
	} // No instances!

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		ItemRegistry.registerItemArtefact(registry, "charm_stonebreaker_sigil", IFSpellPack.MODID, CHARM_STONEBREAKER_SIGIL);
		ItemRegistry.registerItemArtefact(registry, "head_cockatrices_comb", IFSpellPack.MODID, HEAD_COCKATRICES_COMB);
	}

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder() {
		return null;
	}
}