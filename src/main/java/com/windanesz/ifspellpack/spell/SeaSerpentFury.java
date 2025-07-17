package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.SpellAreaEffect;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SeaSerpentFury extends SpellAreaEffect {

	public static final String KNOCKBACK_STRENGTH = "knockback_strength";

	public SeaSerpentFury() {
		super(IFSpellPack.MODID, "sea_serpent_fury", SpellActions.POINT_DOWN, false);
		this.addProperties(KNOCKBACK_STRENGTH);
		this.alwaysSucceed = true;
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (caster.isInWater()) {
			return super.cast(world, caster, hand, ticksInUse, modifiers);
		} else {
			return false;
		}
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		if (caster.isInWater()) {
			return super.cast(world, caster, hand, ticksInUse, target, modifiers);
		} else {
			return false;
		}
	}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {
		return false;
	}

	@Override
	protected boolean affectEntity(World world, Vec3d origin, @Nullable EntityLivingBase caster, EntityLivingBase target, int targetCount, int ticksInUse, SpellModifiers modifiers) {
		if (caster != null) {
			double attackDamage;
			IAttributeInstance attributeInstance = caster.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
			if (attributeInstance == null) {
				attackDamage = SharedMonsterAttributes.ATTACK_DAMAGE.getDefaultValue();
			} else {
				attackDamage = attributeInstance.getAttributeValue();
			}
			float damage = (float)attackDamage * modifiers.get(SpellModifiers.POTENCY);
			target.attackEntityFrom(DamageSource.causeMobDamage(caster), damage);
			double xRatio = origin.x - target.posX;
			double zRatio = origin.z - target.posZ;
			float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
			float strength = this.getProperty(KNOCKBACK_STRENGTH).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			target.motionX /= 2.0D;
			target.motionZ /= 2.0D;
			target.motionX -= xRatio / (double) f * (double) strength;
			target.motionZ -= zRatio / (double) f * (double) strength;
			target.motionY += strength;
		}
		return true;
	}

	@Override
	protected void spawnParticle(World world, double x, double y, double z) {
		EnumParticleTypes[] particleTypes = new EnumParticleTypes[]{EnumParticleTypes.FIREWORKS_SPARK, EnumParticleTypes.WATER_BUBBLE, EnumParticleTypes.WATER_BUBBLE, EnumParticleTypes.WATER_BUBBLE};
		for (EnumParticleTypes particleType : particleTypes) {
			double motionX = world.rand.nextGaussian() * 0.07D;
			double motionY = world.rand.nextGaussian() * 0.07D;
			double motionZ = world.rand.nextGaussian() * 0.07D;
			world.spawnParticle(particleType, true, x, y, z, motionX, motionY, motionZ, 0);
		}
	}
}
