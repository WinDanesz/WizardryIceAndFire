package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.registry.IFSPItems;
import com.windanesz.ifspellpack.registry.IFSPPotions;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellBuff;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class Dragonrend extends SpellRay {

	public Dragonrend() {
		super(IFSpellPack.MODID, "dragonrend", SpellActions.POINT, false);
		this.addProperties(EFFECT_DURATION);
		this.aimAssist = 2f;
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (target instanceof EntityDragonBase) {
			int duration = (int)(this.getProperty(EFFECT_DURATION).floatValue() * modifiers.get(WizardryItems.duration_upgrade));
			int amplifier = 0;
			if (caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer)caster, IFSPItems.CHARM_DOVAHKRIID)) {
				amplifier = SpellBuff.getStandardBonusAmplifier(modifiers.get(SpellModifiers.POTENCY));
			}
			((EntityDragonBase)target).addPotionEffect(new PotionEffect(IFSPPotions.DRAGONREND, duration, amplifier));
		}
		return true;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return true;
	}

	@Override
	protected void spawnParticle(World world, double x, double y, double z, double vx, double vy, double vz) {
		IceAndFire.PROXY.spawnParticle("dread_torch", x, y, z, vx, vy, vz);
	}
}
