package com.windanesz.ifspellpack.registry;

import com.windanesz.ifspellpack.IFSpellPack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

@GameRegistry.ObjectHolder(IFSpellPack.MODID)
@Mod.EventBusSubscriber
public class IFSPRecipes {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void register(RegistryEvent.Register<IRecipe> event) {
		IForgeRegistryModifiable<IRecipe> registry = (IForgeRegistryModifiable<IRecipe>) event.getRegistry();

//		if (PointerIntegration.getInstance().isEnabled() && Settings.generalSettings.remove_pointer_item) {
//			if (registry.getValue(new ResourceLocation("pointer:pointer")) != null) {
//				registry.remove(new ResourceLocation("pointer:pointer"));
//			}
//		}
//
//		if (PointerIntegration.getInstance().isEnabled() && Settings.generalSettings.remove_portalgun_recipes) {
//			if (registry.getValue(new ResourceLocation("portalgun:portalgun")) != null) {
//				registry.remove(new ResourceLocation("portalgun:portalgun"));
//			}
//			if (registry.getValue(new ResourceLocation("portalgun:miniature_black_hole")) != null) {
//				registry.remove(new ResourceLocation("portalgun:miniature_black_hole"));
//			}
//		}
	}
}
