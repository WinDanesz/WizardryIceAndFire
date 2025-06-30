package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IDragonProof;
import com.github.alexthe666.iceandfire.entity.DragonUtils;
import com.github.alexthe666.iceandfire.entity.IafDragonDestructionManager;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput;
import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class DragonBreath extends SpellRay {

	public static final String DAMAGE_SCALE = "damage_scale";
	public static final String TICKS_UNTIL_TARGET = "ticks_until_target";

	public DragonBreath(String name) {
		super(IFSpellPack.MODID, name, SpellActions.POINT, true);
		this.addProperties(DAMAGE_SCALE, TICKS_UNTIL_TARGET, EFFECT_RADIUS, EFFECT_DURATION);
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		this.onTargetHit(world, hit, origin, caster, ticksInUse, modifiers);
		return true;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		this.onTargetHit(world, hit, origin, caster, ticksInUse, modifiers);
		return true;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return true;
	}

	public abstract void onTargetHit(World world, Vec3d hit, Vec3d origin, @Nullable EntityLivingBase caster, int ticksInUse, SpellModifiers modifiers);

	@Override
	protected void playSound(World world, double x, double y, double z, int ticksInUse, int duration, SpellModifiers modifiers, String... sounds) {
		if (ticksInUse % 5 == 0) {
			super.playSound(world, x, y, z, ticksInUse, duration, modifiers, sounds);
		}
	}

	@Override
	protected double getRange(World world, Vec3d origin, Vec3d direction, @Nullable EntityLivingBase caster, int ticksInUse, SpellModifiers modifiers) {
		int ticksUntilTarget = this.getProperty(TICKS_UNTIL_TARGET).intValue();
		double range = super.getRange(world, origin, direction, caster, ticksInUse, modifiers);
		return Math.min(range, range * ticksInUse / ticksUntilTarget);
	}

	public static boolean canDestroyBlock(@Nullable Entity breaker, World world, BlockPos pos) {
		return EntityUtils.canDamageBlocks(breaker, world) && IceAndFire.CONFIG.dragonGriefing != 2 && !(world.getBlockState(pos).getBlock() instanceof IDragonProof) && DragonUtils.canDragonBreak(world.getBlockState(pos).getBlock());
	}

	public static boolean canPlaceBlock(@Nullable Entity breaker, World world, BlockPos pos) {
		return BlockUtils.canPlaceBlock(breaker, world, pos) && IceAndFire.CONFIG.dragonGriefing != 2;
	}

	public static boolean canReplaceBlock(@Nullable EntityLivingBase caster) {
		return caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer)caster, WizardryItems.amulet_anchoring);
	}

	public static boolean canPowerForge(@Nullable EntityLivingBase caster) {
		return IFSpellPack.settings.dragonBreathSpellsPowerForge;
	}

}
