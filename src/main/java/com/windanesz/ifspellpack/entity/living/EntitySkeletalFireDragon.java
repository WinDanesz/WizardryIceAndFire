package com.windanesz.ifspellpack.entity.living;

import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntitySkeletalFireDragon extends EntityFireDragon {

	public EntitySkeletalFireDragon(World worldIn) {
		super(worldIn);
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		return false;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer player) {
		return 0;
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
