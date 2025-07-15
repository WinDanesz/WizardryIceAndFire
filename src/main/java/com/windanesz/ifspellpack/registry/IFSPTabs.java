package com.windanesz.ifspellpack.registry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class IFSPTabs {

	public static final CreativeTabs IFSPELLPACK = new CreativeTabs("ifspellpack") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(IFSPItems.AMULET_DAMNED);
		}
	};

}
