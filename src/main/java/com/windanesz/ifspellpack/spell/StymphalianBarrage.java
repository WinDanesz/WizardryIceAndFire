package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianFeather;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.accessor.AccessorEntityStymphalianFeather;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.RayTracer;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class StymphalianBarrage extends Spell {

	public static final String DAMAGE_SCALE = "damage_scale";
	public static final String VELOCITY = "velocity";
	public static final String AIM_ASSIST = "aim_assist";

	public StymphalianBarrage() {
		super(IFSpellPack.MODID, "stymphalian_barrage", SpellActions.POINT, true);
		this.addProperties(DAMAGE_SCALE, VELOCITY, AIM_ASSIST);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (ticksInUse <= 7) {
			if (ticksInUse == 0 || ticksInUse == 7) {
				Vec3d look = caster.getLookVec();
				Vec3d origin = new Vec3d(caster.posX, caster.posY + caster.getEyeHeight() - 0.25f, caster.posZ);
				shootSpell(world, origin, look, caster, modifiers);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		if (ticksInUse <= 7) {
			if (ticksInUse == 0 || ticksInUse == 7) {
				Vec3d look = caster.getLookVec();
				Vec3d origin = new Vec3d(caster.posX, caster.posY + caster.getEyeHeight() - 0.25f, caster.posZ);
				shootSpell(world, origin, look, caster, modifiers);
			}
			return true;
		}
		return false;
	}

	protected void shootSpell(World world, Vec3d origin, Vec3d direction, EntityLivingBase caster, SpellModifiers modifiers){
		double range = IceAndFire.CONFIG.stymphalianBirdTargetSearchLength * modifiers.get(WizardryItems.range_upgrade);
		Vec3d endpoint = origin.add(direction.scale(range));
		RayTraceResult rayTrace = RayTracer.rayTrace(world, origin, endpoint, this.getProperty(AIM_ASSIST).floatValue(), false,
				true, false, EntityLivingBase.class, RayTracer.ignoreEntityFilter(caster));
		boolean flag = false;
		if(rayTrace != null){
			if(rayTrace.typeOfHit == RayTraceResult.Type.ENTITY){
				flag = this.onEntityHit(world, rayTrace.hitVec, caster, modifiers);
			}else if(rayTrace.typeOfHit == RayTraceResult.Type.BLOCK){
				flag = this.onBlockHit(world, rayTrace.hitVec, caster, modifiers);
			}
		}
		if (!flag) {
			this.onMiss(world, caster, modifiers);
		}
	}

	protected boolean onEntityHit(World world, Vec3d hit, EntityLivingBase caster, SpellModifiers modifiers) {
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
			((AccessorEntityStymphalianFeather)feather).ifspellpack$setDroppable(false);
			feather.setDamage(feather.getDamage() * damage_multiplier);
			feather.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
			feather.shoot(d0, d1 + d3 * 0.10000000298023224D, d2, velocity, inaccuracy);
			caster.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (world.rand.nextFloat() * 0.4F + 0.8F));
			if (!world.isRemote) {
				world.spawnEntity(feather);
			}
		}
		return true;
	}

	protected boolean onBlockHit(World world, Vec3d hit, EntityLivingBase caster, SpellModifiers modifiers) {
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
			((AccessorEntityStymphalianFeather)feather).ifspellpack$setDroppable(false);
			feather.setDamage(feather.getDamage() * damage_multiplier);
			feather.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
			feather.shoot(d0, d1 + d3 * 0.10000000298023224D, d2, velocity, inaccuracy);
			caster.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (world.rand.nextFloat() * 0.4F + 0.8F));
			if (!world.isRemote) {
				world.spawnEntity(feather);
			}
		}
		return true;
	}

	protected void onMiss(World world, EntityLivingBase caster, SpellModifiers modifiers) {
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
			if (!world.isRemote) {
				world.spawnEntity(feather);
			}
		}
	}

}
