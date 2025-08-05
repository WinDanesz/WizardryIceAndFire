package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.living.EntityDreadBeastMinion;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class DreadHowl extends SpellRay {

	public DreadHowl() {
		super(IFSpellPack.MODID, "dread_howl", SpellActions.POINT, false);
	}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {
		return false;
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {
		return false;
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (target instanceof EntityWolf) {
			EntityWolf wolf = (EntityWolf)target;
			if (wolf.isTamed() && wolf.getOwner() == caster) {
				float potency = modifiers.get(SpellModifiers.POTENCY);
				float wolfHealthPercentage = wolf.getHealth() / wolf.getMaxHealth();
				EntityDreadBeastMinion dreadBeast = new EntityDreadBeastMinion(world);
				dreadBeast.setPositionAndRotation(wolf.posX, wolf.posY, wolf.posZ, wolf.rotationPitch, wolf.rotationYaw);
				dreadBeast.setCaster(caster);
				dreadBeast.setLifetime(-1);
				IAttributeInstance attribute = dreadBeast.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
				attribute.applyModifier(new AttributeModifier(SpellMinion.POTENCY_ATTRIBUTE_MODIFIER, potency - 1, EntityUtils.Operations.MULTIPLY_CUMULATIVE));
				dreadBeast.setHealth(dreadBeast.getMaxHealth() * wolfHealthPercentage);
				dreadBeast.setScale(MathHelper.clamp(0.85f * potency, 0.85f, 1.35f));
				if (wolf.hasCustomName()) {
					dreadBeast.setCustomNameTag(wolf.getCustomNameTag());
				}
				dreadBeast.setConvert(true);
				world.spawnEntity(dreadBeast);
				wolf.setDead();
			}
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
}
