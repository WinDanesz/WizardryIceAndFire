package com.windanesz.ifspellpack.core;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.ArrayList;
import java.util.List;

public class IFSPMixinLoader implements ILateMixinLoader {
	@Override
	public List<String> getMixinConfigs() {
		List<String> configs = new ArrayList<>();
		// CLIENT ONLY
		if (IFSPLoadingPlugin.isClient) {
			configs.add("ifspellpack.ebwizardry.client.mixins.json");
			configs.add("ifspellpack.iceandfire.client.mixins.json");
		}
		// COMMON
		configs.add("ifspellpack.ebwizardry.mixins.json");
		configs.add("ifspellpack.iceandfire.mixins.json");
		return configs;
	}

	@Override
	public boolean shouldMixinConfigQueue(String mixinConfig) {
		if (IFSPLoadingPlugin.isClient) {
			if (mixinConfig.equals("ifspellpack.ebwizardry.client.mixins.json") || mixinConfig.equals("ifspellpack.iceandfire.client.mixins.json")) {
				return true;
			}
		}
		return true;
	}
}