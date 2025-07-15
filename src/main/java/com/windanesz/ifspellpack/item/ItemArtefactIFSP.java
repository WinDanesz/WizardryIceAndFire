package com.windanesz.ifspellpack.item;

import com.windanesz.ifspellpack.registry.IFSPTabs;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.item.EnumRarity;

public class ItemArtefactIFSP extends ItemArtefact {

	public ItemArtefactIFSP(EnumRarity rarity, Type type) {
		super(rarity, type);
		this.setCreativeTab(IFSPTabs.IFSPELLPACK);
	}

}
