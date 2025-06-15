package com.windanesz.ifspellpack.client;

import com.github.alexthe666.iceandfire.client.render.entity.RenderTroll;
import com.windanesz.ifspellpack.CommonProxy;
import com.windanesz.ifspellpack.entity.living.EntityTrollMinion;
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
		RenderingRegistry.registerEntityRenderingHandler(EntityTrollMinion.class, RenderTroll::new);
	}

	@Override
	public void openGuiPlayerSelect(List<EntityPlayer> players, Object enumWarpMode, EnumHand hand, Object fromWaystoneEntry) {
	}
}