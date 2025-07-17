package com.windanesz.ifspellpack.client;

import com.github.alexthe666.iceandfire.client.model.ModelMyrmexRoyal;
import com.github.alexthe666.iceandfire.client.model.ModelMyrmexSentinel;
import com.github.alexthe666.iceandfire.client.model.ModelMyrmexSoldier;
import com.github.alexthe666.iceandfire.client.model.ModelMyrmexWorker;
import com.github.alexthe666.iceandfire.client.render.entity.*;
import com.github.alexthe666.iceandfire.entity.EntityDreadBeast;
import com.github.alexthe666.iceandfire.entity.EntityDreadHorse;
import com.windanesz.ifspellpack.CommonProxy;
import com.windanesz.ifspellpack.client.renderer.entity.layer.LayerDragonhide;
import com.windanesz.ifspellpack.client.renderer.entity.layer.LayerDragonhideNext;
import com.windanesz.ifspellpack.client.renderer.entity.layer.LayerSentinelShell;
import com.windanesz.ifspellpack.entity.living.*;
import com.windanesz.ifspellpack.entity.projectile.*;
import electroblob.wizardry.client.renderer.entity.layers.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void initialiseLayers(){
		LayerTiledOverlay.initialiseLayers(LayerDragonhide::new);
		LayerTiledOverlay.initialiseLayers(LayerDragonhideNext::new);
		LayerTiledOverlay.initialiseLayers(LayerSentinelShell::new);
	}

	/**
	 * Called from preInit() in the main mod class to initialise the renderers.
	 */
	@Override
	public void registerRenderers() {

		//living
		RenderingRegistry.registerEntityRenderingHandler(EntityDreadBeastMinion.class, RenderDreadBeast::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDreadGhoulMinion.class, RenderDreadGhoul::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDreadHorseMinion.class, RenderDreadHorse::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDreadKnightMinion.class, RenderDreadKnight::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDreadScuttlerMinion.class, RenderDreadScuttler::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDreadThrallMinion.class, RenderDreadThrall::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMyrmexSentinelMinion.class, manager -> new RenderMyrmexBase(manager, new ModelMyrmexSentinel(), 0.85F));
		RenderingRegistry.registerEntityRenderingHandler(EntityMyrmexSoldierMinion.class, manager -> new RenderMyrmexBase(manager, new ModelMyrmexSoldier(), 0.75F));
		RenderingRegistry.registerEntityRenderingHandler(EntityMyrmexSwarmerMinion.class, manager -> new RenderMyrmexBase(manager, new ModelMyrmexRoyal(), 0.25F));
		RenderingRegistry.registerEntityRenderingHandler(EntityMyrmexWorkerMinion.class, manager -> new RenderMyrmexBase(manager, new ModelMyrmexWorker(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityPixieMinion.class, RenderPixie::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityTrollMinion.class, RenderTroll::new);

		//projectile
		RenderingRegistry.registerEntityRenderingHandler(EntityDragonFireChargeIFSP.class, manager -> new RenderDragonFireCharge(manager, true));
		RenderingRegistry.registerEntityRenderingHandler(EntityDragonIceChargeIFSP.class, manager -> new RenderDragonFireCharge(manager, false));
		RenderingRegistry.registerEntityRenderingHandler(EntityDragonLightningChargeIFSP.class, RenderDragonLightningCharge::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityHydraBreathIFSP.class, RenderNothing::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPixieChargeIFSP.class, RenderNothing::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySeaSerpentBubblesIFSP.class, RenderNothing::new);
	}

	@Override
	public void openGuiPlayerSelect(List<EntityPlayer> players, Object enumWarpMode, EnumHand hand, Object fromWaystoneEntry) {
	}
}