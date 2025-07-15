package com.windanesz.ifspellpack.client.renderer.entity.layer;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.registry.IFSPPotions;
import electroblob.wizardry.client.renderer.entity.layers.LayerTiledOverlay;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;


public class LayerSentinelShell extends LayerTiledOverlay<EntityLivingBase> {

	public LayerSentinelShell(RenderLivingBase<?> renderer){
		super(renderer);
	}

	@Override
	public boolean shouldRender(EntityLivingBase entity, float partialTicks){
		return !entity.isInvisible() && entity.isPotionActive(IFSPPotions.SENTINEL_SHELL);
	}

	@Override
	public ResourceLocation getTexture(EntityLivingBase entity, float partialTicks){
		if (entity.world.getLightFromNeighbors(entity.getPosition()) > 8) {
			return new ResourceLocation("textures/blocks/sand.png");
		} else {
			return new ResourceLocation(IFSpellPack.MODID, "textures/overlay/jungle_leaves.png");
		}
	}

	@Override
	public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale){
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		int j = entity.getBrightnessForRender();
		int k = j % 65536;
		int l = j / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
		super.doRenderLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		GlStateManager.disableBlend();
	}

}
