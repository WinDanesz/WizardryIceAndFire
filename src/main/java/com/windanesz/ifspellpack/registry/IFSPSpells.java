package com.windanesz.ifspellpack.registry;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.spell.*;
import electroblob.wizardry.spell.Spell;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(IFSpellPack.MODID)
@EventBusSubscriber
public final class IFSPSpells {

	public static final Spell TROLL_TROOP = new TrollTroop();
	public static final Spell HYDRA_PULSE = new HydraPulse();
	public static final Spell TIDE_GUARDIAN = new TideGuardian();
	public static final Spell STYMPHALIAN_STORM = new StymphalianStorm();
	public static final Spell STYMPHALIAN_BARRAGE = new StymphalianBarrage();
	public static final Spell COCKATRICES_STARE = new CockatriceStare();
	public static final Spell HYDRA_BREATH = new HydraBreath();
	public static final Spell TROLL_SKIN = new TrollSkin();
	public static final Spell SHACKLES = new Shackles();
	public static final Spell NO_ESCAPE = new NoEscape();
	public static final Spell DRAGONREND = new Dragonrend();
	public static final Spell SUMMON_DREAD_THRALL = new SummonDreadThrall();
	public static final Spell SUMMON_DREAD_GHOUL = new SummonDreadGhoul();
	public static final Spell SUMMON_DREAD_KNIGHT = new SummonDreadKnight();
	public static final Spell DREAD_LICH_SKULL = new DreadLichSkull();
	public static final Spell DRAGON_ROAR = new DragonRoar();
	public static final Spell DRAGONBANE = new Dragonbane();
	public static final Spell SUMMON_DREAD_HORSE = new SummonDreadHorse();
	public static final Spell SILVER_WIND = new SilverWind();
	public static final Spell DRAGONHIDE = new Dragonhide();
	public static final Spell DRAGON_FIRE_BREATH = new DragonFireBreath();
	public static final Spell DRAGON_ICE_BREATH = new DragonIceBreath();
	public static final Spell DRAGON_LIGHTNING_BREATH = new DragonLightningBreath();
	public static final Spell DRAGON_FIRE_CHARGE = new DragonFireCharge();
	public static final Spell DRAGON_ICE_CHARGE = new DragonIceCharge();
	public static final Spell DRAGON_LIGHTNING_CHARGE = new DragonLightningCharge();
	public static final Spell GORGON_GAZE = new GorgonGaze();
	public static final Spell EXPLODE_STATUE = new ExplodeStatue();
	public static final Spell CONSUME_STATUE = new ConsumeStatue();
	public static final Spell SUMMON_PIXIE_CLUSTER = new SummonPixieCluster();
	public static final Spell PIXIE_CHARGE = new PixieCharge();
	public static final Spell SILVER_LINING = new SilverLining();
	public static final Spell SUMMON_MYRMEX_SOLDIER = new SummonMyrmexSoldier();
	public static final Spell SUMMON_MYRMEX_WORKER = new SummonMyrmexWorker();
	public static final Spell SUMMON_MYRMEX_SENTINEL = new SummonMyrmexSentinel();
	public static final Spell SUMMON_MYRMEX_SWARM = new SummonMyrmexSwarm();
	public static final Spell MYRMEX_BLESSING = new MyrmexBlessing();
	public static final Spell SENTINEL_SHELL = new SentinelShell();
	public static final Spell SEA_SERPENT_BUBBLES = new SeaSerpentBubbles();
	public static final Spell SEA_SERPENT_FURY = new SeaSerpentFury();
	public static final Spell LIVE_WIRE = new LiveWire();
	public static final Spell CALL_BEAST = new CallBeast();

	private IFSPSpells() {
	} // no instances

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Spell> event) {

		IForgeRegistry<Spell> registry = event.getRegistry();
		registry.register(TROLL_TROOP);
		registry.register(HYDRA_PULSE);
		registry.register(TIDE_GUARDIAN);
		registry.register(STYMPHALIAN_STORM);
		registry.register(STYMPHALIAN_BARRAGE);
		registry.register(COCKATRICES_STARE);
		registry.register(HYDRA_BREATH);
		registry.register(TROLL_SKIN);
		registry.register(SHACKLES);
		registry.register(NO_ESCAPE);
		registry.register(DRAGONREND);
		registry.register(SUMMON_DREAD_THRALL);
		registry.register(SUMMON_DREAD_GHOUL);
		registry.register(SUMMON_DREAD_KNIGHT);
		registry.register(DREAD_LICH_SKULL);
		registry.register(DRAGON_ROAR);
		registry.register(DRAGONBANE);
		registry.register(SUMMON_DREAD_HORSE);
		registry.register(SILVER_WIND);
		registry.register(DRAGONHIDE);
		registry.register(DRAGON_FIRE_BREATH);
		registry.register(DRAGON_ICE_BREATH);
		registry.register(DRAGON_LIGHTNING_BREATH);
		registry.register(DRAGON_FIRE_CHARGE);
		registry.register(DRAGON_ICE_CHARGE);
		registry.register(DRAGON_LIGHTNING_CHARGE);
		registry.register(GORGON_GAZE);
		registry.register(EXPLODE_STATUE);
		registry.register(CONSUME_STATUE);
		registry.register(SUMMON_PIXIE_CLUSTER);
		registry.register(PIXIE_CHARGE);
		registry.register(SILVER_LINING);
		registry.register(SUMMON_MYRMEX_SOLDIER);
		registry.register(SUMMON_MYRMEX_WORKER);
		registry.register(SUMMON_MYRMEX_SENTINEL);
		registry.register(SUMMON_MYRMEX_SWARM);
		registry.register(MYRMEX_BLESSING);
		registry.register(SENTINEL_SHELL);
		registry.register(SEA_SERPENT_BUBBLES);
		registry.register(SEA_SERPENT_FURY);
		registry.register(LIVE_WIRE);
		registry.register(CALL_BEAST);
	}
}
