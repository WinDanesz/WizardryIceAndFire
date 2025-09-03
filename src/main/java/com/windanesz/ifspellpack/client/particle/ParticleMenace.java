package com.windanesz.ifspellpack.client.particle;

import electroblob.wizardry.Wizardry;
import electroblob.wizardry.client.particle.ParticleWizardry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ParticleMenace extends ParticleWizardry {

	public ParticleMenace(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn);
		this.particleRed = 1.0F;
		this.particleGreen = 1.0F;
		this.particleBlue = 1.0F;
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.particleGravity = 0.0F;
		this.particleMaxAge = 1;
	}

	public int getFXLayer()
	{
		return 3;
	}

	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		if (this.entity != null) {
			if (!Wizardry.proxy.isFirstPerson(this.entity)) {
				RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
				float f2 = 240.0F;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f2, f2);
				double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - rendermanager.viewerPosX;
				double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - rendermanager.viewerPosY;
				double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - rendermanager.viewerPosZ;
				GlStateManager.pushMatrix();
				GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5f);
				double scale = 2.5 + (0.25 * Math.sin((this.entity.ticksExisted + partialTicks) / 4));
				GlStateManager.scale(scale, scale, scale);
				GlStateManager.depthMask(false);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				rendermanager.renderEntity(entity, x, y, z, entity.rotationYaw, partialTicks, false);
				GlStateManager.depthMask(true);
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			}
		}
	}
	
}
