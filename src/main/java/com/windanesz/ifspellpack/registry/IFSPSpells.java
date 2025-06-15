package com.windanesz.ifspellpack.registry;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.spell.TestSpell;
import com.windanesz.ifspellpack.spell.TrollTroop;
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

	}
}
