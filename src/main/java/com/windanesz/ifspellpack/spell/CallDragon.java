package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.entity.*;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.accessor.AccessorEntityTameable;
import com.windanesz.ifspellpack.registry.IFSPPackets;
import com.windanesz.ifspellpack.network.S2CPacketCallDragon;
import com.windanesz.ifspellpack.registry.IFSPItems;
import com.windanesz.ifspellpack.world.EntityPosData;
import electroblob.wizardry.data.IStoredVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.NBTExtras;
import electroblob.wizardry.util.RayTracer;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;

import java.util.*;

public class CallDragon extends Spell {

	public static final int FIRE_DRAGON = 0;
	public static final int ICE_DRAGON = 1;
	public static final int LIGHTNING_DRAGON = 2;
	public static final String DEAD = "dead";
	public static final IStoredVariable<Map<Integer, String>> MOUNTS = new IStoredVariable.StoredVariable<Map<Integer, String>, NBTTagList>("ifspellpack:storedDragons", s -> NBTExtras.mapToNBT(s, NBTTagInt::new, NBTTagString::new), t -> new LinkedHashMap<>(NBTExtras.NBTToMap(t, NBTTagInt::getInt, NBTTagString::getString)), Persistence.ALWAYS).setSynced();

	public CallDragon() {
		super(IFSpellPack.MODID, "call_dragon", SpellActions.POINT_UP, false);
		this.addProperties(RANGE);
		WizardData.registerStoredVariables(MOUNTS);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (!world.isRemote) {
			WizardData wizardData = WizardData.get(caster);
			if (wizardData != null) {
				boolean artefact = ItemArtefact.isArtefactActive(caster, IFSPItems.CHARM_TRICHOMATIC_CRYSTAL);
				Map<Integer, String> mounts = wizardData.getVariable(MOUNTS);
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
						EntityTameable entityTameable = (EntityTameable)target;
						if (isAcceptableDragon(entityTameable) && entityTameable.isTamed() && entityTameable.getOwner() == caster) {
							int dragonID = dragonID(entityTameable);
							UUID tameableUUID = entityTameable.getUniqueID();
							if (mounts.containsValue(tameableUUID.toString())) {
								if (artefact) {
									caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".already_stored", entityTameable.getDisplayName()), true);
								} else {
									List<String> strings = new ArrayList<>(mounts.values());
									String string = strings.get(strings.size() - 1);
									//Check if the String is a valid UUID
									if (stringIsUUID(string)) {
										UUID uuid = UUID.fromString(string);
										if (uuid.equals(tameableUUID)) {
											caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".already_stored", entityTameable.getDisplayName()), true);
										} else {
											caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".replace", entityTameable.getDisplayName()), true);
										}
									} else {
										//String is not a UUID, indicating that the mount is dead and a new one should be added
										caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".add", entityTameable.getDisplayName()), true);
									}
								}
								replaceDragon(mounts, entityTameable.getUniqueID(), dragonID);
								wizardData.setVariable(MOUNTS, mounts);
								((AccessorEntityTameable)entityTameable).ifspellpack$setShouldSavePos(true);
								return false;
							}
							if (!containsDragon(mounts, dragonID)) {
								mounts.put(dragonID, tameableUUID.toString());
								if (artefact || mounts.isEmpty()) {
									caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".add", entityTameable.getDisplayName()), true);
								} else {
									caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".replace", entityTameable.getDisplayName()), true);
								}
							} else {
								if (mounts.get(dragonID).equals(DEAD)) {
									caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".add", entityTameable.getDisplayName()), true);
								} else {
									caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".replace", entityTameable.getDisplayName()), true);
								}
								replaceDragon(mounts, entityTameable.getUniqueID(), dragonID);
							}
							wizardData.setVariable(MOUNTS, mounts);
							return false;
						}
					}
				}
				if (mounts.size() == 0) {
					caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".no_dragons"), true);
				} else {
					if (artefact) {
						if (caster instanceof EntityPlayerMP) {
							boolean[] enabledMounts = new boolean[]{false, false, false};
							//Set valid dragon types to true to display in the GUI
							for (Map.Entry<Integer, String> entry : mounts.entrySet()) {
								String string = entry.getValue();
								//Check if the String is a valid UUID
								try {
									UUID uuid = UUID.fromString(string);
									enabledMounts[entry.getKey()] = true;
								} catch (IllegalArgumentException e) {
									//Dont do anything if the String is not a UUID
								}
							}
							IFSPPackets.net.sendTo(new S2CPacketCallDragon.Message(enabledMounts), (EntityPlayerMP) caster);
							this.playSound(world, caster, ticksInUse, -1, modifiers);
							return true;
						}
					} else {
						List<String> strings = new ArrayList<>(mounts.values());
						//get the last mount's UUID String
						String string = strings.get(strings.size() - 1);
						List<Integer> dragonIDs = new ArrayList<>(mounts.keySet());
						int dragonID = dragonIDs.get(dragonIDs.size() - 1);
						//Check if the String is a valid UUID
						if (stringIsUUID(string)) {
							UUID uuid = UUID.fromString(string);
							Entity entity = world.getMinecraftServer().getEntityFromUuid(uuid);
							if (entity != null) {
								if (summonDragon(caster, entity)) {
									this.playSound(world, caster, ticksInUse, -1, modifiers);
									return true;
								}
							} else {
								EntityPosData entityPosData = EntityPosData.get(world);
								if (entityPosData != null) {
									BlockPos pos = entityPosData.getEntityPos(uuid);
									if (pos == null) {
										//Put a non UUID String "dead" to indicate the dragon is dead
										mounts.put(dragonID, DEAD);
										wizardData.setVariable(MOUNTS, mounts);
										caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".dead"), true);
									} else {
										ForgeChunkManager.Ticket ticket = ForgeChunkManager.requestPlayerTicket(IFSpellPack.instance, caster.getName(), world, ForgeChunkManager.Type.NORMAL);
										ForgeChunkManager.forceChunk(ticket, new ChunkPos(pos));
										entity = world.getMinecraftServer().getEntityFromUuid(uuid);
										if (entity != null) {
											if (!summonDragon(caster, entity)) {
												ForgeChunkManager.releaseTicket(ticket);
												caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".no_space", entity.getDisplayName()), true);
												return false;
											} else {
												this.playSound(world, caster, ticksInUse, -1, modifiers);
												ForgeChunkManager.releaseTicket(ticket);
												return true;
											}
										}
									}
								}
							}
						} else {
							//String is not a UUID, indicating that the mount is dead
							caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".dead"), true);
							return false;
						}
					}
				}
			}
		}
		return false;
	}

	public static int dragonID(Entity entity) {
		if (entity instanceof EntityFireDragon) {
			return FIRE_DRAGON;
		}
		if (entity instanceof EntityIceDragon) {
			return ICE_DRAGON;
		}
		if (entity instanceof EntityLightningDragon) {
			return LIGHTNING_DRAGON;
		}
		return 0;
	}

	public static boolean isAcceptableDragon(EntityTameable entity) {
		if (entity instanceof EntityFireDragon || entity instanceof EntityIceDragon || entity instanceof EntityLightningDragon) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean summonDragon(EntityPlayer player, Entity entity) {
		BlockPos pos = BlockUtils.findNearbyFloorSpace(player, 4, 4);
		if (pos == null) {
			return false;
		}
		entity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
		return true;
	}

	public static boolean containsDragon(Map<Integer, String> mounts, int dragonID) {
		if (mounts.containsKey(dragonID)) {
			return true;
		}
		return false;
	}

	public static void replaceDragon(Map<Integer, String> mounts, UUID uuid, int dragonID) {
		if (mounts.containsKey(dragonID)) {
			mounts.remove(dragonID);
			mounts.put(dragonID, uuid.toString());
		}
	}

	public static boolean stringIsUUID(String string) {
		try {
			//Check if the String is a UUID and return true
			UUID.fromString(string);
			return true;
		} catch (IllegalArgumentException e) {
			//Catch the exception and return false if the String is not a UUID
			return false;
		}
	}

}

