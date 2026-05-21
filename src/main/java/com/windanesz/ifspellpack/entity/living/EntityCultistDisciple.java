package com.windanesz.ifspellpack.entity.living;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;


public class EntityCultistDisciple extends EntityCultistMage {

	public EntityCultistDisciple(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		if (this.getElementVariant() == 0) {

		} else if (this.getElementVariant() == 1) {

		} else if (this.getElementVariant() == 2) {

		}
	}

}
