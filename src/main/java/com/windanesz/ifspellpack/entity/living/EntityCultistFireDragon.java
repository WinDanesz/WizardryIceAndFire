package com.windanesz.ifspellpack.entity.living;

import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.windanesz.ifspellpack.entity.IPatroller;
import com.windanesz.ifspellpack.world.Patrol;
import net.minecraft.world.World;

public class EntityCultistFireDragon extends EntityFireDragon implements IPatroller {

	private Patrol patrol;

	public EntityCultistFireDragon(World worldIn) {
		super(worldIn);
	}

	@Override
	protected boolean canDespawn() {
		if (this.isInPatrol()) {
			return true;
		}
		return super.canDespawn();
	}

	@Override
	public void setDead() {
		if (this.isInPatrol()) {
			this.getPatrol().removePatroller(this);
		}
		super.setDead();
	}

	@Override
	public Patrol getPatrol() {
		return this.patrol;
	}

	@Override
	public void setPatrol(Patrol patrol) {
		this.patrol = patrol;
	}

}
