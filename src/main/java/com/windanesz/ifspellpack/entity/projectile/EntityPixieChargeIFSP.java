package com.windanesz.ifspellpack.entity.projectile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.entity.EntityPixieCharge;
import com.windanesz.ifspellpack.registry.IFSPSpells;
import electroblob.wizardry.spell.Spell;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityPixieChargeIFSP extends EntityPixieCharge {

	public final float[] rgb;
	public float damageMultiplier = 1f;
	public float durationMultiplier = 1f;

	public EntityPixieChargeIFSP(World worldIn) {
		super(worldIn);
		this.rgb = 	EntityPixie.PARTICLE_RGB[this.rand.nextInt(EntityPixie.PARTICLE_RGB.length)];
	}

	public EntityPixieChargeIFSP(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
		super(worldIn, posX, posY, posZ, accelX, accelY, accelZ);
		this.rgb = 	EntityPixie.PARTICLE_RGB[this.rand.nextInt(EntityPixie.PARTICLE_RGB.length)];
	}

	public EntityPixieChargeIFSP(World worldIn, EntityPlayer shooter, double accelX, double accelY, double accelZ) {
		super(worldIn, shooter, accelX, accelY, accelZ);
		this.rgb = 	EntityPixie.PARTICLE_RGB[this.rand.nextInt(EntityPixie.PARTICLE_RGB.length)];
	}

	@Override
	protected void onImpact(RayTraceResult rayTraceResult) {
		if (rayTraceResult == null) return;
		if (!this.world.isRemote) {
			if (rayTraceResult.entityHit instanceof EntityLivingBase) {
				int duration = (int)(IFSPSpells.PIXIE_CHARGE.getProperty(Spell.EFFECT_DURATION).floatValue() * durationMultiplier);
				float damage = IFSPSpells.PIXIE_CHARGE.getProperty(Spell.DAMAGE).floatValue() * damageMultiplier;
				((EntityLivingBase) rayTraceResult.entityHit).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, duration));
				((EntityLivingBase) rayTraceResult.entityHit).addPotionEffect(new PotionEffect(MobEffects.GLOWING, duration));
				rayTraceResult.entityHit.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.shootingEntity), damage);
			}
			if (this.world.isRemote) {
				for (int i = 0; i < 20; ++i) {
					IceAndFire.PROXY.spawnParticle("if_pixie", this.posX + this.rand.nextDouble() * 1F * (this.rand.nextBoolean() ? -1 : 1), this.posY + this.rand.nextDouble() * 1F * (this.rand.nextBoolean() ? -1 : 1), this.posZ + this.rand.nextDouble() * 1F * (this.rand.nextBoolean() ? -1 : 1), rgb[0], rgb[1], rgb[2]);
				}
			}
			this.setDead();
		}
	}
}
