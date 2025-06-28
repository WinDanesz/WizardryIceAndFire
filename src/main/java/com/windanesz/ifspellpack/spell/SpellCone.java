package com.windanesz.ifspellpack.spell;

import electroblob.wizardry.Wizardry;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class SpellCone extends Spell {

	protected static final double Y_OFFSET = 0.25;
	public static final String ANGLE = "angle";

	public SpellCone(String modID, String name, EnumAction action, boolean isContinuous) {
		super(modID, name, action, isContinuous);
		this.addProperties(RANGE, ANGLE);
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
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers){
		Vec3d look = caster.getLookVec();
		Vec3d origin = new Vec3d(caster.posX, caster.posY + caster.getEyeHeight() - Y_OFFSET, caster.posZ);
		if(!this.isContinuous && world.isRemote && !Wizardry.proxy.isFirstPerson(caster)){
			origin = origin.add(look.scale(1.2));
		}
		if(!shootCone(world, origin, look, caster, ticksInUse, modifiers)) return false;
		if(casterSwingsArm(world, caster, hand, ticksInUse, modifiers)) caster.swingArm(hand);
		this.playSound(world, caster, ticksInUse, -1, modifiers);
		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers){
		// IDEA: Add in an aiming error and trigger onMiss accordingly
		Vec3d origin = new Vec3d(caster.posX, caster.posY + caster.getEyeHeight() - Y_OFFSET, caster.posZ);
		Vec3d targetPos = target.getPositionVector();
		if(!shootCone(world, origin, targetPos.subtract(origin).normalize(), caster, ticksInUse, modifiers)) return false;
		if(casterSwingsArm(world, caster, hand, ticksInUse, modifiers)) caster.swingArm(hand);
		this.playSound(world, caster, ticksInUse, -1, modifiers);
		return true;
	}

	@Override
	public boolean cast(World world, double x, double y, double z, EnumFacing direction, int ticksInUse, int duration, SpellModifiers modifiers){
		Vec3d vec = new Vec3d(direction.getDirectionVec());
		Vec3d origin = new Vec3d(x, y, z);
		if(!shootCone(world, origin, vec, null, ticksInUse, modifiers)) return false;
		// This MUST be the coordinates of the actual dispenser, so we need to offset it
		this.playSound(world, x - direction.getXOffset(), y - direction.getYOffset(), z - direction.getZOffset(), ticksInUse, duration, modifiers);
		return true;
	}

	protected boolean casterSwingsArm(World world, EntityLivingBase caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers){
		return !this.isContinuous && this.action == EnumAction.NONE;
	}

	public abstract boolean applyEffects(World world, Vec3d origin, @Nullable EntityLivingBase caster, EntityLivingBase target, int ticksInUse, SpellModifiers modifiers);

	public boolean shootCone(World world, Vec3d origin, Vec3d direction, @Nullable EntityLivingBase caster, int ticksInUse, SpellModifiers modifiers) {
		boolean flag = this.alwaysFire();
		float angle = this.getProperty(ANGLE).floatValue() * modifiers.get(WizardryItems.blast_upgrade);
		List<EntityLivingBase> targets = this.getTargets(world, origin, modifiers);
		for (EntityLivingBase target : targets) {
			if (withinCone(origin, direction, target.getPositionVector(), angle) && crossesAABB(world, origin, target.getEntityBoundingBox())) {
				if (applyEffects(world, origin, caster, target, ticksInUse, modifiers)) {
					flag = true;
				}
			}
		}
		return flag;
	}

	public List<EntityLivingBase> getTargets(World world, Vec3d origin, SpellModifiers modifiers) {
		double range = this.getProperty(RANGE).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
		return EntityUtils.getEntitiesWithinRadius(range, origin.x, origin.y, origin.z, world, EntityLivingBase.class);
	}

	//if the spell should always fire or only if there are affected targets
	public boolean alwaysFire() {
		return true;
	}

	//angle is in degrees
	public static boolean withinCone(Vec3d origin, Vec3d direction, Vec3d hit, double angle) {
		direction = direction.normalize();
		Vec3d difference = hit.subtract(origin).normalize();
		double dotProduct = direction.dotProduct(difference);
		double maxAngleRad = Math.toRadians(angle);
		double minDot = Math.cos(maxAngleRad);
		return dotProduct >= minDot;
	}

	//checks if an entity's AABB is visible to the caster. This is the way mojang handles similar processes for explosions, despite it being very intensive.
	public static boolean crossesAABB(World world, Vec3d origin, AxisAlignedBB aabb) {
		double[] xArray = new double[]{aabb.minX, (aabb.minX + aabb.maxX) / 2, aabb.minX};
		double[] yArray = new double[]{aabb.minY, (aabb.minY + aabb.maxY) / 2, aabb.minY};
		double[] zArray = new double[]{aabb.minZ, (aabb.minZ + aabb.maxZ) / 2, aabb.minZ};
		for (double x : xArray) {
			for (double y : yArray) {
				for (double z : zArray) {
					Vec3d corner = new Vec3d(x, y, z);
					if (world.rayTraceBlocks(origin, corner, false, true, false) == null) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
