package com.windanesz.ifspellpack.registry;

import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.item.IMultiTexturedItem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public final class IFSPModels {
	private IFSPModels() {
	} // no instances


	@SubscribeEvent
	public static void register(ModelRegistryEvent event) {

		// Automatic item model registry
		for (Item item : Item.REGISTRY) {
			if (item.getRegistryName().getNamespace().equals(IFSpellPack.MODID)) {
				registerItemModel(item); // Standard item model
			}
		}

	}

	// below registry methods are courtesy of EB
	public static void registerItemModel(Item item) {
		ModelBakery.registerItemVariants(item, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(item, s -> new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

	private static <T extends Item & IMultiTexturedItem> void registerMultiTexturedModel(T item) {

		if (item.getHasSubtypes()) {
			NonNullList<ItemStack> items = NonNullList.create();
			item.getSubItems(item.getCreativeTab(), items);
			for (ItemStack stack : items) {
				ModelLoader.setCustomModelResourceLocation(item, stack.getMetadata(),
						new ModelResourceLocation(item.getModelName(stack), "inventory"));
			}
		}
	}

	private static void registerItemModel(Item item, int metadata, String variant) {
		ModelLoader.setCustomModelResourceLocation(item, metadata,
				new ModelResourceLocation(item.getRegistryName(), variant));
	}

}

