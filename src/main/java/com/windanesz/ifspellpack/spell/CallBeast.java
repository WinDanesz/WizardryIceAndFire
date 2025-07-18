package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.data.IStoredVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.spell.Transportation;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.NBTExtras;
import electroblob.wizardry.util.RayTracer;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CallBeast extends Spell {

	public static final IStoredVariable<List<UUID>> MOUNT_UUIDS = new IStoredVariable.StoredVariable<List<UUID>, NBTTagList>("ifspellpack:mountUUIDs", s -> NBTExtras.listToNBT(s, NBTUtil::createUUIDTag), t -> new ArrayList<>(NBTExtras.NBTToList(t, NBTUtil::getUUIDFromTag)), Persistence.ALWAYS).setSynced();

	public CallBeast() {
		super(IFSpellPack.MODID, "call_beast", SpellActions.POINT_UP, false);
		this.addProperties(RANGE);
		WizardData.registerStoredVariables(MOUNT_UUIDS);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		WizardData data = WizardData.get(caster);
		if (data != null) {
			List<UUID> uuids = data.getVariable(MOUNT_UUIDS);
			if (uuids == null) {
				uuids = new ArrayList<>();
			}

			filterMounts(uuids, world);
			Vec3d look = caster.getLookVec();
			Vec3d origin = new Vec3d(caster.posX, caster.posY + caster.getEyeHeight() - 0.25, caster.posZ);
			double range = this.getProperty(RANGE).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
			Vec3d endpoint = origin.add(look.scale(range));
			RayTraceResult rayTrace = RayTracer.rayTrace(world, origin, endpoint, 0, false, true, false, EntityTameable.class, RayTracer.ignoreEntityFilter(null));
			if (rayTrace != null && rayTrace.typeOfHit == RayTraceResult.Type.ENTITY) {
				Entity target = rayTrace.entityHit;
				if (isAcceptableBeast(target)) {
					// Remove any existing UUID of the same type before adding
					List<UUID> toRemove = new ArrayList<>();
					for (UUID uuid : uuids) {
						Entity uuidEntity = EntityUtils.getEntityByUUID(world, uuid);
						if (uuidEntity != null && uuidEntity.getClass() == target.getClass()) {
							toRemove.add(uuid);
						}
					}
					uuids.removeAll(toRemove);
					uuids.add(target.getUniqueID());
					data.setVariable(MOUNT_UUIDS, uuids);
					return false;
				}
			}
		}
		return false;
	}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {
		return false;
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {
		return false;
	}

	public static boolean isAcceptableBeast(Entity entity) {
		if (entity instanceof EntityHippogryph || entity instanceof EntityAmphithere || entity instanceof EntityHippocampus) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean containsMount(List<UUID> uuids, World world, Entity entity) {
		for (UUID uuid : uuids) {
			Entity uuidEntity = EntityUtils.getEntityByUUID(world, uuid);
			if (uuidEntity != null && uuidEntity.getClass() == entity.getClass()) {
				return true;
			}
		}
		return false;
	}

	public static void replaceMount(List<UUID> uuids, World world, Entity entity) {
		List<UUID> toRemove = new ArrayList<>();
		for (UUID uuid : uuids) {
			Entity uuidEntity = EntityUtils.getEntityByUUID(world, uuid);
			if (uuidEntity != null && uuidEntity.getClass() == entity.getClass()) {
				toRemove.add(uuid);
			}
		}
		uuids.removeAll(toRemove);
		uuids.add(entity.getUniqueID());
	}

	public static void filterMounts(List<UUID> uuids, World world) {
		for (UUID uuid : uuids) {
			Entity uuidEntity = EntityUtils.getEntityByUUID(world, uuid);
			if (uuidEntity == null) {
				uuids.remove(uuid);
			}
		}
	}
}
