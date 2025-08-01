package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.entity.EntityTideTrident;
import com.github.alexthe666.iceandfire.item.ItemTideTrident;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.accessor.AccessorEntityTideTrident;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SerpentSlayer extends Spell {

	public static final String DAMAGE_MULTIPLIER = "damage_multiplier";
	public static final String VELOCITY = "velocity";

	public SerpentSlayer() {
		super(IFSpellPack.MODID, "serpent_slayer", EnumAction.BOW, true);
		//DURATION is in seconds, not ticks!
		this.addProperties(DAMAGE, DURATION, DAMAGE_MULTIPLIER, VELOCITY);
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
		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		if (ticksInUse <= 20) {
			return true;
		}
		return false;
	}

	@Override
	public boolean cast(World world, double x, double y, double z, EnumFacing direction, int ticksInUse, int duration, SpellModifiers modifiers) {
		if (ticksInUse == 0) {
			float damage = this.getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			float damageMultiplier = this.getProperty(DAMAGE_MULTIPLIER).floatValue();
			float velocity = this.getProperty(VELOCITY).floatValue() * modifiers.get(WizardryItems.range_upgrade);
			int fireDuration = (int) (this.getProperty(DURATION).floatValue() * modifiers.get(WizardryItems.duration_upgrade));
			EntityTideTrident trident = new EntityTideTrident(world, x, y, z, ItemStack.EMPTY);
			trident.shoot(direction.getXOffset(), direction.getYOffset(), direction.getZOffset(), velocity, 1f);
			trident.setDamage(damage);
			trident.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
			((AccessorEntityTideTrident) trident).ifspellpack$setSpell(true);
			((AccessorEntityTideTrident) trident).ifspellpack$setDamageMultiplier(damageMultiplier);
			((AccessorEntityTideTrident) trident).ifspellpack$setBurnDuration(fireDuration);
			if (!world.isRemote) {
				world.spawnEntity(trident);
			}
			return true;
		}
		return false;
	}

	@Override
	public void finishCasting(World world, @Nullable EntityLivingBase caster, double x, double y, double z, @Nullable EnumFacing direction, int duration, SpellModifiers modifiers) {
		if (caster != null) {
			float damage = this.getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			float damageMultiplier = this.getProperty(DAMAGE_MULTIPLIER).floatValue();
			float velocity = this.getProperty(VELOCITY).floatValue() * modifiers.get(WizardryItems.range_upgrade);
			int fireDuration = (int)(this.getProperty(DURATION).floatValue() * modifiers.get(WizardryItems.duration_upgrade));
			velocity *= ItemTideTrident.getArrowVelocity(duration);
			EntityTideTrident trident = new EntityTideTrident(world, caster, ItemStack.EMPTY);
			float inaccuracy = caster instanceof EntityPlayer ? 1f : EntityUtils.getDefaultAimingError(world.getDifficulty());
			trident.shoot(caster, caster.rotationPitch, caster.rotationYaw, 0, velocity, inaccuracy);
			trident.setDamage(damage);
			trident.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
			((AccessorEntityTideTrident)trident).ifspellpack$setSpell(true);
			((AccessorEntityTideTrident)trident).ifspellpack$setDamageMultiplier(damageMultiplier);
			((AccessorEntityTideTrident)trident).ifspellpack$setBurnDuration(fireDuration);
			if (!world.isRemote) {
				world.spawnEntity(trident);
			}
		}
	}

}
