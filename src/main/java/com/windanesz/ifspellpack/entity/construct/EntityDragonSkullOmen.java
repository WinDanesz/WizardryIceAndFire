package com.windanesz.ifspellpack.entity.construct;

import com.github.alexthe666.iceandfire.entity.EntityDragonSkull;
import com.google.common.base.Optional;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.living.EntityAbstractCultist;
import com.windanesz.ifspellpack.entity.living.EntityCultistAcolyte;
import com.windanesz.ifspellpack.entity.living.EntityCultistDisciple;
import com.windanesz.ifspellpack.entity.living.EntityCultistPriest;
import com.windanesz.ifspellpack.registry.IFSPItems;
import com.windanesz.ifspellpack.world.WorldData;
import electroblob.wizardry.util.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityDragonSkullOmen extends EntityDragonSkull implements IEntityOwnable {

	public static final String OWNER_UUID_KEY = "OwnerUUID";
	public static final String TICKS_EXISTED_KEY = "TicksExisted";
	public static final String NEXT_PATROL_KEY = "NextPatrol";

	private static final DataParameter<Optional<UUID>> OWNER_UUID = EntityDataManager.createKey(EntityDragonSkullOmen.class, DataSerializers.OPTIONAL_UNIQUE_ID);

	private int nextPatrol;

	public EntityDragonSkullOmen(World worldIn) {
		super(worldIn);
		this.nextPatrol = this.getRandomPatrolTimer();
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(OWNER_UUID, Optional.absent());
	}

	public int getRandomPatrolTimer() {
		int difference = IFSpellPack.settings.cultistPatrolCooldownUpperbound - IFSpellPack.settings.cultistPatrolCooldownLowerbound;
		if (difference < 0) {
			IFSpellPack.logger.warn("Cultist patrol lower bound is greater than the upper bound when it should not be; setting the value to the lower bound");
			difference = 0;
		}
		return this.rand.nextInt(1 + difference) + IFSpellPack.settings.cultistPatrolCooldownLowerbound;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		//Modified from 1.20.1 PatrolSpawner code
		if (!this.world.isRemote) {
			if (this.ticksExisted >= this.getNextPatrol()) {
				WorldData worldData = WorldData.get(this.world);
				if (this.world.getTotalWorldTime() >= worldData.getNextPossiblePatrolTime()) {
					if (this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.world.getGameRules().getBoolean("doMobSpawning")) {
						double range = IFSpellPack.settings.skullOmenRange;
						if (range > 0) {
							List<EntityPlayer> players = EntityUtils.getEntitiesWithinRadius(range, this.posX, this.posY, this.posZ, this.world, EntityPlayer.class);
							players.removeIf(EntityPlayer::isSpectator);
							if (IFSpellPack.settings.cultistPatrolNeedsOwner) {
								players.removeIf(player -> player != this.getOwner());
							}
							if (!players.isEmpty()) {
								double angle = this.rand.nextDouble() * 2 * Math.PI;
								int x = (int)((24 + this.rand.nextInt(24)) * Math.cos(angle));
								int z = (int)((24 + this.rand.nextInt(24)) * Math.sin(angle));
								if (this.world.isChunkGeneratedAt(x, z)) {
									BlockPos pos = new BlockPos(this.getPosition());
									pos = pos.add(x, 0, z);
									//This generates a value from 1 to 8 inclusive according to the wiki
									int patrolLevel = this.getStage() + (int) (this.world.getDifficultyForLocation(pos).getAdditionalDifficulty() * 0.5f);
									List<EntityAbstractCultist> patrolParty = new ArrayList<>();
									List<UUID> patrolPartyUUIDs = new ArrayList<>();
									for (EntityAbstractCultist patrolMember : this.generatePatrol(patrolLevel)) {
										patrolMember.setPatrolLevel(patrolLevel);
										//Try 4 times to spawn the creature
										for (int i = 0; i < 4; i++) {
											pos = pos.add(this.rand.nextInt(8) - 4, 0, this.rand.nextInt(8) - 4);
											pos = this.world.getTopSolidOrLiquidBlock(pos);
											if (WorldEntitySpawner.canCreatureTypeSpawnBody(SpawnPlacementType.ON_GROUND, this.world, pos)) {
												patrolMember.setPosition(pos.getX(), pos.getY(), pos.getZ());
												patrolMember.onInitialSpawn(this.world.getDifficultyForLocation(pos), null);
												patrolParty.add(patrolMember);
												patrolPartyUUIDs.add(patrolMember.getUniqueID());
												this.world.spawnEntity(patrolMember);
												break;
											}
										}
									}
									for (EntityAbstractCultist patrolMember : patrolParty) {
										patrolMember.setPatrolPartyUUIDs(patrolPartyUUIDs);
									}
									this.setNextPatrol(this.ticksExisted + this.getRandomPatrolTimer());
									worldData.setNextPossiblePatrolTime(this.world.getTotalWorldTime() + IFSpellPack.settings.cultistPatrolGlobalCooldown);
								}
							}
						}
					}
				}
			}
		}
	}

	public int getNextPatrol() {
		return this.nextPatrol;
	}

	public void setNextPatrol(int nextPatrol) {
		this.nextPatrol = nextPatrol;
	}

	@Nullable
	@Override
	public UUID getOwnerId() {
		return this.dataManager.get(OWNER_UUID).orNull();
	}

	public void setOwnerId(@Nullable UUID uuid) {
		this.dataManager.set(OWNER_UUID, Optional.fromNullable(uuid));
	}

	@Nullable
	@Override
	public Entity getOwner() {
		try {
			UUID uuid = this.getOwnerId();
			return uuid == null ? null : this.world.getPlayerEntityByUUID(uuid);
		} catch (IllegalArgumentException exception) {
			return null;
		}
	}

	public void turnIntoItem() {
		if (this.isDead)
			return;
		this.setDead();
		ItemStack stack = new ItemStack(IFSPItems.DRAGON_SKULL_OMEN, 1, this.getType());
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger("Stage", this.getStage());
		stack.getTagCompound().setInteger("DragonAge", this.getDragonAge());
		if (!this.world.isRemote) {
			this.entityDropItem(stack, 0.0F);
		}

	}

	public List<EntityAbstractCultist> generatePatrol(int patrolLevel) {
		List<EntityAbstractCultist> patrolMembers = new ArrayList<>();
		if (patrolLevel == 1) {
			EntityCultistAcolyte acolyte = new EntityCultistAcolyte(this.world);
			acolyte.setPatrolLeader(true);
			patrolMembers.add(acolyte);
		} else if (patrolLevel == 2) {
			EntityCultistAcolyte acolyte = new EntityCultistAcolyte(this.world);
			acolyte.setPatrolLeader(true);
			patrolMembers.add(acolyte);
			EntityCultistAcolyte acolyte2 = new EntityCultistAcolyte(this.world);
			patrolMembers.add(acolyte2);
		} else if (patrolLevel == 3) {
			EntityCultistDisciple disciple = new EntityCultistDisciple(this.world);
			disciple.setPatrolLeader(true);
			patrolMembers.add(disciple);
			EntityCultistAcolyte acolyte = new EntityCultistAcolyte(this.world);
			patrolMembers.add(acolyte);
		} else if (patrolLevel == 4) {
			EntityCultistDisciple disciple = new EntityCultistDisciple(this.world);
			disciple.setPatrolLeader(true);
			patrolMembers.add(disciple);
			EntityCultistAcolyte acolyte = new EntityCultistAcolyte(this.world);
			patrolMembers.add(acolyte);
			EntityCultistAcolyte acolyte2 = new EntityCultistAcolyte(this.world);
			patrolMembers.add(acolyte2);
		} else if (patrolLevel == 5) {
			EntityCultistDisciple disciple = new EntityCultistDisciple(this.world);
			disciple.setPatrolLeader(true);
			patrolMembers.add(disciple);
			EntityCultistDisciple disciple2 = new EntityCultistDisciple(this.world);
			patrolMembers.add(disciple2);
			EntityCultistAcolyte acolyte2 = new EntityCultistAcolyte(this.world);
			patrolMembers.add(acolyte2);
		} else if (patrolLevel == 6) {
			EntityCultistPriest priest = new EntityCultistPriest(this.world);
			priest.setShouldSpawnDragon(true);
			priest.setDragonAge(30);
			priest.setPatrolLeader(true);
			patrolMembers.add(priest);
			EntityCultistAcolyte acolyte = new EntityCultistAcolyte(this.world);
			patrolMembers.add(acolyte);
			EntityCultistAcolyte acolyte2 = new EntityCultistAcolyte(this.world);
			patrolMembers.add(acolyte2);
		} else if (patrolLevel == 7) {
			EntityCultistPriest priest = new EntityCultistPriest(this.world);
			priest.setShouldSpawnDragon(true);
			priest.setDragonAge(40);
			priest.setPatrolLeader(true);
			patrolMembers.add(priest);
			EntityCultistDisciple disciple = new EntityCultistDisciple(this.world);
			patrolMembers.add(disciple);
			EntityCultistAcolyte acolyte = new EntityCultistAcolyte(this.world);
			patrolMembers.add(acolyte);
		} else if (patrolLevel == 8) {
			EntityCultistPriest priest = new EntityCultistPriest(this.world);
			priest.setShouldSpawnDragon(true);
			priest.setDragonAge(50);
			priest.setPatrolLeader(true);
			patrolMembers.add(priest);
			EntityCultistDisciple disciple = new EntityCultistDisciple(this.world);
			patrolMembers.add(disciple);
			EntityCultistAcolyte acolyte = new EntityCultistAcolyte(this.world);
			patrolMembers.add(acolyte);
			EntityCultistAcolyte acolyte2 = new EntityCultistAcolyte(this.world);
			patrolMembers.add(acolyte2);
		}
		return patrolMembers;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		if (this.getOwnerId() != null) {
			compound.setUniqueId(OWNER_UUID_KEY, this.getOwnerId());
		}
		compound.setInteger(TICKS_EXISTED_KEY, this.ticksExisted);
		compound.setInteger(NEXT_PATROL_KEY, this.getNextPatrol());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setOwnerId(compound.getUniqueId(OWNER_UUID_KEY));
		this.ticksExisted = compound.getInteger(TICKS_EXISTED_KEY);
		this.setNextPatrol(compound.getInteger(NEXT_PATROL_KEY));
	}

}
