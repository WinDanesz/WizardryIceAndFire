package com.windanesz.ifspellpack.registry;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.spell.*;
import electroblob.wizardry.spell.Spell;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@ObjectHolder(IFSpellPack.MODID)
@EventBusSubscriber
public final class IFSPSpells {

	public static final Spell testspell = placeholder();
	public static final Spell TROLL_TROOP = new TrollTroop();
	public static final Spell HYDRAS_HEART = new HydrasHeart();
	public static final Spell TIDE_GUARDIAN = new TideGuardian();
	public static final Spell STYMPHALIAN_STORM = new StymphalianStorm();
	public static final Spell STYMPHALIAN_BARRAGE = new StymphalianBarrage();

	private IFSPSpells() {
	} // no instances

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder() {
		return null;
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Spell> event) {

		IForgeRegistry<Spell> registry = event.getRegistry();
		registry.register(new TestSpell());
		registry.register(TROLL_TROOP);
		registry.register(HYDRAS_HEART);
		registry.register(TIDE_GUARDIAN);
		registry.register(STYMPHALIAN_STORM);
		registry.register(STYMPHALIAN_BARRAGE);
	}
}
