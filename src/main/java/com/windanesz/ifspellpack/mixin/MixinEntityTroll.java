package com.windanesz.ifspellpack.mixin;

import com.github.alexthe666.iceandfire.entity.EntityTroll;
import com.windanesz.ifspellpack.entity.living.EntityTrollMinion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityTroll.class)
public class MixinEntityTroll {

	@Redirect(method = "onLivingUpdate",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/world/World;isDaytime()Z",
					ordinal = 1)) // Use ordinal=1 for the second call to isDaytime()
	private boolean redirectIsDayTimeCheck(World world) {
		// This redirects the second call to isDaytime() and adds our shouldPetrify check
		return world.isDaytime() && shouldPetrify();
	}

	@Unique
	private boolean shouldPetrify() {
		// Return false for EntityTrollMinion, true for regular EntityTroll
		return !((Object) this instanceof EntityTrollMinion);
	}

}