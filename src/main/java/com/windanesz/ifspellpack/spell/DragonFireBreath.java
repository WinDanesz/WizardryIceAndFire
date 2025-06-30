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

public class DragonFireBreath extends DragonBreath {

	public DragonFireBreath() {
		super("dragon_fire_breath");
	}

	public void onTargetHit(World world, Vec3d hit, Vec3d origin, @Nullable EntityLivingBase caster, int ticksInUse, SpellModifiers modifiers) {
		double radius = this.getProperty(EFFECT_RADIUS).doubleValue() * modifiers.get(WizardryItems.blast_upgrade);
		if (ticksInUse % 10 == 0) {
			float damageScale = this.getProperty(DAMAGE_SCALE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			float durationScale = this.getProperty(EFFECT_DURATION).floatValue() * modifiers.get(WizardryItems.duration_upgrade);
			List<EntityLivingBase> targets = EntityUtils.getEntitiesWithinRadius(radius, hit.x, hit.y, hit.z, world, EntityLivingBase.class);
			targets.removeIf(e -> e == caster || (caster != null && !caster.canEntityBeSeen(e)));
			for (EntityLivingBase target : targets) {
				target.setFire(5 + (int)(5 * durationScale));
				target.attackEntityFrom(IceAndFire.dragonFire, (float)IceAndFire.CONFIG.dragonAttackDamageFire * damageScale);
			}
		}
		List<BlockPos> posList = BlockUtils.getBlockSphere(new BlockPos(hit.x, hit.y, hit.z), radius);
		for (BlockPos pos : posList) {
			IBlockState transformState = IafDragonDestructionManager.transformBlockFire(world.getBlockState(pos));
			if (DragonBreath.canReplaceBlock(caster) && canDestroyBlock(caster, world, pos) && world.rand.nextBoolean()) {
				world.setBlockState(pos, transformState);
			}
			if (DragonBreath.canPlaceBlock(caster, world, pos) && world.rand.nextBoolean() && transformState.isFullBlock() && world.isAirBlock(pos.up())) {
				world.setBlockState(pos.up(), Blocks.FIRE.getDefaultState());
			}
			if (DragonBreath.canPowerForge(caster) && world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityDragonforgeInput) {
				((TileEntityDragonforgeInput)world.getTileEntity(pos)).onHitWithFlame();
			}
		}
	}

	@Override
	protected void spawnParticle(World world, double x, double y, double z, double vx, double vy, double vz) {
		IceAndFire.PROXY.spawnParticle("dragonfire", x, y, z, vx, vy, vz, 1);
	}

}
