package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.FrozenEntityProperties;
import com.github.alexthe666.iceandfire.entity.IafDragonDestructionManager;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput;
import com.github.alexthe666.iceandfire.util.IsImmune;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class DragonLightningBreath extends DragonBreath {

	public static final String KNOCKBACK_STRENGTH = "knockback_strength";

	public DragonLightningBreath() {
		super("dragon_lightning_breath");
		this.addProperties(KNOCKBACK_STRENGTH);
	}

	@Override
	public void onTargetHit(World world, Vec3d hit, Vec3d origin, @Nullable EntityLivingBase caster, int ticksInUse, SpellModifiers modifiers) {
		double radius = this.getProperty(EFFECT_RADIUS).doubleValue() * modifiers.get(WizardryItems.blast_upgrade);
		if (ticksInUse % 10 == 0) {
			float damage = this.getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			List<EntityLivingBase> targets = EntityUtils.getLivingWithinRadius(radius, hit.x, hit.y, hit.z, world);
			targets.removeIf(e -> e == caster || (caster != null && !caster.canEntityBeSeen(e)) || IsImmune.toDragonLightning(e));
			for (EntityLivingBase target : targets) {
				target.attackEntityFrom(IceAndFire.dragonLightning, damage);
				target.knockBack(target, this.getProperty(KNOCKBACK_STRENGTH).floatValue() * modifiers.get(SpellModifiers.POTENCY), origin.x - target.posX, origin.z - target.posZ);
			}
		}
		List<BlockPos> posList = BlockUtils.getBlockSphere(new BlockPos(hit.x, hit.y, hit.z), radius / 2);
		for (BlockPos pos : posList) {
			IBlockState transformState = IafDragonDestructionManager.transformBlockLightning(world.getBlockState(pos));
			if (DragonBreath.canReplaceBlock(caster) && canDestroyBlock(caster, world, pos) && world.rand.nextBoolean()) {
				world.setBlockState(pos, transformState);
			}
			if (DragonBreath.canPowerForge(caster) && world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityDragonforgeInput) {
				((TileEntityDragonforgeInput)world.getTileEntity(pos)).onHitWithFlame();
			}
		}
	}
}
