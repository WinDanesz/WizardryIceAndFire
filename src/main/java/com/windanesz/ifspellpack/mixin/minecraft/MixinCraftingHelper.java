package com.windanesz.ifspellpack.mixin.minecraft;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.recipe.RecipeDragonSkullOmen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingHelper.class)
public abstract class MixinCraftingHelper {

/*
	@Inject(method = "Lnet/minecraftforge/common/crafting/CraftingHelper;init()V", at = @At("TAIL"))
	private void injectCraftingHelper(CallbackInfo info) {
		CraftingHelper.register(new ResourceLocation(IFSpellPack.MODID, "skull_omen"), RecipeDragonSkullOmen::factory);
	}
*/

}
