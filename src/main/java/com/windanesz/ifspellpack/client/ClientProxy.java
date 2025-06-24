package com.windanesz.ifspellpack.client;

import com.github.alexthe666.iceandfire.client.render.entity.*;
import com.github.alexthe666.iceandfire.entity.EntityDreadBeast;
import com.github.alexthe666.iceandfire.entity.EntityDreadHorse;
import com.windanesz.ifspellpack.CommonProxy;
import com.windanesz.ifspellpack.entity.living.*;
import com.windanesz.ifspellpack.entity.projectile.EntityHydraBreathIFSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	/**
	 * Called from preInit() in the main mod class to initialise the renderers.
	 */
	public void registerRenderers() {

		//living
		RenderingRegistry.registerEntityRenderingHandler(EntityDreadBeastMinion.class, RenderDreadBeast::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDreadGhoulMinion.class, RenderDreadGhoul::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDreadHorseMinion.class, RenderDreadHorse::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDreadKnightMinion.class, RenderDreadKnight::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDreadScuttlerMinion.class, RenderDreadScuttler::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDreadThrallMinion.class, RenderDreadThrall::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityTrollMinion.class, RenderTroll::new);

		//projectile
		RenderingRegistry.registerEntityRenderingHandler(EntityHydraBreathIFSP.class, RenderNothing::new);
	}

	@Override
	public void openGuiPlayerSelect(List<EntityPlayer> players, Object enumWarpMode, EnumHand hand, Object fromWaystoneEntry) {
	}
}