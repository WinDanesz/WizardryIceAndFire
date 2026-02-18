package com.windanesz.ifspellpack.item;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class ItemBodyReinforcedChitinousPlating extends ItemChargedArtefact {

	public static final int INCREASED_ARMOR = 2;

	public ItemBodyReinforcedChitinousPlating() {
		super(EnumRarity.UNCOMMON, ItemArtefact.Type.BODY, 10000, new HashSet<>(Arrays.asList(IafItemRegistry.myrmex_desert_chitin, IafItemRegistry.myrmex_jungle_chitin)), 100, 25);
	}
}
