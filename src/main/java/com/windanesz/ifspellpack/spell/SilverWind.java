package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SilverWind extends SpellCone {

	public SilverWind() {
		super(IFSpellPack.MODID, "silver_wind", SpellActions.POINT, false);
		this.addProperties(DAMAGE);
		this.particleVelocity = 0.2;
		this.soundValues(1f, 0.5f, 0.1f);
	}

	public boolean applyEffects(World world, Vec3d origin, @Nullable EntityLivingBase caster, EntityLivingBase target, int ticksInUse, SpellModifiers modifiers) {
		if (target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
			float damage = this.getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			target.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.RADIANT), damage);
			//EntityUtils.attackEntityWithoutKnockback(target, MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.RADIANT), damage);
		}
		return true;
	}

	@Override
	protected void spawnParticle(World world, double x, double y, double z, double vx, double vy, double vz) {
		ParticleBuilder.create(ParticleBuilder.Type.DUST).pos(x, y, z).vel(vx, vy, vz).collide(true).spawn(world);
	}
}
