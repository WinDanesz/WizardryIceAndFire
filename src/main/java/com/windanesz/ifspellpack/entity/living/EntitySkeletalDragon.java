package com.windanesz.ifspellpack.entity.living;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntitySkeletalDragon extends EntityDragonBase {


	public EntitySkeletalDragon(World world, DragonType type) {
		super(world, type, 1, 1 + IceAndFire.CONFIG.dragonAttackDamage, IceAndFire.CONFIG.dragonHealth * 0.04, IceAndFire.CONFIG.dragonHealth, 0.15F, 0.4F);
	}

	@Override
	protected void breathFireAtPos(BlockPos burningTarget) {
	}

	@Override
	protected Item getHeartItem() {
		return null;
	}

	@Override
	protected Item getBloodItem() {
		return null;
	}

	@Override
	public ResourceLocation getDeadLootTable() {
		return null;
	}

	@Override
	public String getVariantName(int variant) {
		return "skeletal_" + this.dragonType.getName() + "_";
	}

	@Override
	public Item getVariantScale(int variant) {
		return null;
	}

	@Override
	public Item getVariantEgg(int variant) {
		return null;
	}

	@Override
	public Item getSummoningCrystal() {
		return null;
	}

	@Override
	public SoundEvent getRoarSound() {
		return null;
	}

	@Override
	public void stimulateFire(double burnX, double burnY, double burnZ, int syncType) {
	}

	@Override
	public void tryScorchTarget() {
	}

	@Override
	public boolean isSkeletal() {
		return true;
	}

	@Override
	public boolean isFlying() {
		return false;
	}

	@Override
	public void setFlying(boolean flying) {
	}

	@Override
	public boolean useFlyingPathFinder() {
		return false;
	}

	@Override
	public boolean canMateWith(EntityAnimal otherAnimal) {
		return false;
	}

	@Override
	public boolean isAgingDisabled() {
		return true;
	}

	@Override
	public void setAgingDisabled(boolean isAgingDisabled) {
	}

	@Override
	public boolean isAllowedToTriggerFlight() {
		return false;
	}
}
