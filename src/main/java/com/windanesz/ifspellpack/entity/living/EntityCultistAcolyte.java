package com.windanesz.ifspellpack.entity.living;

import com.windanesz.ifspellpack.registry.IFSPItems;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCultistAcolyte extends EntityAbstractCultist {

	public EntityCultistAcolyte(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(3, new EntityAIAttackMelee(this, this.getMovementSpeed(), true));
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(IFSPItems.DRAGONBONE_DAGGER));
		if (this.getElementVariant() == 0) {

		} else if (this.getElementVariant() == 1) {

		} else if (this.getElementVariant() == 2) {

		}
	}

}
