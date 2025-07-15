package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.projectile.EntityPixieChargeIFSP;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class PixieCharge extends Spell {

	public static final String ACCELERATION = "acceleration";

	public PixieCharge() {
		super(IFSpellPack.MODID, "pixie_charge", SpellActions.POINT, false);
		this.addProperties(DAMAGE, ACCELERATION, EFFECT_DURATION);
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {
		return true;
	}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {
		return true;
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		Vec3d look = caster.getLookVec();
		double acceleration = this.getProperty(ACCELERATION).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
		EntityPixieChargeIFSP pixieCharge = new EntityPixieChargeIFSP(world, caster.posX + look.x, caster.posY + look.y + caster.getEyeHeight() + 0.1f, caster.posZ + look.z, acceleration, acceleration, acceleration);
		pixieCharge.shootingEntity = caster;
		pixieCharge.accelerationX = look.x * acceleration;
		pixieCharge.accelerationY = look.y * acceleration;
		pixieCharge.accelerationZ = look.z * acceleration;
		pixieCharge.damageMultiplier = modifiers.get(SpellModifiers.POTENCY);
		pixieCharge.durationMultiplier = modifiers.get(WizardryItems.duration_upgrade);
		this.playSound(world, caster, ticksInUse, -1, modifiers);
		if (!world.isRemote) {
			world.spawnEntity(pixieCharge);
		}
		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		Vec3d look = caster.getLookVec();
		double acceleration = this.getProperty(ACCELERATION).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
		EntityPixieChargeIFSP pixieCharge = new EntityPixieChargeIFSP(world, caster.posX + look.x, caster.posY + look.y + caster.getEyeHeight() + 0.1f, caster.posZ + look.z, acceleration, acceleration, acceleration);
		pixieCharge.shootingEntity = caster;
		pixieCharge.accelerationX = look.x * acceleration;
		pixieCharge.accelerationY = look.y * acceleration;
		pixieCharge.accelerationZ = look.z * acceleration;
		pixieCharge.damageMultiplier = modifiers.get(SpellModifiers.POTENCY);
		pixieCharge.durationMultiplier = modifiers.get(WizardryItems.duration_upgrade);
		this.playSound(world, caster, ticksInUse, - 1, modifiers);
		if (!world.isRemote) {
			world.spawnEntity(pixieCharge);
		}
		return true;
	}

	@Override
	public boolean cast(World world, double x, double y, double z, EnumFacing direction, int ticksInUse, int duration, SpellModifiers modifiers){
		Vec3i look = direction.getDirectionVec();
		double acceleration = this.getProperty(ACCELERATION).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
		EntityPixieChargeIFSP pixieCharge = new EntityPixieChargeIFSP(world, x, y + 0.375f, z, acceleration, acceleration, acceleration);
		pixieCharge.accelerationX = look.getX() * acceleration;
		pixieCharge.accelerationY = look.getY() * acceleration;
		pixieCharge.accelerationZ = look.getZ() * acceleration;
		pixieCharge.damageMultiplier = modifiers.get(SpellModifiers.POTENCY);
		pixieCharge.durationMultiplier = modifiers.get(WizardryItems.duration_upgrade);
		this.playSound(world, x, y, z, ticksInUse, - 1, modifiers);
		if (!world.isRemote) {
			world.spawnEntity(pixieCharge);
		}
		return true;
	}
}
