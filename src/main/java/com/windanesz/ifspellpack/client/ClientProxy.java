package com.windanesz.ifspellpack.client;

import com.github.alexthe666.iceandfire.client.model.*;
import com.github.alexthe666.iceandfire.client.model.animator.FireDragonTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.animator.IceDragonTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.animator.LightningDragonTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.*;
import com.github.alexthe666.iceandfire.client.render.entity.*;
import com.github.alexthe666.iceandfire.entity.*;
import com.windanesz.ifspellpack.CommonProxy;
import com.windanesz.ifspellpack.client.particle.ParticleAllureAppearance;
import com.windanesz.ifspellpack.client.renderer.entity.layer.LayerDragonhide;
import com.windanesz.ifspellpack.client.renderer.entity.layer.LayerDragonhideNext;
import com.windanesz.ifspellpack.client.renderer.entity.layer.LayerSentinelShell;
import com.windanesz.ifspellpack.client.renderer.entity.living.RenderSkeletalDragon;
import com.windanesz.ifspellpack.entity.construct.EntityDreadArmy;
import com.windanesz.ifspellpack.entity.living.*;
import com.windanesz.ifspellpack.entity.projectile.*;
import com.windanesz.ifspellpack.registry.IFSPParticles;
import electroblob.wizardry.client.particle.ParticleWizardry;
import electroblob.wizardry.client.renderer.entity.layers.*;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;


@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	public IceAndFireTabulaModel<EntityFireDragon> FIRE_DRAGON_MODEL;
	public IceAndFireTabulaModel<EntityIceDragon> ICE_DRAGON_MODEL;
	public IceAndFireTabulaModel<EntityLightningDragon> LIGHTNING_DRAGON_MODEL;

	@Override
	public void initialiseLayers(){
		LayerTiledOverlay.initialiseLayers(LayerDragonhide::new);
		LayerTiledOverlay.initialiseLayers(LayerDragonhideNext::new);
		LayerTiledOverlay.initialiseLayers(LayerSentinelShell::new);
	}

	@Override
	public void registerParticles() {
		ParticleWizardry.registerParticle(IFSPParticles.ALLURE_APPEARANCE, ParticleAllureAppearance::new);
	}

	/**
	 * Called from preInit() in the main mod class to initialise the renderers.
	 */
	@Override
	public void registerRenderers() {
		//These calls are needed in order for the dragon model and render to load properly
		EnumDragonAnimations.initializeDragonModels();
		DragonAnimationsLibrary.register(EnumDragonPoses.values(), EnumDragonModelTypes.values());
		try {
			this.FIRE_DRAGON_MODEL = new IceAndFireTabulaModel<>(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/firedragon/firedragon_Ground"), new FireDragonTabulaModelAnimator());
			this.ICE_DRAGON_MODEL = new IceAndFireTabulaModel<>(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/icedragon/icedragon_Ground"), new IceDragonTabulaModelAnimator());
			this.LIGHTNING_DRAGON_MODEL = new IceAndFireTabulaModel<>(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/lightningdragon/lightningdragon_Ground"), new LightningDragonTabulaModelAnimator());
		} catch (IOException e) {
			e.printStackTrace();
		}

		//construct
		RenderingRegistry.registerEntityRenderingHandler(EntityDreadArmy.class, RenderNothing::new);

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
		RenderingRegistry.registerEntityRenderingHandler(EntitySkeletalFireDragon.class, manager -> new RenderSkeletalDragon(manager, FIRE_DRAGON_MODEL, 0));
		RenderingRegistry.registerEntityRenderingHandler(EntitySkeletalIceDragon.class, manager -> new RenderSkeletalDragon(manager, ICE_DRAGON_MODEL, 1));
		RenderingRegistry.registerEntityRenderingHandler(EntitySkeletalLightningDragon.class, manager -> new RenderSkeletalDragon(manager, LIGHTNING_DRAGON_MODEL, 2));
		RenderingRegistry.registerEntityRenderingHandler(EntityTrollMinion.class, RenderTroll::new);

		//projectile
		RenderingRegistry.registerEntityRenderingHandler(EntityDragonFireChargeIFSP.class, manager -> new RenderDragonFireCharge(manager, true));
		RenderingRegistry.registerEntityRenderingHandler(EntityDragonIceChargeIFSP.class, manager -> new RenderDragonFireCharge(manager, false));
		RenderingRegistry.registerEntityRenderingHandler(EntityDragonLightningChargeIFSP.class, RenderDragonLightningCharge::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityHydraBreathIFSP.class, RenderNothing::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPixieChargeIFSP.class, RenderNothing::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySeaSerpentBubblesIFSP.class, RenderNothing::new);
	}

}