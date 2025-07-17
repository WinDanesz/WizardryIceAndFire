package com.windanesz.ifspellpack.entity.projectile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonLightningCharge;
import com.github.alexthe666.iceandfire.entity.IafDragonDestructionManager;
import com.github.alexthe666.iceandfire.util.IsImmune;
import com.windanesz.ifspellpack.registry.IFSPSpells;
import com.windanesz.ifspellpack.spell.DragonBreath;
import com.windanesz.ifspellpack.spell.DragonLightningCharge;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.EntityUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class EntityDragonLightningChargeIFSP extends EntityDragonLightningCharge {

	public float damageMultiplier = 1f;
	public float blastMultiplier = 1f;

	public EntityDragonLightningChargeIFSP(World worldIn) {
		super(worldIn);
	}

	public EntityDragonLightningChargeIFSP(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
		super(worldIn, posX, posY, posZ, accelX, accelY, accelZ);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote) {
			//this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 4, 1);
			@Nullable EntityLivingBase shooter = this.shootingEntity;
			if (result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit != shooter) {
				float directDamage = IFSPSpells.DRAGON_LIGHTNING_CHARGE.getProperty(Spell.DIRECT_DAMAGE).floatValue() * damageMultiplier;
				result.entityHit.attackEntityFrom(IceAndFire.dragonLightning, directDamage);
			}
			Vec3d hit = result.hitVec;
			double radius = IFSPSpells.DRAGON_LIGHTNING_CHARGE.getProperty(Spell.EFFECT_RADIUS).doubleValue() * this.blastMultiplier;
			float damage = IFSPSpells.DRAGON_LIGHTNING_CHARGE.getProperty(Spell.SPLASH_DAMAGE).floatValue() * damageMultiplier;
			List<EntityLivingBase> entities = EntityUtils.getLivingWithinRadius(radius, hit.x, hit.y, hit.z, this.world);
			entities.removeIf(e -> e == shooter || !(e.canEntityBeSeen(this)) || IsImmune.toDragonLightning(e));
			for (EntityLivingBase entity : entities) {
				entity.attackEntityFrom(IceAndFire.dragonLightning, damage);
				//entity.knockBack(entity, IFSPSpells.DRAGON_LIGHTNING_CHARGE.getProperty(DragonLightningCharge.KNOCKBACK_STRENGTH).floatValue() * damageMultiplier, hit.x - entity.posX, hit.z - entity.posZ);
			}
			BlockPos center = new BlockPos(hit.x, hit.y, hit.z);
			List<BlockPos> blocksPos = BlockUtils.getBlockSphere(center, radius);
			for (BlockPos pos : blocksPos) {
				IBlockState transformState = IafDragonDestructionManager.transformBlockLightning(world.getBlockState(pos));
				if (DragonBreath.canDestroyBlock(shooter, world, pos)) {
					if (world.rand.nextFloat() > pos.distanceSq(center)) {
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
					}
					else if (DragonBreath.canReplaceBlock(shooter) && world.rand.nextBoolean() && world.getBlockState(pos) != Blocks.AIR.getDefaultState()) {
						world.setBlockState(pos, transformState);
					}
				}
			}
			this.setDead();
		}
	}

}
