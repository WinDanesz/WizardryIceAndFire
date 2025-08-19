package com.windanesz.ifspellpack.client.renderer.entity.layer;

import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.StoneEntityProperties;
import com.windanesz.ifspellpack.IFSpellPack;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//Taken from LayerGenericGlowing
@SideOnly(Side.CLIENT)
public class LayerSkeletalDragonEyes implements LayerRenderer<EntityDragonBase> {
	
	private final RenderLiving<EntityDragonBase> render;

	public LayerSkeletalDragonEyes(RenderLiving<EntityDragonBase> render) {
		this.render = render;
	}

	public void doRenderLayer(EntityDragonBase dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(dragon, StoneEntityProperties.class);
		if (properties == null || !properties.isStone) {
			this.render.bindTexture(this.getTexture(dragon));
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
			GlStateManager.disableLighting();
			GlStateManager.depthMask(!dragon.isInvisible());
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 0.0F);
			GlStateManager.enableLighting();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.render.getMainModel().render(dragon, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			this.render.setLightmap(dragon);
			GlStateManager.depthMask(true);
			GlStateManager.disableBlend();
		}
	}

	public ResourceLocation getTexture(EntityDragonBase dragon) {
		if (dragon.dragonType == DragonType.FIRE) {
			return new ResourceLocation(IFSpellPack.MODID, "textures/entities/layers/skeletal_fire_dragon_eyes.png");
		} else if (dragon.dragonType == DragonType.ICE) {
			return new ResourceLocation(IFSpellPack.MODID, "textures/entities/layers/skeletal_ice_dragon_eyes.png");
		} else {
			return new ResourceLocation(IFSpellPack.MODID, "textures/entities/layers/skeletal_lightning_dragon_eyes.png");
		}
	}

	public boolean shouldCombineTextures() {
		return true;
	}
}
