package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.registry.IFSPItems;
import com.windanesz.ifspellpack.world.BeastPosData;
import electroblob.wizardry.data.IStoredVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;

import java.util.*;

public class CallBeast extends Spell {

	public static final int AMPHITHERE = 0;
	public static final int HIPPOGRYPH = 1;
	public static final int HIPPOCAMPUS = 2;
	public static final IStoredVariable<Map<UUID, Integer>> MOUNTS = new IStoredVariable.StoredVariable<Map<UUID, Integer>, NBTTagList>("ifspellpack:mountUUIDs", s -> NBTExtras.mapToNBT(s, NBTUtil::createUUIDTag, NBTTagInt::new), t -> new LinkedHashMap<>(NBTExtras.NBTToMap(t, NBTUtil::getUUIDFromTag, NBTTagInt::getInt)), Persistence.ALWAYS).setSynced();

	public CallBeast() {
		super(IFSpellPack.MODID, "call_beast", SpellActions.POINT_UP, false);
		this.addProperties(RANGE);
		WizardData.registerStoredVariables(MOUNTS);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		WizardData wizardData = WizardData.get(caster);
		if (wizardData != null) {
			boolean artefact = ItemArtefact.isArtefactActive(caster, IFSPItems.CHARM_WILDCALLER_WHISTLE);
			Map<UUID, Integer> mounts = wizardData.getVariable(MOUNTS);
			if (mounts == null) {
				mounts = new LinkedHashMap<>();
			}
			Vec3d look = caster.getLookVec();
			Vec3d origin = new Vec3d(caster.posX, caster.posY + caster.getEyeHeight() - 0.25, caster.posZ);
			double range = this.getProperty(RANGE).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
			Vec3d endpoint = origin.add(look.scale(range));
			RayTraceResult rayTrace = RayTracer.rayTrace(world, origin, endpoint, 0, false, true, false, EntityTameable.class, RayTracer.ignoreEntityFilter(null));
			if (rayTrace != null && rayTrace.typeOfHit == RayTraceResult.Type.ENTITY) {
				Entity target = rayTrace.entityHit;
				if (target instanceof EntityTameable) {
					EntityTameable entityTameable = (EntityTameable) target;
					if (isAcceptableBeast(entityTameable) && entityTameable.isTamed() && entityTameable.getOwner() == caster) {
						int beastID = beastID(entityTameable);
						UUID tameableUUID = entityTameable.getUniqueID();
						if (mounts.containsKey(tameableUUID)) {
							if (artefact) {
								caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".already_stored", entityTameable.getDisplayName()), true);
							} else {
								List<UUID> uuids = new ArrayList<>(mounts.keySet());
								UUID uuid = uuids.get(uuids.size() - 1);
								if (uuid == tameableUUID) {
									caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".already_stored", entityTameable.getDisplayName()), true);
								} else {
									caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".replace", entityTameable.getDisplayName()), true);
								}
							}
							replaceBeast(mounts, entityTameable.getUniqueID(), beastID);
							return false;
						}
						if (!containsBeast(mounts, beastID)) {
							mounts.put(entityTameable.getUniqueID(), beastID);
							if (ItemArtefact.isArtefactActive(caster, IFSPItems.CHARM_WILDCALLER_WHISTLE) || mounts.isEmpty()) {
								caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".add", entityTameable.getDisplayName()), true);
							} else {
								caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".replace", entityTameable.getDisplayName()), true);
							}
						} else {
							replaceBeast(mounts, entityTameable.getUniqueID(), beastID);
							caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".replace", entityTameable.getDisplayName()), true);
						}
						wizardData.setVariable(MOUNTS, mounts);
						return false;
					}
				}
			}
			if (mounts.size() == 0) {
				caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".no_beasts"), true);
			} else {
				if (artefact) {

				} else {
					List<UUID> uuids = new ArrayList<>(mounts.keySet());
					UUID uuid = uuids.get(uuids.size() - 1);
					if (uuid != null) {
						Entity entity = world.getMinecraftServer().getEntityFromUuid(uuid);
						if (entity != null) {
							if (summonBeast(caster, entity)) {
								return true;
							}
						} else {
							BeastPosData beastPosData = BeastPosData.get(world);
							if (beastPosData != null) {
								BlockPos pos = beastPosData.getBeastPos(uuid);
								if (pos == null) {
									caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".dead"), true);
								} else {
									ForgeChunkManager.Ticket ticket = ForgeChunkManager.requestPlayerTicket(IFSpellPack.instance, caster.getName(), world, ForgeChunkManager.Type.NORMAL);
									ForgeChunkManager.forceChunk(ticket, new ChunkPos(pos));
									entity = world.getMinecraftServer().getEntityFromUuid(uuid);
									if (entity != null) {
										if (!summonBeast(caster, entity)) {
											ForgeChunkManager.releaseTicket(ticket);
											caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".no_space", entity.getDisplayName()), true);
											return false;
										} else {
											ForgeChunkManager.releaseTicket(ticket);
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	public static int beastID(Entity entity) {
		if (entity instanceof EntityAmphithere) {
			return AMPHITHERE;
		}
		if (entity instanceof EntityHippogryph) {
			return HIPPOGRYPH;
		}
		if (entity instanceof EntityHippocampus) {
			return HIPPOCAMPUS;
		}
		return 0;
	}

	public static boolean isAcceptableBeast(EntityTameable entity) {
		if (entity instanceof EntityAmphithere || entity instanceof EntityHippogryph || entity instanceof EntityHippocampus) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean summonBeast(EntityPlayer player, Entity entity) {
		BlockPos pos = BlockUtils.findNearbyFloorSpace(player, 2, 4);
		if (pos == null) {
			return false;
		}
		entity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
		return true;
	}

	public static boolean containsBeast(Map<UUID, Integer> mounts, int beastID) {
		if (mounts.containsValue(beastID)) {
			return true;
		}
		return false;
	}

	public static void replaceBeast(Map<UUID, Integer> mounts, UUID uuid, int beastID) {
		for (Map.Entry<UUID, Integer> entry : mounts.entrySet()) {
			if (entry.getValue() == beastID) {
				mounts.remove(entry.getKey());
				break;
			}
		}
		mounts.put(uuid, beastID);
	}

}
