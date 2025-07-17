package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.projectile.EntitySeaSerpentBubblesIFSP;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SeaSerpentBubbles extends SpellRay {


	public static final String ACCELERATION = "acceleration";

	public SeaSerpentBubbles() {
		super(IFSpellPack.MODID, "sea_serpent_bubbles", SpellActions.POINT, false);
		this.addProperties(DAMAGE, ACCELERATION);
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (caster != null) {
			Vec3d look = caster.getLookVec();
			double acceleration = this.getProperty(ACCELERATION).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
			EntitySeaSerpentBubblesIFSP bubbles = new EntitySeaSerpentBubblesIFSP(world, origin.x + look.x, origin.y + look.y, origin.z + look.z, acceleration, acceleration, acceleration);
			bubbles.shootingEntity = caster;
			bubbles.accelerationX = look.x * acceleration;
			bubbles.accelerationY = look.y * acceleration;
			bubbles.accelerationZ = look.z * acceleration;
			bubbles.setDamage(this.getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY));
			bubbles.setTarget(target);
			if (!world.isRemote) {
				world.spawnEntity(bubbles);
			}
		} else {
			Vec3d look = hit.subtract(origin).normalize();
			double acceleration = this.getProperty(ACCELERATION).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
			EntitySeaSerpentBubblesIFSP bubbles = new EntitySeaSerpentBubblesIFSP(world, origin.x, origin.y + 0.375f, origin.z, acceleration, acceleration, acceleration);
			bubbles.shootingEntity = caster;
			bubbles.accelerationX = look.x * acceleration;
			bubbles.accelerationY = look.y * acceleration;
			bubbles.accelerationZ = look.z * acceleration;
			bubbles.setDamage(this.getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY));
			bubbles.setTarget(target);
			if (!world.isRemote) {
				world.spawnEntity(bubbles);
			}
		}
		return true;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		if (caster != null) {
			Vec3d look = caster.getLookVec();
			double acceleration = this.getProperty(ACCELERATION).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
			EntitySeaSerpentBubblesIFSP bubbles = new EntitySeaSerpentBubblesIFSP(world, origin.x + look.x, origin.y + look.y, origin.z + look.z, acceleration, acceleration, acceleration);
			bubbles.shootingEntity = caster;
			bubbles.accelerationX = look.x * acceleration;
			bubbles.accelerationY = look.y * acceleration;
			bubbles.accelerationZ = look.z * acceleration;
			bubbles.setDamage(this.getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY));
			if (!world.isRemote) {
				world.spawnEntity(bubbles);
			}
		} else {
			double acceleration = this.getProperty(ACCELERATION).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
			EntitySeaSerpentBubblesIFSP bubbles = new EntitySeaSerpentBubblesIFSP(world, origin.x, origin.y + 0.375f, origin.z, acceleration, acceleration, acceleration);
			bubbles.shootingEntity = caster;
			bubbles.accelerationX = direction.x * acceleration;
			bubbles.accelerationY = direction.y * acceleration;
			bubbles.accelerationZ = direction.z * acceleration;
			bubbles.setDamage(this.getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY));
			if (!world.isRemote) {
				world.spawnEntity(bubbles);
			}
		}
		return true;
	}

}
