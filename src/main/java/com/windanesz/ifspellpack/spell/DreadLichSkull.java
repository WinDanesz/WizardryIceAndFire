package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.entity.EntityDreadLichSkull;
import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.data.IStoredVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.NBTExtras;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DreadLichSkull extends Spell {

	public static final String VELOCITY = "velocity";
	public static final String POTENCY_KEY = "ifspellpack:potency";
	public static final IStoredVariable<List<UUID>> DREAD_MINION_ARMY_UUIDS = new IStoredVariable.StoredVariable<List<UUID>, NBTTagList>("ifspellpack:dreadMinionArmy",
			s -> NBTExtras.listToNBT(s, NBTUtil::createUUIDTag), t -> new ArrayList<>(NBTExtras.NBTToList(t, NBTUtil::getUUIDFromTag)), Persistence.ALWAYS).setSynced();
	public static final int DREAD_MINION_ARMY_BASE_SIZE = 4;

	public DreadLichSkull() {
		super(IFSpellPack.MODID, "dread_lich_skull", SpellActions.POINT, false);
		this.addProperties(DAMAGE, VELOCITY);
		WizardData.registerStoredVariables(DREAD_MINION_ARMY_UUIDS);
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {
		return true;
	}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {
		return true;
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers){
		if(!world.isRemote){
			float damage = this.getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			float velocity = this.getProperty(VELOCITY).floatValue() * modifiers.get(WizardryItems.range_upgrade);
			EntityDreadLichSkull skull = new EntityDreadLichSkull(world, caster, damage);
			skull.shoot(caster, caster.rotationPitch, caster.rotationYaw, 0, velocity, 1f);
			skull.getEntityData().setFloat(POTENCY_KEY, modifiers.get(SpellModifiers.POTENCY));
			world.spawnEntity(skull);
		}
		this.playSound(world, caster, ticksInUse, -1, modifiers);
		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers){
		if(target != null){
			if(!world.isRemote){
				float damage = this.getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
				float velocity = this.getProperty(VELOCITY).floatValue() * modifiers.get(WizardryItems.range_upgrade);
				EntityDreadLichSkull skull = new EntityDreadLichSkull(world, caster, damage);
				double d0 = target.posX - caster.posX;
				double d1 = target.getEntityBoundingBox().minY + (double) (target.height * 2) - skull.posY;
				double d2 = target.posZ - caster.posZ;
				double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
				skull.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, velocity, EntityUtils.getDefaultAimingError(world.getDifficulty()));
				world.spawnEntity(skull);
			}
			this.playSound(world, caster, ticksInUse, -1, modifiers);
			return true;
		}
		return false;
	}

	@Override
	public boolean cast(World world, double x, double y, double z, EnumFacing direction, int ticksInUse, int duration, SpellModifiers modifiers){
		if(!world.isRemote){
			float damage = this.getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			float velocity = this.getProperty(VELOCITY).floatValue() * modifiers.get(WizardryItems.range_upgrade);
			EntityDreadLichSkull skull = new EntityDreadLichSkull(world, x, y, z);
			skull.setDamage(damage);
			Vec3i vec = direction.getDirectionVec();
			skull.shoot(vec.getX(), vec.getY(), vec.getZ(), velocity, 1);
			world.spawnEntity(skull);
		}
		this.playSound(world, x - direction.getXOffset(), y - direction.getYOffset(), z - direction.getZOffset(), ticksInUse, duration, modifiers);
		return true;
	}

}
