package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.FrozenEntityProperties;
import com.github.alexthe666.iceandfire.entity.IafDragonDestructionManager;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput;
import com.github.alexthe666.iceandfire.util.IsImmune;
import com.windanesz.ifspellpack.registry.IFSPBlocks;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class DragonIceBreath extends DragonBreath {

	public DragonIceBreath() {
		super("dragon_ice_breath");
	}

	public void onTargetHit(World world, Vec3d hit, Vec3d origin, @Nullable EntityLivingBase caster, int ticksInUse, SpellModifiers modifiers) {
		double radius = this.getProperty(EFFECT_RADIUS).doubleValue() * modifiers.get(WizardryItems.blast_upgrade);
		if (ticksInUse % 10 == 0) {
			float damageScale = this.getProperty(DAMAGE_SCALE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			int durationScale = (int)(this.getProperty(EFFECT_DURATION).floatValue() * modifiers.get(WizardryItems.duration_upgrade));
			List<EntityLivingBase> targets = EntityUtils.getEntitiesWithinRadius(radius, hit.x, hit.y, hit.z, world, EntityLivingBase.class);
			targets.removeIf(e -> e == caster || (caster != null && !caster.canEntityBeSeen(e)));
			for (EntityLivingBase target : targets) {
				if (!IsImmune.toDragonIce(target)) {
					FrozenEntityProperties frozenProps = EntityPropertiesHandler.INSTANCE.getProperties(target, FrozenEntityProperties.class);
					if (frozenProps != null) frozenProps.setFrozenFor(durationScale);
					target.attackEntityFrom(IceAndFire.dragonIce, (float) IceAndFire.CONFIG.dragonAttackDamageIce * damageScale);
				}
			}
		}
		List<BlockPos> posList = BlockUtils.getBlockSphere(new BlockPos(hit.x, hit.y, hit.z), radius);
		for (BlockPos pos : posList) {
			IBlockState transformState = IafDragonDestructionManager.transformBlockIce(world.getBlockState(pos));
			if (DragonBreath.canReplaceBlock(caster) && canDestroyBlock(caster, world, pos) && world.rand.nextBoolean()) {
				world.setBlockState(pos, transformState);
			}
			if (DragonBreath.canPlaceBlock(caster, world, pos) && world.rand.nextInt(9) == 0) {
				generateSpikes(world, pos, transformState);
			}
			if (DragonBreath.canPowerForge(caster) && world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityDragonforgeInput) {
				((TileEntityDragonforgeInput)world.getTileEntity(pos)).onHitWithFlame();
			}
		}
	}

	private static void generateSpikes(World world, BlockPos pos, IBlockState transformState) {
		for (EnumFacing facing : EnumFacing.VALUES) {
			BlockPos spikePos = pos.offset(facing);
			if (transformState.isFullBlock() && world.isAirBlock(spikePos)) {
				world.setBlockState(spikePos, IFSPBlocks.ICE_SPIKES.getDefaultState().withProperty(BlockDirectional.FACING, facing));
			}
		}
	}

	@Override
	protected void spawnParticle(World world, double x, double y, double z, double vx, double vy, double vz) {
		IceAndFire.PROXY.spawnParticle("dragonice", x, y, z, vx, vy, vz, 1);
	}

}
