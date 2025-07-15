package com.windanesz.ifspellpack.registry;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.enchantment.EnchantmentDragonbane;
import com.windanesz.ifspellpack.enchantment.EnchantmentSilverLining;
import electroblob.wizardry.enchantment.EnchantmentTimed;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(IFSpellPack.MODID)
@Mod.EventBusSubscriber
public final class IFSPEnchantments {

	private IFSPEnchantments() {
	}

	public static final Enchantment DRAGONBANE = new EnchantmentDragonbane();
	public static final Enchantment SILVER_LINING = new EnchantmentSilverLining();

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Enchantment> event) {
		IForgeRegistry<Enchantment> registry = event.getRegistry();
		registry.register(DRAGONBANE);
		registry.register(SILVER_LINING);
	}
}
