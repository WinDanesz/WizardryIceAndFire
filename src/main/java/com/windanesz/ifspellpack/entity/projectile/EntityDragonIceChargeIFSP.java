package com.windanesz.ifspellpack.entity.projectile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonIceCharge;
import com.github.alexthe666.iceandfire.entity.FrozenEntityProperties;
import com.github.alexthe666.iceandfire.entity.IafDragonDestructionManager;
import com.github.alexthe666.iceandfire.util.IsImmune;
import com.windanesz.ifspellpack.registry.IFSPSpells;
import com.windanesz.ifspellpack.spell.DragonBreath;
import com.windanesz.ifspellpack.spell.DragonIceBreath;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.EntityUtils;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class EntityDragonIceChargeIFSP extends EntityDragonIceCharge {

	public float damageMultiplier = 1f;
	public float blastMultiplier = 1f;
	public float durationMultiplier = 1f;

	public EntityDragonIceChargeIFSP(World worldIn) {
		super(worldIn);
	}

	public EntityDragonIceChargeIFSP(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
		super(worldIn, posX, posY, posZ, accelX, accelY, accelZ);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote) {
			//this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 4, 1);
			@Nullable EntityLivingBase shooter = this.shootingEntity;
			if (result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit != shooter) {
				float directDamage = IFSPSpells.DRAGON_ICE_CHARGE.getProperty(Spell.DIRECT_DAMAGE).floatValue() * damageMultiplier;
				result.entityHit.attackEntityFrom(IceAndFire.dragonIce, directDamage);
			}
			Vec3d hit = result.hitVec;
			double radius = IFSPSpells.DRAGON_ICE_CHARGE.getProperty(Spell.EFFECT_RADIUS).doubleValue() * this.blastMultiplier;
			int duration = (int)(IFSPSpells.DRAGON_ICE_CHARGE.getProperty(Spell.EFFECT_DURATION).intValue() * this.durationMultiplier);
			float damage = IFSPSpells.DRAGON_ICE_CHARGE.getProperty(Spell.SPLASH_DAMAGE).floatValue() * damageMultiplier;
			List<EntityLivingBase> entities = EntityUtils.getLivingWithinRadius(radius, hit.x, hit.y, hit.z, this.world);
			entities.removeIf(e -> e == shooter || !(e.canEntityBeSeen(this)) || IsImmune.toDragonIce(e));
			for (EntityLivingBase entity : entities) {
				FrozenEntityProperties frozenProps = EntityPropertiesHandler.INSTANCE.getProperties(entity, FrozenEntityProperties.class);
				if (frozenProps != null) frozenProps.setFrozenFor(duration);
				entity.attackEntityFrom(IceAndFire.dragonIce, damage);
			}
			BlockPos center = new BlockPos(hit.x, hit.y, hit.z);
			List<BlockPos> blocksPos = BlockUtils.getBlockSphere(center, radius);
			for (BlockPos pos : blocksPos) {
				IBlockState transformState = IafDragonDestructionManager.transformBlockIce(world.getBlockState(pos));
				if (DragonBreath.canDestroyBlock(shooter, world, pos)) {
					if (world.rand.nextFloat() > pos.distanceSq(center)) {
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
					}
					else if (DragonBreath.canReplaceBlock(shooter) && world.rand.nextBoolean() && world.getBlockState(pos) != Blocks.AIR.getDefaultState()) {
						world.setBlockState(pos, transformState);
					}
				}
				if (DragonBreath.canPlaceBlock(shooter, world, pos) && world.rand.nextBoolean()) {
					DragonIceBreath.generateSpikes(world, pos, transformState);
				}
			}
			this.setDead();
		}
	}

}
