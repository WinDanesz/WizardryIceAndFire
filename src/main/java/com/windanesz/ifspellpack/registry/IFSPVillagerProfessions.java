package com.windanesz.ifspellpack.registry;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.IafVillagerRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import electroblob.wizardry.registry.WizardryItems;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

@Mod.EventBusSubscriber
public class IFSPVillagerProfessions {

	public static void init() {
		IafVillagerRegistry iafVillagerRegistry = IafVillagerRegistry.INSTANCE;

		//Desert Myrmex Queen
		desertQueenInjections(iafVillagerRegistry.desertMyrmexQueen.getCareer(0));
		//Desert Myrmex Sentinel
		desertSentinelInjections(iafVillagerRegistry.desertMyrmexSentinel.getCareer(0));
		//Jungle Myrmex Queen
		jungleQueenInjections(iafVillagerRegistry.jungleMyrmexQueen.getCareer(0));
		//Jungle Myrmex Sentinel
		jungleSentinelInjections(iafVillagerRegistry.jungleMyrmexSentinel.getCareer(0));
	}

	public static void desertQueenInjections(VillagerRegistry.VillagerCareer career) {
		career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.MYRMEX_BLESSING.metadata()), new EntityVillager.PriceInfo(10, 20), new EntityVillager.PriceInfo(1, 1)));
		career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.SUMMON_MYRMEX_WORKER.metadata()), new EntityVillager.PriceInfo(10, 20), new EntityVillager.PriceInfo(1, 1)));
		career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.SUMMON_MYRMEX_SOLDIER.metadata()), new EntityVillager.PriceInfo(20, 30), new EntityVillager.PriceInfo(1, 1)));
		career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.SUMMON_MYRMEX_SENTINEL.metadata()), new EntityVillager.PriceInfo(30, 40), new EntityVillager.PriceInfo(1, 1)));
		career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.SUMMON_MYRMEX_SWARM.metadata()), new EntityVillager.PriceInfo(40, 50), new EntityVillager.PriceInfo(1, 1)));
	}

	public static void desertSentinelInjections(VillagerRegistry.VillagerCareer career) {
		career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.SENTINEL_SHELL.metadata()), new EntityVillager.PriceInfo(10, 20), new EntityVillager.PriceInfo(1, 1)));
	}

	public static void jungleQueenInjections(VillagerRegistry.VillagerCareer career) {
		career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.MYRMEX_BLESSING.metadata()), new EntityVillager.PriceInfo(10, 20), new EntityVillager.PriceInfo(1, 1)));
		career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.SUMMON_MYRMEX_WORKER.metadata()), new EntityVillager.PriceInfo(10, 20), new EntityVillager.PriceInfo(1, 1)));
		career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.SUMMON_MYRMEX_SOLDIER.metadata()), new EntityVillager.PriceInfo(20, 30), new EntityVillager.PriceInfo(1, 1)));
		career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.SUMMON_MYRMEX_SENTINEL.metadata()), new EntityVillager.PriceInfo(30, 40), new EntityVillager.PriceInfo(1, 1)));
		career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.SUMMON_MYRMEX_SWARM.metadata()), new EntityVillager.PriceInfo(40, 50), new EntityVillager.PriceInfo(1, 1)));
	}

	public static void jungleSentinelInjections(VillagerRegistry.VillagerCareer career) {
		career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(WizardryItems.spell_book, 1, IFSPSpells.SENTINEL_SHELL.metadata()), new EntityVillager.PriceInfo(10, 20), new EntityVillager.PriceInfo(1, 1)));
	}


}
