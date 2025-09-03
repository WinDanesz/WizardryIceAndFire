/*package com.windanesz.ifspellpack.mixin.minecraft;

import com.windanesz.ifspellpack.registry.IFSPItems;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderLivingBase.class)
public abstract class MixinRenderLivingBase<T extends EntityLivingBase> {

	@Redirect(method = "Lnet/minecraft/client/renderer/entity/RenderLivingBase;renderModel(Lnet/minecraft/entity/EntityLivingBase;FFFFFF)V", at = @At(value = "INVOKE", target = "isInvisibleToPlayer(Lnet/minecraft/entity/player/EntityPlayer;)Z"))
	private boolean redirectIsInvisibleToPlayer(T entityLivingBase) {
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		return !entityLivingBase.isInvisibleToPlayer(player) || ItemArtefact.isArtefactActive(player, IFSPItems.CHARM_DRAGON_EYE);
	}
}*/
