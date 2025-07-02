package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.projectile.EntityDragonIceChargeIFSP;
import com.windanesz.ifspellpack.entity.projectile.EntityDragonIceChargeIFSP;
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

public class DragonIceCharge extends Spell {

	public static final String ACCELERATION = "acceleration";

	public DragonIceCharge() {
		super(IFSpellPack.MODID, "dragon_ice_charge", SpellActions.POINT, false);
		this.addProperties(DIRECT_DAMAGE, SPLASH_DAMAGE, EFFECT_RADIUS, EFFECT_DURATION, ACCELERATION);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (!world.isRemote) {
			Vec3d look = caster.getLookVec();
			double acceleration = this.getProperty(ACCELERATION).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
			EntityDragonIceChargeIFSP dragonIceCharge = new EntityDragonIceChargeIFSP(world, caster.posX, caster.posY + caster.getEyeHeight(), caster.posZ, acceleration, acceleration, acceleration);
			dragonIceCharge.shootingEntity = caster;
			dragonIceCharge.accelerationX = look.x * acceleration;
			dragonIceCharge.accelerationY = look.y * acceleration;
			dragonIceCharge.accelerationZ = look.z * acceleration;
			dragonIceCharge.damageMultiplier = modifiers.get(SpellModifiers.POTENCY);
			dragonIceCharge.blastMultiplier = modifiers.get(WizardryItems.blast_upgrade);
			dragonIceCharge.durationMultiplier = modifiers.get(WizardryItems.duration_upgrade);
			this.playSound(world, caster, ticksInUse, - 1, modifiers);
			world.spawnEntity(dragonIceCharge);
		}
		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		if(target != null){
			if(!world.isRemote){
				double acceleration = this.getProperty(ACCELERATION).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
				EntityDragonIceChargeIFSP dragonIceCharge = new EntityDragonIceChargeIFSP(world, caster.posX, caster.posY + caster.getEyeHeight(), caster.posZ, 0, 0, 0);
				double dx = target.posX - caster.posX;
				double dy = target.posY + (double)(target.height / 2.0F) - (caster.posY + (double)(caster.height / 2.0F));
				double dz = target.posZ - caster.posZ;
				dragonIceCharge.accelerationX = dx / caster.getDistance(target) * acceleration;
				dragonIceCharge.accelerationY = dy / caster.getDistance(target) * acceleration;
				dragonIceCharge.accelerationZ = dz / caster.getDistance(target) * acceleration;
				dragonIceCharge.shootingEntity = caster;
				dragonIceCharge.damageMultiplier = modifiers.get(SpellModifiers.POTENCY);
				dragonIceCharge.blastMultiplier = modifiers.get(WizardryItems.blast_upgrade);
				dragonIceCharge.durationMultiplier = modifiers.get(WizardryItems.duration_upgrade);
				world.spawnEntity(dragonIceCharge);
				this.playSound(world, caster, ticksInUse, -1, modifiers);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean cast(World world, double x, double y, double z, EnumFacing direction, int ticksInUse, int duration, SpellModifiers modifiers) {
		if(!world.isRemote){
			double acceleration = this.getProperty(ACCELERATION).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
			EntityDragonIceChargeIFSP dragonIceCharge = new EntityDragonIceChargeIFSP(world, x, y, z, 0, 0, 0);
			Vec3i vec = direction.getDirectionVec();
			dragonIceCharge.accelerationX = vec.getX() * acceleration;
			dragonIceCharge.accelerationY = vec.getY() * acceleration;
			dragonIceCharge.accelerationZ = vec.getZ() * acceleration;
			dragonIceCharge.damageMultiplier = modifiers.get(SpellModifiers.POTENCY);
			dragonIceCharge.blastMultiplier = modifiers.get(WizardryItems.blast_upgrade);
			dragonIceCharge.durationMultiplier = modifiers.get(WizardryItems.duration_upgrade);
			world.spawnEntity(dragonIceCharge);
			this.playSound(world, x - direction.getXOffset(), y - direction.getYOffset(), z - direction.getZOffset(), ticksInUse, duration, modifiers);
		}
		return true;
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {
		return true;
	}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {
		return true;
	}
}
