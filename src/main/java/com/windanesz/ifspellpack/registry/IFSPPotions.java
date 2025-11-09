package com.windanesz.ifspellpack.registry;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.potion.*;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@GameRegistry.ObjectHolder(IFSpellPack.MODID)
@Mod.EventBusSubscriber
public class IFSPPotions {

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder() {
		return null;
	}
	public static final Potion ALLURE = placeholder();
	public static final Potion DRAGONHIDE = placeholder();
	public static final Potion DRAGONREND = placeholder();
	public static final Potion MENACE = placeholder();
	public static final Potion MYRMEX_BLESSING = placeholder();
	public static final Potion SENTINEL_SHELL = placeholder();
	public static final Potion TIDE_GUARDIAN = placeholder();
	public static final Potion TROLL_SKIN = placeholder();

	private IFSPPotions() {
	}

	public static void registerPotion(IForgeRegistry<Potion> registry, String name, Potion potion) {
		potion.setRegistryName(IFSpellPack.MODID, name);
		// For some reason, Potion#getName() doesn't prepend "potion." itself, so it has to be done here.
		potion.setPotionName("potion." + potion.getRegistryName().toString());
		registry.register(potion);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Potion> event) {
		IForgeRegistry<Potion> registry = event.getRegistry();
		registerPotion(registry, "allure", new PotionAllure());
		registerPotion(registry, "dragonhide", new PotionDragonhide());
		registerPotion(registry, "dragonrend", new PotionDragonrend());
		registerPotion(registry, "menace", new PotionMenace());
		registerPotion(registry, "myrmex_blessing", new PotionMyrmexBlessing());
		registerPotion(registry, "sentinel_shell", new PotionSentinelShell());
		registerPotion(registry, "tide_guardian", new PotionTideGuardian());
		registerPotion(registry, "troll_skin", new PotionTrollSkin());
	}

}
