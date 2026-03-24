package com.windanesz.ifspellpack.registry;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.construct.EntityDreadArmy;
import com.windanesz.ifspellpack.entity.living.*;
import com.windanesz.ifspellpack.entity.projectile.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class IFSPEntities {

	/**
	 * Incrementing index for the mod-specific entity network ID.
	 */
	private static int id = 0;

	private IFSPEntities() {
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<EntityEntry> event) {
		IForgeRegistry<EntityEntry> registry = event.getRegistry();

		//construct
		registry.register(createEntry(EntityDreadArmy.class, "dread_army", TrackingType.CONSTRUCT).build());

		//living
		registry.register(createEntry(EntityDreadBeastMinion.class, "dread_beast_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityDreadGhoulMinion.class, "dread_ghoul_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityDreadHorseMinion.class, "dread_horse_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityDreadKnightMinion.class, "dread_knight_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityDreadScuttlerMinion.class, "dread_scuttler_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityDreadThrallMinion.class, "dread_thrall_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntitySkeletalFireDragon.class, "skeletal_fire_dragon", TrackingType.LIVING).build());
		registry.register(createEntry(EntitySkeletalIceDragon.class, "skeletal_ice_dragon", TrackingType.LIVING).build());
		registry.register(createEntry(EntitySkeletalLightningDragon.class, "skeletal_lightning_dragon", TrackingType.LIVING).build());
		registry.register(createEntry(EntityMyrmexSentinelMinion.class, "myrmex_sentinel_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityMyrmexSoldierMinion.class, "myrmex_soldier_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityMyrmexSwarmerMinion.class, "myrmex_swarmer_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityMyrmexWorkerMinion.class, "myrmex_worker_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityPixieMinion.class, "pixie_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntitySlayerMerchant.class, "slayer_merchant", TrackingType.LIVING).egg(0, 0).build());
		registry.register(createEntry(EntityTrollMinion.class, "troll_minion", TrackingType.LIVING).build());

		//projectile
		registry.register(createEntry(EntityDragonFireChargeIFSP.class, "dragon_fire_charge", TrackingType.PROJECTILE).build());
		registry.register(createEntry(EntityDragonIceChargeIFSP.class, "dragon_ice_charge", TrackingType.PROJECTILE).build());
		registry.register(createEntry(EntityDragonLightningChargeIFSP.class, "dragon_lightning_charge", TrackingType.PROJECTILE).build());
		registry.register(createEntry(EntityHydraBreathIFSP.class, "hydra_breath", TrackingType.PROJECTILE).build());
		registry.register(createEntry(EntityPixieChargeIFSP.class, "pixie_charge", TrackingType.PROJECTILE).build());
		registry.register(createEntry(EntitySeaSerpentBubblesIFSP.class, "sea_serpent_bubbles", TrackingType.PROJECTILE).build());
	}

	/**
	 * Private helper method that simplifies the parts of an {@link EntityEntry} that are common to all entities.
	 * This automatically assigns a network id, and accepts a {@link TrackingType} for automatic tracker assignment.
	 *
	 * @param entityClass The entity class to use.
	 * @param name        The name of the entity. This will form the path of a {@code ResourceLocation} with domain
	 *                    {@code ebwizardry}, which in turn will be used as both the registry name and the 'command' name.
	 * @param tracking    The {@link TrackingType} to use for this entity.
	 * @param <T>         The type of entity.
	 * @return The (part-built) builder instance, allowing other builder methods to be added as necessary.
	 */
	public static <T extends Entity> EntityEntryBuilder<T> createEntry(Class<T> entityClass, String name, TrackingType tracking) {
		return createEntry(entityClass, name).tracker(tracking.range, tracking.interval, tracking.trackVelocity);
	}

	/**
	 * Private helper method that simplifies the parts of an {@link EntityEntry} that are common to all entities.
	 * This automatically assigns a network id.
	 *
	 * @param entityClass The entity class to use.
	 * @param name        The name of the entity. This will form the path of a {@code ResourceLocation} with domain
	 *                    {@code ebwizardry}, which in turn will be used as both the registry name and the 'command' name.
	 * @param <T>         The type of entity.
	 * @return The (part-built) builder instance, allowing other builder methods to be added as necessary.
	 */
	private static <T extends Entity> EntityEntryBuilder<T> createEntry(Class<T> entityClass, String name) {
		ResourceLocation registryName = new ResourceLocation(IFSpellPack.MODID, name);
		return EntityEntryBuilder.<T>create().entity(entityClass).id(registryName, id++).name(registryName.toString());
	}

	/**
	 * Private helper method that simplifies the parts of an {@link EntityEntry} that are common to all entities.
	 * This automatically assigns a network id, and accepts a {@link TrackingType} for automatic tracker assignment.
	 *
	 * @param entityClass The entity class to use.
	 * @param name        The name of the entity. This will form the path of a {@code ResourceLocation} with domain
	 *                    {@code ebwizardry}, which in turn will be used as both the registry name and the 'command' name.
	 * @param tracking    The {@link TrackingType} to use for this entity.
	 * @param <T>         The type of entity.
	 * @return The (part-built) builder instance, allowing other builder methods to be added as necessary.
	 */
	private static <T extends Entity> EntityEntryBuilder<T> createEntry(Class<T> entityClass, String name, String modid, TrackingType tracking) {
		return createEntry(entityClass, name, modid).tracker(tracking.range, tracking.interval, tracking.trackVelocity);
	}

	/**
	 * Private helper method that simplifies the parts of an {@link EntityEntry} that are common to all entities.
	 * This automatically assigns a network id.
	 *
	 * @param entityClass The entity class to use.
	 * @param name        The name of the entity. This will form the path of a {@code ResourceLocation} with domain
	 *                    {@code ebwizardry}, which in turn will be used as both the registry name and the 'command' name.
	 * @param <T>         The type of entity.
	 * @return The (part-built) builder instance, allowing other builder methods to be added as necessary.
	 */
	private static <T extends Entity> EntityEntryBuilder<T> createEntry(Class<T> entityClass, String name, String modid) {
		ResourceLocation registryName = new ResourceLocation(modid, name);
		return EntityEntryBuilder.<T>create().entity(entityClass).id(registryName, id++).name(registryName.toString());
	}

	/**
	 * Most entity trackers fall into one of a few categories, so they are defined here for convenience. This
	 * generally follows the values used in vanilla for each entity type.
	 */
	public enum TrackingType {

		LIVING(80, 3, true),
		PROJECTILE(64, 1, true),
		CONSTRUCT(160, 10, false);

		int range;
		int interval;
		boolean trackVelocity;

		TrackingType(int range, int interval, boolean trackVelocity) {
			this.range = range;
			this.interval = interval;
			this.trackVelocity = trackVelocity;
		}
	}
}
