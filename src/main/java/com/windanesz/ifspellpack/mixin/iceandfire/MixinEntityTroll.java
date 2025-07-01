package com.windanesz.ifspellpack.mixin.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntityTroll;
import com.windanesz.ifspellpack.accessor.AccessorEntityTroll;
import com.windanesz.ifspellpack.entity.ai.TrollAIRestrictSun;
import com.windanesz.ifspellpack.entity.living.EntityTrollMinion;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityTroll.class)
public abstract class MixinEntityTroll extends EntityMob implements AccessorEntityTroll {

	@Unique
	private static final DataParameter<Boolean> SUNLIGHT_IMMUNE = EntityDataManager.createKey(MixinEntityTroll.class, DataSerializers.BOOLEAN);

	public MixinEntityTroll(World worldIn) {
		super(worldIn);
	}

	@Unique
	public boolean ifspellpack$isSunlightImmune() {
		return this.dataManager.get(SUNLIGHT_IMMUNE);
	}

	@Unique
	public void ifspellpack$setSunlightImmune(boolean b) {
		this.dataManager.set(SUNLIGHT_IMMUNE, b);;
	}

	@Inject(method = "initEntityAI()V", at = @At("HEAD"))
	private void injectInitEntityAI(CallbackInfo info) {
		EntityTroll troll = (EntityTroll)((Object)this);
		troll.tasks.addTask(2, new TrollAIRestrictSun(troll));
	}

	@Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isDaytime()Z", ordinal = 1))
	private boolean redirectIsDayTime(World world) {
		return world.isDaytime() && !this.ifspellpack$isSunlightImmune();
	}

	@Inject(method = "entityInit()V", at = @At("TAIL"))
	private void injectEntityInit(CallbackInfo info) {
		this.dataManager.register(SUNLIGHT_IMMUNE, false);
	}

	@Inject(method = "writeEntityToNBT(Lnet/minecraft/nbt/NBTTagCompound;)V", at = @At("TAIL"))
	private void injectWriteEntityToNBT(NBTTagCompound compound, CallbackInfo info) {
		compound.setBoolean("SunlightImmune", this.ifspellpack$isSunlightImmune());
	}

	@Inject(method = "readEntityFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)V", at = @At("TAIL"))
	private void injectReadEntityFromNBT(NBTTagCompound compound, CallbackInfo info) {
		this.ifspellpack$setSunlightImmune(compound.getBoolean("SunlightImmune"));
	}

	@Inject(method = "onDeathUpdate()V", at = @At("HEAD"), cancellable = true)
	private void injectOnDeathUpdate(CallbackInfo info) {
		if ((Object)this instanceof EntityTrollMinion) {
			super.onDeathUpdate();
			info.cancel();
		}
	}

	@Inject(method = "onInitialSpawn(Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/entity/IEntityLivingData;)Lnet/minecraft/entity/IEntityLivingData;", at = @At("HEAD"), cancellable = true)
	private void injectOnInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata, CallbackInfoReturnable<IEntityLivingData> info) {
		if ((Object)this instanceof EntityTrollMinion) {
			livingdata = super.onInitialSpawn(difficulty, livingdata);
			info.setReturnValue(livingdata);
		}
	}

	/**
	 * @author ipdnaeip
	 * @reason added avoid sun functionality through TrollAIRestrictSun that accounts for sunlight immunity
	 */
	@Overwrite
	private void setAvoidSun(boolean day) {
	}

	//Dan's mixins

/*	@Shadow
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

	*//**
	 * @author windanesz
	 * @reason Adding conditional sun avoidance for trolls
	 *//*
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
	}*/

}