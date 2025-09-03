package com.windanesz.ifspellpack.client.particle;

import com.windanesz.ifspellpack.potion.PotionAllure;
import electroblob.wizardry.client.particle.ParticleWizardry;
import electroblob.wizardry.util.EntityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.UUID;

public class ParticleAllureAppearance extends ParticleWizardry {

	private EntityLivingBase allurer;

	public ParticleAllureAppearance(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn);
		this.particleRed = 1.0F;
		this.particleGreen = 1.0F;
		this.particleBlue = 1.0F;
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.particleGravity = 0.0F;
		this.particleMaxAge = 60;
	}

	public int getFXLayer()
	{
		return 3;
	}

	public void onUpdate() {
		super.onUpdate();
		if (this.allurer == null) {
			UUID uuid = Minecraft.getMinecraft().player.getEntityData().getUniqueId(PotionAllure.UUID_KEY);
			if (uuid != null) {
				Entity entity = EntityUtils.getEntityByUUID(Minecraft.getMinecraft().world, uuid);
				if (entity instanceof EntityLivingBase) {
					this.allurer = (EntityLivingBase)entity;
				}
			}
		}
	}

	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		if (this.allurer != null) {
			RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
			rendermanager.setRenderPosition(Particle.interpPosX, Particle.interpPosY, Particle.interpPosZ);
			float f1 = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge;
			GlStateManager.depthMask(true);
			GlStateManager.enableBlend();
			GlStateManager.enableDepth();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			float f2 = 240.0F;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f2, f2);
			GlStateManager.pushMatrix();
			float f3 = 0.05F + 0.5F * MathHelper.sin(f1 * (float) Math.PI);
			GlStateManager.color(1.0F, 1.0F, 1.0F, f3);
			GlStateManager.translate(0.0F, 1.8F, 0.0F);
			GlStateManager.rotate(180.0F - entityIn.rotationYaw, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(60.0F - 150.0F * f1 - entityIn.rotationPitch, 0.0F, 1.0F, 0.0F);
			GlStateManager.translate(0.0F, -0.8F, -1.5F);
			GlStateManager.scale(0.6F, 0.6F, 0.6F);
			GlStateManager.rotate((allurer.ticksExisted % 90) * 4, 0.0F, 1.0F, 0.0F);
			rendermanager.renderEntity(this.allurer, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
			GlStateManager.popMatrix();
			GlStateManager.enableDepth();
		}
	}
}
