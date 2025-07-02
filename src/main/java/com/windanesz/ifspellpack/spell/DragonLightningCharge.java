package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.projectile.EntityDragonLightningChargeIFSP;
import com.windanesz.ifspellpack.entity.projectile.EntityDragonLightningChargeIFSP;
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

public class DragonLightningCharge extends Spell {

	public static final String ACCELERATION = "acceleration";
	public static final String KNOCKBACK_STRENGTH = "knockback_strength";

	public DragonLightningCharge() {
		super(IFSpellPack.MODID, "dragon_lightning_charge", SpellActions.POINT, false);
		this.addProperties(DIRECT_DAMAGE, SPLASH_DAMAGE, EFFECT_RADIUS, ACCELERATION, KNOCKBACK_STRENGTH);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (!world.isRemote) {
			Vec3d look = caster.getLookVec();
			double acceleration = this.getProperty(ACCELERATION).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
			EntityDragonLightningChargeIFSP dragonLightningCharge = new EntityDragonLightningChargeIFSP(world, caster.posX, caster.posY + caster.getEyeHeight(), caster.posZ, acceleration, acceleration, acceleration);
			dragonLightningCharge.shootingEntity = caster;
			dragonLightningCharge.accelerationX = look.x * acceleration;
			dragonLightningCharge.accelerationY = look.y * acceleration;
			dragonLightningCharge.accelerationZ = look.z * acceleration;
			dragonLightningCharge.damageMultiplier = modifiers.get(SpellModifiers.POTENCY);
			dragonLightningCharge.blastMultiplier = modifiers.get(WizardryItems.blast_upgrade);
			this.playSound(world, caster, ticksInUse, - 1, modifiers);
			world.spawnEntity(dragonLightningCharge);
		}
		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		if(target != null){
			if(!world.isRemote){
				double acceleration = this.getProperty(ACCELERATION).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
				EntityDragonLightningChargeIFSP dragonLightningCharge = new EntityDragonLightningChargeIFSP(world, caster.posX, caster.posY + caster.getEyeHeight(), caster.posZ, 0, 0, 0);
				double dx = target.posX - caster.posX;
				double dy = target.posY + (double)(target.height / 2.0F) - (caster.posY + (double)(caster.height / 2.0F));
				double dz = target.posZ - caster.posZ;
				dragonLightningCharge.accelerationX = dx / caster.getDistance(target) * acceleration;
				dragonLightningCharge.accelerationY = dy / caster.getDistance(target) * acceleration;
				dragonLightningCharge.accelerationZ = dz / caster.getDistance(target) * acceleration;
				dragonLightningCharge.shootingEntity = caster;
				dragonLightningCharge.damageMultiplier = modifiers.get(SpellModifiers.POTENCY);
				dragonLightningCharge.blastMultiplier = modifiers.get(WizardryItems.blast_upgrade);
				world.spawnEntity(dragonLightningCharge);
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
			EntityDragonLightningChargeIFSP dragonLightningCharge = new EntityDragonLightningChargeIFSP(world, x, y, z, 0, 0, 0);
			Vec3i vec = direction.getDirectionVec();
			dragonLightningCharge.accelerationX = vec.getX() * acceleration;
			dragonLightningCharge.accelerationY = vec.getY() * acceleration;
			dragonLightningCharge.accelerationZ = vec.getZ() * acceleration;
			dragonLightningCharge.damageMultiplier = modifiers.get(SpellModifiers.POTENCY);
			dragonLightningCharge.blastMultiplier = modifiers.get(WizardryItems.blast_upgrade);
			world.spawnEntity(dragonLightningCharge);
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
