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
		this.addProperties(EFFECT_DURATION);
	}

	public void onTargetHit(World world, Vec3d hit, Vec3d origin, @Nullable EntityLivingBase caster, int ticksInUse, SpellModifiers modifiers) {
		double radius = this.getProperty(EFFECT_RADIUS).doubleValue() * modifiers.get(WizardryItems.blast_upgrade);
		if (ticksInUse % 10 == 0) {
			float damage = this.getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			int durationScale = (int)(this.getProperty(EFFECT_DURATION).floatValue() * modifiers.get(WizardryItems.duration_upgrade));
			List<EntityLivingBase> targets = EntityUtils.getLivingWithinRadius(radius, hit.x, hit.y, hit.z, world);
			targets.removeIf(e -> e == caster || (caster != null && !caster.canEntityBeSeen(e)) || IsImmune.toDragonIce(e));
			for (EntityLivingBase target : targets) {
				FrozenEntityProperties frozenProps = EntityPropertiesHandler.INSTANCE.getProperties(target, FrozenEntityProperties.class);
				if (frozenProps != null) frozenProps.setFrozenFor(durationScale);
				target.attackEntityFrom(IceAndFire.dragonIce, damage);
			}
		}
		if (!world.isRemote) {
			List<BlockPos> posList = BlockUtils.getBlockSphere(new BlockPos(hit.x, hit.y, hit.z), radius / 2);
			for (BlockPos pos : posList) {
				IBlockState transformState = IafDragonDestructionManager.transformBlockIce(world.getBlockState(pos));
				if (DragonBreath.canReplaceBlock(caster) && canDestroyBlock(caster, world, pos) && world.rand.nextBoolean()) {
					world.setBlockState(pos, transformState);
				}
				if (DragonBreath.canPlaceBlock(caster, world, pos) && world.rand.nextInt(9) == 0) {
					generateSpikes(world, pos, transformState);
				}
				if (DragonBreath.canPowerForge(caster) && world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityDragonforgeInput) {
					((TileEntityDragonforgeInput) world.getTileEntity(pos)).onHitWithFlame();
				}
			}
		}
	}

	public static void generateSpikes(World world, BlockPos pos, IBlockState transformState) {
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
