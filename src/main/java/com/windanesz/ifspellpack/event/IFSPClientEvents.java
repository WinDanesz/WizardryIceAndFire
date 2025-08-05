package com.windanesz.ifspellpack.event;

import baubles.api.BaublesApi;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.item.ItemChargedArtefact;
import com.windanesz.ifspellpack.registry.IFSPPackets;
import com.windanesz.ifspellpack.network.C2SPacketPixieWingGlider;
import com.windanesz.ifspellpack.registry.IFSPItems;
import com.windanesz.ifspellpack.registry.IFSPParticles;
import com.windanesz.ifspellpack.registry.IFSPPotions;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class IFSPClientEvents {

	public static final ResourceLocation SIREN_SHADER = new ResourceLocation(IFSpellPack.MODID, "shaders/post/siren.json");

	@SubscribeEvent
	public static void onInputUpdateEvent(InputUpdateEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		//Pixie wing glider
		if (event.getMovementInput().jump && ItemArtefact.isArtefactActive(player, IFSPItems.BODY_PIXIE_WING_GLIDER)) {
			if (player.isCreative() || ItemChargedArtefact.consumeCharge(BaublesApi.getBaublesHandler(player).getStackInSlot(5))) {
				IMessage msg = new C2SPacketPixieWingGlider.Message();
				IFSPPackets.net.sendToServer(msg);
				Spells.glide.cast(player.world, player, EnumHand.MAIN_HAND, player.ticksExisted, new SpellModifiers());
			}
		}
	}

	@SubscribeEvent
	public static void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.world;
		if (world.isRemote) {
			EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
			if (entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				if (player.isPotionActive(IFSPPotions.ALLURE)) {
					if (world.rand.nextInt(40) == 0) {
						ParticleBuilder.create(IFSPParticles.ALLURE_APPEARANCE).pos(player.getPositionVector()).spawn(world);
					}
					if (IceAndFire.CONFIG.sirenShader && !renderer.isShaderActive()) {
						renderer.loadShader(SIREN_SHADER);
					}
				} else {
					if (IceAndFire.CONFIG.sirenShader && renderer.getShaderGroup() != null && renderer.getShaderGroup().getShaderGroupName() != null && SIREN_SHADER.toString().equals(renderer.getShaderGroup().getShaderGroupName())) {
						renderer.stopUseShader();
					}
				}
			}
		}
	}

}
