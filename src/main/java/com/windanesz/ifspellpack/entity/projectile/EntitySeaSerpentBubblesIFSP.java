package com.windanesz.ifspellpack.entity.projectile;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import com.github.alexthe666.iceandfire.entity.EntitySeaSerpentBubbles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySeaSerpentBubblesIFSP extends EntitySeaSerpentBubbles {

	private Entity target;
	private float damage;

	public EntitySeaSerpentBubblesIFSP(World worldIn) {
		super(worldIn);
	}

	public EntitySeaSerpentBubblesIFSP(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
		super(worldIn, posX, posY, posZ, accelX, accelY, accelZ);
	}

	public EntitySeaSerpentBubblesIFSP(World worldIn, EntitySeaSerpent shooter, double accelX, double accelY, double accelZ) {
		super(worldIn, shooter, accelX, accelY, accelZ);
	}

	public float getDamage() {
		return this.damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public Entity getTarget() {
		return this.target;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}

	@Override
	public void autoTarget() {
		if (this.getTarget() != null) {
			double d2 = target.posX - posX;
			double d3 = target.posY - posY;
			double d4 = target.posZ - posZ;
			double d0 = MathHelper.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
			this.accelerationX = d2 / d0 * 0.1D;
			this.accelerationY = d3 / d0 * 0.1D;
			this.accelerationZ = d4 / d0 * 0.1D;
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result != null) {
			if (result.entityHit != null && result.entityHit != this.shootingEntity) {
				result.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), this.getDamage());
				this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1F, this.rand.nextFloat());
				this.setDead();
			}
		}
	}
}
