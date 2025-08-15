package com.windanesz.ifspellpack.entity.living;

import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntitySkeletalIceDragon extends EntitySkeletalDragon {

	public EntitySkeletalIceDragon(World worldIn) {
		super(worldIn, DragonType.ICE);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.isTeen() ? IafSoundRegistry.ICEDRAGON_TEEN_IDLE : this.isAdult() ? IafSoundRegistry.ICEDRAGON_ADULT_IDLE : IafSoundRegistry.ICEDRAGON_CHILD_IDLE;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return this.isTeen() ? IafSoundRegistry.ICEDRAGON_TEEN_HURT : this.isAdult() ? IafSoundRegistry.ICEDRAGON_ADULT_HURT : IafSoundRegistry.ICEDRAGON_CHILD_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return this.isTeen() ? IafSoundRegistry.ICEDRAGON_TEEN_DEATH : this.isAdult() ? IafSoundRegistry.ICEDRAGON_ADULT_DEATH : IafSoundRegistry.ICEDRAGON_CHILD_DEATH;
	}

	@Override
	public SoundEvent getRoarSound() {
		return this.isTeen() ? IafSoundRegistry.ICEDRAGON_TEEN_ROAR : this.isAdult() ? IafSoundRegistry.ICEDRAGON_ADULT_ROAR : IafSoundRegistry.ICEDRAGON_CHILD_ROAR;
	}

}
