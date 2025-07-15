package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.projectile.EntityHydraBreathIFSP;
import com.windanesz.ifspellpack.registry.IFSPItems;
import electroblob.wizardry.item.ItemArtefact;
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

public class HydraBreath extends Spell {

	public static final String ACCELERATION = "acceleration";
	public static final String ARTEFACT_ANGLE = "artefact_angle";

	public HydraBreath() {
		super(IFSpellPack.MODID, "hydra_breath", SpellActions.POINT, true);
		this.addProperties(DAMAGE, ACCELERATION, EFFECT_DURATION, ARTEFACT_ANGLE);
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
		if (ticksInUse <= 60) {
			if (ticksInUse % 7 == 0) {
				Vec3d look = caster.getLookVec();
				double acceleration = this.getProperty(ACCELERATION).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
				float damage = this.getProperty(DAMAGE).floatValue();
				if (ItemArtefact.isArtefactActive(caster, IFSPItems.HEAD_TRIADIC_SERPENT_CROWN)) {
					float angle = this.getProperty(ARTEFACT_ANGLE).floatValue();
					for (float f : new float[]{(float)Math.toRadians(-angle), 0, (float)Math.toRadians(angle)}) {
						Vec3d angledLook = look.rotateYaw(f);
						EntityHydraBreathIFSP hydraBreath = new EntityHydraBreathIFSP(world, caster.posX + angledLook.x, caster.posY + angledLook.y + caster.getEyeHeight() + 0.2f, caster.posZ + angledLook.z, acceleration, acceleration, acceleration);
						hydraBreath.damage = damage;
						hydraBreath.shootingEntity = caster;
						hydraBreath.accelerationX = angledLook.x * acceleration;
						hydraBreath.accelerationY = angledLook.y * acceleration;
						hydraBreath.accelerationZ = angledLook.z * acceleration;
						hydraBreath.damageMultiplier = modifiers.get(SpellModifiers.POTENCY);
						hydraBreath.durationMultiplier = modifiers.get(WizardryItems.duration_upgrade);
						this.playSound(world, caster, ticksInUse, -1, modifiers);
						if (!world.isRemote) {
							world.spawnEntity(hydraBreath);
						}
					}
				} else {
					EntityHydraBreathIFSP hydraBreath = new EntityHydraBreathIFSP(world, caster.posX + look.x, caster.posY + look.y + caster.getEyeHeight() + 0.2f, caster.posZ + look.z, acceleration, acceleration, acceleration);
					hydraBreath.damage = damage;
					hydraBreath.shootingEntity = caster;
					hydraBreath.accelerationX = look.x * acceleration;
					hydraBreath.accelerationY = look.y * acceleration;
					hydraBreath.accelerationZ = look.z * acceleration;
					hydraBreath.damageMultiplier = modifiers.get(SpellModifiers.POTENCY);
					hydraBreath.durationMultiplier = modifiers.get(WizardryItems.duration_upgrade);
					this.playSound(world, caster, ticksInUse, -1, modifiers);
					if (!world.isRemote) {
						world.spawnEntity(hydraBreath);
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		if (ticksInUse <= 60) {
			if (ticksInUse % 7 == 0) {
				Vec3d look = caster.getLookVec();
				double acceleration = this.getProperty(ACCELERATION).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
				EntityHydraBreathIFSP hydraBreath = new EntityHydraBreathIFSP(world, caster.posX + look.x, caster.posY + look.y + caster.getEyeHeight() + 0.2f, caster.posZ + look.z, acceleration, acceleration, acceleration);
				hydraBreath.shootingEntity = caster;
				hydraBreath.accelerationX = look.x * acceleration;
				hydraBreath.accelerationY = look.y * acceleration;
				hydraBreath.accelerationZ = look.z * acceleration;
				hydraBreath.damageMultiplier = modifiers.get(SpellModifiers.POTENCY);
				hydraBreath.durationMultiplier = modifiers.get(WizardryItems.duration_upgrade);
				this.playSound(world, caster, ticksInUse, - 1, modifiers);
				if (!world.isRemote) {
					world.spawnEntity(hydraBreath);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean cast(World world, double x, double y, double z, EnumFacing direction, int ticksInUse, int duration, SpellModifiers modifiers){
		if (ticksInUse <= 60) {
			if (ticksInUse % 7 == 0) {
				Vec3i look = direction.getDirectionVec();
				double acceleration = this.getProperty(ACCELERATION).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
				EntityHydraBreathIFSP hydraBreath = new EntityHydraBreathIFSP(world, x, y + 0.375f, z, acceleration, acceleration, acceleration);
				hydraBreath.accelerationX = look.getX() * acceleration;
				hydraBreath.accelerationY = look.getY() * acceleration;
				hydraBreath.accelerationZ = look.getZ() * acceleration;
				hydraBreath.damageMultiplier = modifiers.get(SpellModifiers.POTENCY);
				hydraBreath.durationMultiplier = modifiers.get(WizardryItems.duration_upgrade);
				this.playSound(world, x, y, z, ticksInUse, - 1, modifiers);
				if (!world.isRemote) {
					world.spawnEntity(hydraBreath);
				}
			}
			return true;
		}
		return false;
	}
}
