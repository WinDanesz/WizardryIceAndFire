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
	public static final Spell HYDRAS_HEARTBEAT = new HydrasHeartbeat();
	public static final Spell TIDE_GUARDIAN = new TideGuardian();
	public static final Spell STYMPHALIAN_STORM = new StymphalianStorm();
	public static final Spell STYMPHALIAN_BARRAGE = new StymphalianBarrage();
	public static final Spell COCKATRICES_STARE = new CockatricesStare();
	public static final Spell HYDRAS_BREATH = new HydrasBreath();
	public static final Spell TROLL_SKIN = new TrollSkin();
	public static final Spell SHACKLES = new Shackles();
	public static final Spell NO_ESCAPE = new NoEscape();
	public static final Spell DRAGONREND = new Dragonrend();
	public static final Spell SUMMON_DREAD_THRALL = new SummonDreadThrall();
	public static final Spell SUMMON_DREAD_GHOUL = new SummonDreadGhoul();
	public static final Spell SUMMON_DREAD_KNIGHT = new SummonDreadKnight();
	public static final Spell DREAD_LICH_SKULL = new DreadLichSkull();

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
		registry.register(HYDRAS_HEARTBEAT);
		registry.register(TIDE_GUARDIAN);
		registry.register(STYMPHALIAN_STORM);
		registry.register(STYMPHALIAN_BARRAGE);
		registry.register(COCKATRICES_STARE);
		registry.register(HYDRAS_BREATH);
		registry.register(TROLL_SKIN);
		registry.register(SHACKLES);
		registry.register(NO_ESCAPE);
		registry.register(DRAGONREND);
		registry.register(SUMMON_DREAD_THRALL);
		registry.register(SUMMON_DREAD_GHOUL);
		registry.register(SUMMON_DREAD_KNIGHT);
		registry.register(DREAD_LICH_SKULL);
	}
}
