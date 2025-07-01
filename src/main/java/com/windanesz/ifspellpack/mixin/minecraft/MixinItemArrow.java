package com.windanesz.ifspellpack.mixin.minecraft;

import com.windanesz.ifspellpack.enchantment.EnchantmentDragonbane;
import com.windanesz.ifspellpack.registry.IFSPEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemArrow.class)
public abstract class MixinItemArrow {

	@Inject(method = "createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/entity/projectile/EntityArrow;", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void injectCreateArrow(World worldIn, ItemStack stack, EntityLivingBase shooter, CallbackInfoReturnable<EntityArrow> info, EntityTippedArrow arrow) {
		int level = EnchantmentHelper.getEnchantmentLevel(IFSPEnchantments.DRAGONBANE, stack);
		arrow.getEntityData().setInteger(EnchantmentDragonbane.DRAGONBANE_KEY, level);
	}

}
