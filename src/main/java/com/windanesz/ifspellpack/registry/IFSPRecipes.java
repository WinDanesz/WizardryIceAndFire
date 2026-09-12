package com.windanesz.ifspellpack.registry;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.recipe.RecipeDragonSkullOmen;
import com.windanesz.ifspellpack.recipe.RecipeDragonSkullOmenFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistryModifiable;

@GameRegistry.ObjectHolder(IFSpellPack.MODID)
@Mod.EventBusSubscriber
public class IFSPRecipes {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void register(RegistryEvent.Register<IRecipe> event) {
		IForgeRegistryModifiable<IRecipe> registry = (IForgeRegistryModifiable<IRecipe>) event.getRegistry();

		//Smelting
		GameRegistry.addSmelting(IafItemRegistry.dread_sword, new ItemStack(IFSPItems.DREAD_STEEL_NUGGET, 4), 1f);
		GameRegistry.addSmelting(IafItemRegistry.dread_knight_sword, new ItemStack(IFSPItems.DREAD_STEEL_NUGGET, 8), 1f);

	}

/*	static {
		CraftingHelper.register(new ResourceLocation(IFSpellPack.MODID, "skull_omen"), RecipeDragonSkullOmen::factory);
	}*/

}
