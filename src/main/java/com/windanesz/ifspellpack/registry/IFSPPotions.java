package com.windanesz.ifspellpack.registry;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.potion.PotionDragonhide;
import com.windanesz.ifspellpack.potion.PotionDragonrend;
import com.windanesz.ifspellpack.potion.PotionTideGuardian;
import com.windanesz.ifspellpack.potion.PotionTrollSkin;
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

	public static final Potion DRAGONHIDE = new PotionDragonhide();
	public static final Potion DRAGONREND = new PotionDragonrend();
	public static final Potion TIDE_GUARDIAN = new PotionTideGuardian();
	public static final Potion TROLL_SKIN = new PotionTrollSkin();

	private IFSPPotions() {
	}

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder() {
		return null;
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
		registerPotion(registry, "dragonhide", DRAGONHIDE);
		registerPotion(registry, "dragonrend", DRAGONREND);
		registerPotion(registry, "tide_guardian", TIDE_GUARDIAN);
		registerPotion(registry, "troll_skin", TROLL_SKIN);
	}

}
