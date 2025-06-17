package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianFeather;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.accessor.AccessorEntityStymphalianFeather;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class StymphalianBarrage extends SpellRay {

	public static final String DAMAGE_SCALE = "damage_scale";
	public static final String VELOCITY = "velocity";

	public StymphalianBarrage() {
		super(IFSpellPack.MODID, "stymphalian_barrage", SpellActions.POINT, true);
		this.addProperties(DAMAGE_SCALE);
	}

	//The feathers spawn from around the body of the character. It would not make sense on a dispenser.
	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {
		return false;
	}

	@Override
	protected double getRange(World world, Vec3d origin, Vec3d direction, @Nullable EntityLivingBase caster, int ticksInUse, SpellModifiers modifiers) {
		return IceAndFire.CONFIG.stymphalianBirdTargetSearchLength * modifiers.get(WizardryItems.range_upgrade);
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (caster != null) {
			if (ticksInUse <= 7) {
				if (ticksInUse == 0 || ticksInUse == 7) {
					float velocity = this.getProperty(VELOCITY).floatValue() * modifiers.get(WizardryItems.range_upgrade);
					float damage_multiplier = this.getProperty(DAMAGE_SCALE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
					int inaccuracy = caster instanceof EntityPlayer ? 2 : EntityUtils.getDefaultAimingError(world.getDifficulty());
					for (int i = 0; i < 4; i++) {
						float wingX = (float) (caster.posX + 1.8F * 0.5F * Math.cos((caster.rotationYaw + 180 * (i % 2)) * Math.PI / 180));
						float wingZ = (float) (caster.posZ + 1.8F * 0.5F * Math.sin((caster.rotationYaw + 180 * (i % 2)) * Math.PI / 180));
						float wingY = (float) (caster.posY + 1F);
						double d0 = hit.x - wingX;
						double d1 = hit.y - wingY;
						double d2 = hit.z - wingZ;
						double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
						EntityStymphalianFeather feather = new EntityStymphalianFeather(world, caster);
						feather.setPosition(wingX, wingY, wingZ);
						((AccessorEntityStymphalianFeather) feather).ifspellpack$setDroppable(false);
						feather.setDamage(feather.getDamage() * damage_multiplier);
						feather.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
						feather.shoot(d0, d1 + d3 * 0.10000000298023224D, d2, velocity, (float) 2);
						feather.shoot(caster, caster.rotationPitch, caster.rotationYaw, 0, velocity, inaccuracy);
						caster.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (world.rand.nextFloat() * 0.4F + 0.8F));
						world.spawnEntity(feather);
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (caster != null) {
			if (ticksInUse <= 7) {
				if (ticksInUse == 0 || ticksInUse == 7) {
					float velocity = this.getProperty(VELOCITY).floatValue() * modifiers.get(WizardryItems.range_upgrade);
					float damage_multiplier = this.getProperty(DAMAGE_SCALE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
					int inaccuracy = caster instanceof EntityPlayer ? 2 : EntityUtils.getDefaultAimingError(world.getDifficulty());
					for (int i = 0; i < 4; i++) {
						float wingX = (float) (caster.posX + 1.8F * 0.5F * Math.cos((caster.rotationYaw + 180 * (i % 2)) * Math.PI / 180));
						float wingZ = (float) (caster.posZ + 1.8F * 0.5F * Math.sin((caster.rotationYaw + 180 * (i % 2)) * Math.PI / 180));
						float wingY = (float) (caster.posY + 1F);
						double d0 = hit.x - wingX;
						double d1 = hit.y - wingY;
						double d2 = hit.z - wingZ;
						double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
						EntityStymphalianFeather feather = new EntityStymphalianFeather(world, caster);
						feather.setPosition(wingX, wingY, wingZ);
						((AccessorEntityStymphalianFeather) feather).ifspellpack$setDroppable(false);
						feather.setDamage(feather.getDamage() * damage_multiplier);
						feather.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
						feather.shoot(d0, d1 + d3 * 0.10000000298023224D, d2, velocity, (float) 2);
						feather.shoot(caster, caster.rotationPitch, caster.rotationYaw, 0, velocity, inaccuracy);
						caster.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (world.rand.nextFloat() * 0.4F + 0.8F));
						world.spawnEntity(feather);
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		if (caster != null) {
			if (ticksInUse <= 7) {
				if (ticksInUse == 0 || ticksInUse == 7) {
					float velocity = this.getProperty(VELOCITY).floatValue() * modifiers.get(WizardryItems.range_upgrade);
					float damage_multiplier = this.getProperty(DAMAGE_SCALE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
					int inaccuracy = caster instanceof EntityPlayer ? 2 : EntityUtils.getDefaultAimingError(world.getDifficulty());
					for (int i = 0; i < 4; i++) {
						float wingX = (float) (caster.posX + 1.8F * 0.5F * Math.cos((caster.rotationYaw + 180 * (i % 2)) * Math.PI / 180));
						float wingZ = (float) (caster.posZ + 1.8F * 0.5F * Math.sin((caster.rotationYaw + 180 * (i % 2)) * Math.PI / 180));
						float wingY = (float) (caster.posY + 1F);
						EntityStymphalianFeather feather = new EntityStymphalianFeather(world, caster);
						feather.setPosition(wingX, wingY, wingZ);
						((AccessorEntityStymphalianFeather) feather).ifspellpack$setDroppable(false);
						feather.setDamage(feather.getDamage() * damage_multiplier);
						feather.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
						feather.shoot(caster, caster.rotationPitch, caster.rotationYaw, 0, velocity, inaccuracy);
						caster.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (world.rand.nextFloat() * 0.4F + 0.8F));
						world.spawnEntity(feather);
					}
				}
				return true;
			}
		}
		return false;
	}
}
