package com.windanesz.ifspellpack.registry;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.IafVillagerRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class IFSPVillagerProfessions {

/*
	@SubscribeEvent
	public static void register(RegistryEvent.Register<VillagerRegistry.VillagerProfession> event) {
		IForgeRegistry<VillagerRegistry.VillagerProfession> registry = event.getRegistry();

		IafVillagerRegistry iafVillagerRegistry = IafVillagerRegistry.INSTANCE;
		VillagerRegistry.VillagerCareer career;

		career = new VillagerRegistry.VillagerCareer(iafVillagerRegistry.desertMyrmexQueen, "desert_myrmex_queen");
		career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.dragon_flute), new ItemStack(IafItemRegistry.myrmex_desert_egg, 1, 0), new EntityVillager.PriceInfo(1, 10), new EntityVillager.PriceInfo(1, 1)));
	}
*/

}
