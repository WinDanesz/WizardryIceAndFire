package com.windanesz.ifspellpack.core;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class IFSPContainer extends DummyModContainer {
	public IFSPContainer() {
		super(new ModMetadata());
		ModMetadata meta = this.getMetadata();
		meta.modId = "ifspellpackcore";
		meta.name = "Wizardry: Ice and Fire Core";
		meta.description = "Core functionality of Wizardry: Ice and Fire";
		meta.version = "1.12.2-1.0.0";
		meta.authorList.add("WinDanesz, ipdnaeip");
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}
}