package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.github.alexthe666.iceandfire.entity.StoneEntityProperties;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.registry.IFSPItems;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.SpellModifiers;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ExplodeStatue extends SpellRay {


	private static final String DAMAGE_SCALE = "damage_scale";

	public ExplodeStatue() {
		super(IFSpellPack.MODID, "explode_statue", SpellActions.POINT, false);
		this.addProperties(BLAST_RADIUS, DAMAGE_SCALE);
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (isEntityStatue(target) && target instanceof EntityLiving) {
			explodeStatue(world, caster, (EntityLiving)target, this.getProperty(BLAST_RADIUS).doubleValue(), this.getProperty(DAMAGE_SCALE).floatValue(), modifiers);
		}
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	public static boolean isEntityStatue(Entity entity) {
		if (entity instanceof EntityStoneStatue) {
			return true;
		} else if (entity instanceof EntityLiving) {
			StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entity, StoneEntityProperties.class);
			if (properties.isStone) {
				return true;
			}
		}
		return false;
	}

	public static void explodeStatue(World world, @Nullable EntityLivingBase caster, EntityLiving statue, double radius, float damageScale, SpellModifiers modifiers) {
		Vec3d statueVec3d = new Vec3d(statue.posX, statue.posY + statue.height / 2, statue.posZ);
		statue.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 4, 0.9f + world.rand.nextFloat() * 0.2f);
		statue.setDead();
		radius *= modifiers.get(WizardryItems.blast_upgrade);
		damageScale *= modifiers.get(SpellModifiers.POTENCY);
		double radius2 = radius * 2;
		List<EntityLivingBase> targets = EntityUtils.getLivingWithinRadius(radius, statueVec3d.x, statueVec3d.y, statueVec3d.z, world);
		List<EntityLiving> chainTargets = new ArrayList<>();
		DamageSource damageSource = caster == null ? MagicDamage.causeDirectMagicDamage(statue, MagicDamage.DamageType.BLAST) : MagicDamage.causeIndirectMagicDamage(statue, caster, MagicDamage.DamageType.BLAST);
		targets.removeIf(e -> !AllyDesignationSystem.isValidTarget(caster, e) || e == statue || !e.isEntityAlive());
		for (EntityLivingBase target : targets) {
			double d12 = target.getDistance(statueVec3d.x, statueVec3d.y, statueVec3d.z) / radius2;
			if (d12 <= 1.0D) {
				double d5 = target.posX - statueVec3d.x;
				double d7 = target.posY + (double)target.getEyeHeight() - statueVec3d.y;
				double d9 = target.posZ - statueVec3d.z;
				double d13 = MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
				if (d13 != 0.0D) {
					d5 /= d13;
					d7 /= d13;
					d9 /= d13;
					double d14 = world.getBlockDensity(statueVec3d, target.getEntityBoundingBox());
					double d10 = (1.0D - d12) * d14;
					target.attackEntityFrom(damageSource, (float)((int)((d10 * d10 + d10) / 2.0D * 7.0D * radius2 + 1.0D)) * damageScale);
					if (isEntityStatue(target)) {
						chainTargets.add((EntityLiving)target);
					}
					double d11 = EnchantmentProtection.getBlastDamageReduction(target, d10);
					target.motionX += d5 * d11;
					target.motionY += d7 * d11;
					target.motionZ += d9 * d11;
					if (target instanceof EntityPlayerMP) {
						EntityPlayerMP player = (EntityPlayerMP) target;
						if (!player.isSpectator() && (!player.isCreative() || !player.capabilities.isFlying)) {
							player.connection.sendPacket(new SPacketEntityVelocity(player));
						}
					}
				}
			}
		}
		if (world instanceof WorldServer) {
			((WorldServer)world).spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, statueVec3d.x, statueVec3d.y, statueVec3d.z, 1,0d, 0d, 0d, 0d);
		}
		if (caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer)caster, IFSPItems.CHARM_FRACTURE_CATALYST)) {
			for (EntityLiving chainTarget : chainTargets) {
				explodeStatue(world, caster, chainTarget, radius, damageScale, modifiers);
			}
		}
	}

}
