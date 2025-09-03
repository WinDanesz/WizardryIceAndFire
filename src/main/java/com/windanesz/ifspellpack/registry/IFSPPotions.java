package com.windanesz.ifspellpack.registry;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.potion.*;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(IFSpellPack.MODID)
@Mod.EventBusSubscriber
public class IFSPPotions {

	public static final Potion ALLURE = new PotionAllure();
	public static final Potion DRAGONHIDE = new PotionDragonhide();
	public static final Potion DRAGONREND = new PotionDragonrend();
	public static final Potion MENACE = new PotionMenace();
	public static final Potion MYRMEX_BLESSING = new PotionMyrmexBlessing();
	public static final Potion SENTINEL_SHELL = new PotionSentinelShell();
	public static final Potion TIDE_GUARDIAN = new PotionTideGuardian();
	public static final Potion TROLL_SKIN = new PotionTrollSkin();

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
		registerPotion(registry, "allure", ALLURE);
		registerPotion(registry, "dragonhide", DRAGONHIDE);
		registerPotion(registry, "dragonrend", DRAGONREND);
		registerPotion(registry, "menace", MENACE);
		registerPotion(registry, "myrmex_blessing", MYRMEX_BLESSING);
		registerPotion(registry, "sentinel_shell", SENTINEL_SHELL);
		registerPotion(registry, "tide_guardian", TIDE_GUARDIAN);
		registerPotion(registry, "troll_skin", TROLL_SKIN);
	}

}
