package com.windanesz.ifspellpack.registry;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.school.School;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber
public class IFSPSchools {

	private IFSPSchools() {}

	@SubscribeEvent
	public static void createRegistry(RegistryEvent.NewRegistry event){
		RegistryBuilder<School> builder = new RegistryBuilder<>();
		builder.setType(School.class);
		builder.setName(new ResourceLocation(IFSpellPack.MODID, "schools"));
		builder.setIDRange(0, 5000);
		School.registry = builder.create();
	}

	public static final School BEAST = new School("beast");
	public static final School DRACONIC = new School("draconic");
	public static final School DREAD = new School("dread");
	public static final School FAE = new School("fae");
	public static final School SLAYER = new School("slayer");

	@SubscribeEvent
	public static void register(RegistryEvent.Register<School> event) {
		IForgeRegistry<School> registry = event.getRegistry();
		registry.register(DRACONIC);
		registry.register(DREAD);
		registry.register(FAE);
		registry.register(SLAYER);
		System.out.println("successfully registered schools");
	}

}
