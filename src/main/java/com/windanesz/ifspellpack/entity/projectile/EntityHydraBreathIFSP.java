package com.windanesz.ifspellpack.entity.projectile;

import com.github.alexthe666.iceandfire.entity.EntityHydra;
import com.github.alexthe666.iceandfire.entity.EntityHydraBreath;
import com.windanesz.ifspellpack.registry.IFSPSpells;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.spell.SpellBuff;
import electroblob.wizardry.util.EntityUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityHydraBreathIFSP extends EntityHydraBreath {

	public float damageMultiplier = 1.0f;
	public float durationMultiplier = 1.0f;

	public EntityHydraBreathIFSP(World worldIn) {
		super(worldIn);
	}

	public EntityHydraBreathIFSP(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
		super(worldIn, posX, posY, posZ, accelX, accelY, accelZ);
	}

	public EntityHydraBreathIFSP(World worldIn, EntityHydra shooter, double accelX, double accelY, double accelZ) {
		super(worldIn, shooter, accelX, accelY, accelZ);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		//ignore intellij warning
		if (result != null) {
			if (EntityUtils.isLiving(result.entityHit)) {
				EntityLivingBase entityLivingBase = (EntityLivingBase) result.entityHit;
				if (!(entityLivingBase == this.shootingEntity)) {
					int duration = (int)(IFSPSpells.HYDRA_BREATH.getProperty(Spell.EFFECT_DURATION).floatValue() * this.durationMultiplier);
					int amplifier = SpellBuff.getStandardBonusAmplifier(damageMultiplier);
					entityLivingBase.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), this.damageMultiplier);
					entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.POISON, duration, amplifier));
					this.setDead();
				}
			}
		}
	}

	//testing
	@Override
	public void onUpdate() {
		super.onUpdate();
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound){
		super.readEntityFromNBT(nbttagcompound);
		damageMultiplier = nbttagcompound.getFloat("DamageMultiplier");
		durationMultiplier = nbttagcompound.getFloat("DurationMultiplier");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound){
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setFloat("DamageMultiplier", damageMultiplier);
		nbttagcompound.setFloat("DurationMultiplier", durationMultiplier);
	}
}
