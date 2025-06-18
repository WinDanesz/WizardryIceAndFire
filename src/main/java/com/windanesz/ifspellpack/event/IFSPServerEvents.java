package com.windanesz.ifspellpack.event;

import com.github.alexthe666.iceandfire.entity.StoneEntityProperties;
import electroblob.wizardry.entity.living.ISummonedCreature;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class IFSPServerEvents {

	@SubscribeEvent
	public static void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.world;
		StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entity, StoneEntityProperties.class);
		if (properties != null && properties.isStone && entity instanceof ISummonedCreature) {
			entity.playSound(SoundEvents.BLOCK_STONE_BREAK, 1, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 0.5F);
			//Cant do all 3 because it causes a crash. Possibly because the world refers to the entity being removed?
			//entity.setDead();
			world.removeEntity(entity);
			//world.removeEntityDangerously(entity);
		}
	}

}
