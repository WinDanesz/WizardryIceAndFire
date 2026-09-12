package com.windanesz.ifspellpack.entity.living;

import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;


public class EntityCultistPriest extends EntityCultistMage {

	public static final String SHOULD_SPAWN_DRAGON_KEY = "ShouldSpawnDragon";

	private boolean shouldSpawnDragon;
	private int dragonAge;

	public EntityCultistPriest(World worldIn) {
		super(worldIn);
	}

	public boolean shouldSpawnDragon() {
		return this.shouldSpawnDragon;
	}

	public void setShouldSpawnDragon(boolean shouldSpawnDragon) {
		this.shouldSpawnDragon = shouldSpawnDragon;
	}

	public int getDragonAge() {
		return this.dragonAge;
	}

	public void setDragonAge(int dragonAge) {
		this.dragonAge = dragonAge;
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		if (this.getElementVariant() == 0) {

		} else if (this.getElementVariant() == 1) {

		} else if (this.getElementVariant() == 2) {

		}
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		if (this.shouldSpawnDragon()) {
			EntityFireDragon dragon = new EntityFireDragon(this.world);
			dragon.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			dragon.setOwnerId(this.getUniqueID());
			dragon.growDragon(this.getDragonAge());
			dragon.setGender(this.rand.nextBoolean());
			dragon.setAgingDisabled(true);
			dragon.setHealth(dragon.getMaxHealth());
			dragon.setVariant(new Random().nextInt(4));
			if (!this.world.isRemote) {
				this.world.spawnEntity(dragon);
			}
		}
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean(SHOULD_SPAWN_DRAGON_KEY, this.shouldSpawnDragon());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setShouldSpawnDragon(compound.getBoolean(SHOULD_SPAWN_DRAGON_KEY));
	}

}
