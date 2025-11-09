package com.windanesz.ifspellpack.core;

import net.minecraftforge.fml.common.Loader;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.ArrayList;
import java.util.List;

public class IFSPMixinLoader implements ILateMixinLoader {
	@Override
	public List<String> getMixinConfigs() {
		List<String> configs = new ArrayList<>();
		// CLIENT ONLY
		// COMMON
		configs.add("ifspellpack.ebwizardry.mixins.json");
		configs.add("ifspellpack.iceandfire.mixins.json");
		return configs;
	}

	@Override
	public boolean shouldMixinConfigQueue(String mixinConfig) {
		// Only load mixins if the target mod is present
		if (mixinConfig.contains("ebwizardry")) {
			return Loader.isModLoaded("ebwizardry");
		}
		if (mixinConfig.contains("iceandfire")) {
			return Loader.isModLoaded("iceandfire");
		}
		return true;
	}
}