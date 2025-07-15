package com.windanesz.ifspellpack.item;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import scala.actors.threadpool.Arrays;

public class ItemBodyReinforcedChitinousPlating extends ItemChargedArtefact {

	public static final int INCREASED_ARMOR = 2;

	public ItemBodyReinforcedChitinousPlating() {
		super(EnumRarity.UNCOMMON, ItemArtefact.Type.BODY, 10000, Arrays.asList(new Item[] {IafItemRegistry.myrmex_desert_chitin, IafItemRegistry.mymrex_jungle_swarm}), 100, 25);
	}
}
