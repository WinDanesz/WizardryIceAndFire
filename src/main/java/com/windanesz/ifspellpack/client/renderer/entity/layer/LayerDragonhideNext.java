package com.windanesz.ifspellpack.client.renderer.entity.layer;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.windanesz.ifspellpack.registry.IFSPPotions;
import electroblob.wizardry.client.renderer.entity.layers.LayerTiledOverlay;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;


public class LayerDragonhideNext extends LayerTiledOverlay<EntityLivingBase> {

	public static final ResourceLocation[] TEXTURES = createTextures("bronze", "copper");
	//public static final ResourceLocation[] TEXTURES = createTextures("amythest", "black", "blue", "bronze", "copper", "electric", "gray", "green", "red", "sapphire", "silver", "white");

	public LayerDragonhideNext(RenderLivingBase<?> renderer){
		super(renderer);
	}

	@Override
	public boolean shouldRender(EntityLivingBase entity, float partialTicks){
		return !entity.isInvisible() && entity.isPotionActive(IFSPPotions.DRAGONHIDE);
	}

	//Chooses the next incremental texture from the array
	@Override
	public ResourceLocation getTexture(EntityLivingBase entity, float partialTicks){
		return TEXTURES[((entity.ticksExisted + LayerDragonhide.durationBetweenChanges) / LayerDragonhide.durationBetweenChanges) % ((TEXTURES.length - 1))];
	}

	@Override
	public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale){
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1, 1, 1, computeBlendFactor(entity, partialTicks));
		int j = entity.getBrightnessForRender();
		int k = j % 65536;
		int l = j / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
		super.doRenderLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		GlStateManager.disableBlend();
	}

	private float computeBlendFactor(EntityLivingBase entity, float partialTicks) {
		int ticks = entity.ticksExisted % LayerDragonhide.durationBetweenChanges;
		return Math.min(1.0f, partialTicks + (float)ticks / LayerDragonhide.durationBetweenChanges);
	}

	private static ResourceLocation[] createTextures(String... suffixes) {
		ResourceLocation[] textures = new ResourceLocation[suffixes.length];
		for (int i = 0; i < suffixes.length; i++) {
			textures[i] = new ResourceLocation(IceAndFire.MODID, "textures/blocks/dragonscale_" + suffixes[i] + ".png");
		}
		return textures;
	}

}
