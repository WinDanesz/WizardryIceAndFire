package com.windanesz.ifspellpack.registry;

import com.windanesz.ifspellpack.IFSpellPack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;

/**
 * Class responsible for registering Spell Bundle's loot tables. Also handles loot injection.
 *
 * @author WinDanesz
 */
@Mod.EventBusSubscriber
public class IFSPLoot {


	private static LootTable RARE_SCROLLS;

	private IFSPLoot() {
	} // No instances!

	/**
	 * Called from the preInit method in the main mod class to register the custom dungeon loot.
	 */
	public static void preInit() {
		LootTableList.register(new ResourceLocation(IFSpellPack.MODID, "inject/rare_scrolls"));

	}


	/**
	 * Injects every element of sourcePool into targetPool
	 */
	private static void injectEntries(LootPool sourcePool, LootPool targetPool) {
		// Accessing {@link net.minecraft.world.storage.loot.LootPool.lootEntries}
		if (sourcePool != null && targetPool != null) {
			List<LootEntry> lootEntries = ObfuscationReflectionHelper.getPrivateValue(LootPool.class, sourcePool, "field_186453_a");

			for (LootEntry entry : lootEntries) {
				targetPool.addEntry(entry);
			}
		} else {
			IFSpellPack.logger.warn("Attempted to inject to null pool source or target.");
		}

	}

	private static LootPool getAdditive(String entryName, String poolName) {
		return new LootPool(new LootEntry[]{getAdditiveEntry(entryName, 1)}, new LootCondition[0],
				new RandomValueRange(1), new RandomValueRange(0, 1), IFSpellPack.MODID + "_" + poolName);
	}

	private static LootEntryTable getAdditiveEntry(String name, int weight) {
		return new LootEntryTable(new ResourceLocation(name), weight, 0, new LootCondition[0],
				IFSpellPack.MODID + "_additive_entry");
	}

}
