package com.windanesz.ifspellpack.mixin;

import com.github.alexthe666.iceandfire.entity.EntityTroll;
import com.windanesz.ifspellpack.entity.living.EntityTrollMinion;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityTroll.class)
public class MixinEntityTroll {

	@Shadow
	private boolean avoidSun;

	@Unique
	private boolean shouldPetrify() {
		// Return false for EntityTrollMinion, true for regular EntityTroll
		if ((Object) this instanceof EntityTrollMinion) {
			return !((EntityTrollMinion) (Object) this).isSunlightImmune();
		}
		return true;
	}

	@Redirect(method = "onLivingUpdate",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/world/World;isDaytime()Z",
					ordinal = 1)) // Use ordinal=1 for the second call to isDaytime()
	private boolean redirectIsDayTimeCheck(World world) {
		// This redirects the second call to isDaytime() and adds our shouldPetrify check
		return world.isDaytime() && shouldPetrify();
	}

	@Redirect(method = "onLivingUpdate",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/world/World;canSeeSky(Lnet/minecraft/util/math/BlockPos;)Z",
					ordinal = 0)) // Use ordinal=1 for the second call to isDaytime()
	private boolean redirectCanSeeDaylight(World instance, BlockPos pos) {
		if (instance.canSeeSky(pos) && (Object) this instanceof EntityTrollMinion) {
			((EntityTrollMinion) (Object) this).setDead();
		}
		return instance.canSeeSky(pos);
	}

	/**
	 * @author windanesz
	 * @reason Adding conditional sun avoidance for trolls
	 */
	@Overwrite
	private void setAvoidSun(boolean day) {
		// For EntityTrollMinion, check if it's sunlightImmune before avoiding sun
		if ((Object) this instanceof EntityTrollMinion && ((EntityTrollMinion) (Object) this).isSunlightImmune()) {
			((PathNavigateGround) (((EntityTroll) (Object) this).getNavigator())).setAvoidSun(false);
			avoidSun = false;
			return;
		}

		// Original logic
		if (day && !avoidSun) {
			((PathNavigateGround) (((EntityTroll) (Object) this).getNavigator())).setAvoidSun(true);
			avoidSun = true;
		}
		if (!day && avoidSun) {
			((PathNavigateGround) (((EntityTroll) (Object) this).getNavigator())).setAvoidSun(false);
			avoidSun = false;
		}
	}

}