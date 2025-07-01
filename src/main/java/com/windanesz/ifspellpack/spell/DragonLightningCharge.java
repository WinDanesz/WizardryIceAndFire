package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.projectile.EntityDragonIceChargeIFSP;
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
			double acceleration = this.getProperty(ACCELERATION).doubleValue();
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
		return super.cast(world, caster, hand, ticksInUse, target, modifiers);
	}

	@Override
	public boolean cast(World world, double x, double y, double z, EnumFacing direction, int ticksInUse, int duration, SpellModifiers modifiers) {
		return super.cast(world, x, y, z, direction, ticksInUse, duration, modifiers);
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
