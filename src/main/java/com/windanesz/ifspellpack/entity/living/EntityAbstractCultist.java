package com.windanesz.ifspellpack.entity.living;

import com.google.common.base.Predicate;
import com.windanesz.ifspellpack.entity.IPatroller;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.NBTExtras;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

public abstract class EntityAbstractCultist extends EntityMob /*implements IPatroller*/ {

	// 0 == Fire, 1 == Ice, 2 == Lightning
	public static final String ELEMENT_VARIANT_KEY = "ElementVariant";
	public static final String IS_PATROL_LEADER_KEY = "IsPatrolLeader";
	public static final String PATROL_PARTY_UUIDS_KEY = "PatrolPartyUUIDs";
	public static final String PATROL_LEVEL = "PatrolLevel";

	protected Predicate<Entity> targetSelector;
	private boolean isPatrolLeader;
	private List<UUID> patrolPartyUUIDs = new ArrayList<>();
	private int patrolLevel;

	//0 for fire, 1 for ice, 2 for lightning
	private static final DataParameter<Integer> ELEMENT_VARIANT = EntityDataManager.createKey(EntityAbstractCultist.class, DataSerializers.VARINT);

	public EntityAbstractCultist(World worldIn) {
		super(worldIn);
		this.setSize(0.6f, 1.8f);
		((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(ELEMENT_VARIANT, 0);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(4, new EntityAIRestrictOpenDoor(this));
		this.tasks.addTask(5, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(5, new EntityAIPatrol(this));
		this.tasks.addTask(6, new EntityAIMoveTowardsRestriction(this, this.getMovementSpeed()));
		this.tasks.addTask(7, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(7, new EntityAIWander(this, this.getMovementSpeed()));
		this.targetSelector = entity -> {
			if(entity != null && !entity.isInvisible() && AllyDesignationSystem.isValidTarget(this, entity)) {
				if(entity instanceof EntityPlayer || (entity instanceof ISummonedCreature || entity instanceof EntityAbstractCultist || Arrays.asList(Wizardry.settings.summonedCreatureTargetsWhitelist).contains(EntityList.getKey(entity.getClass()))) && !Arrays.asList(Wizardry.settings.summonedCreatureTargetsBlacklist).contains(EntityList.getKey(entity.getClass()))){
					return true;
				}
			}
			return false;
		};
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(0, new EntityAINearestAttackableTarget<>(this, EntityLivingBase.class, 0, false, true, this.targetSelector));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
	}

	public double getMovementSpeed() {
		return this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
	}

	public boolean isPatrolLeader() {
		return this.isPatrolLeader;
	}

	public void setPatrolLeader(boolean isPatrolLeader) {
		this.isPatrolLeader = isPatrolLeader;
	}

	public List<UUID> getPatrolPartyUUIDs() {
		return this.patrolPartyUUIDs;
	}

	public void setPatrolPartyUUIDs(List<UUID> patrolPartyUUIDs) {
		this.patrolPartyUUIDs = patrolPartyUUIDs;
	}

	public boolean isInPatrol() {
		return this.patrolLevel > 0;
	}

	public int getPatrolLevel() {
		return this.patrolLevel;
	}

	public void setPatrolLevel(int isInPatrol) {
		this.patrolLevel = isInPatrol;
	}

	public int getElementVariant() {
		return this.dataManager.get(ELEMENT_VARIANT);
	}

	public void setElementVariant(int variant) {
		this.dataManager.set(ELEMENT_VARIANT, variant);
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		this.setEquipmentBasedOnDifficulty(difficulty);
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Override
	public boolean isOnSameTeam(Entity entityIn) {
		return super.isOnSameTeam(entityIn) || entityIn instanceof EntityAbstractCultist;
	}

	@Override
	protected boolean canDespawn() {
		if (this.isInPatrol()) {
			return super.canDespawn();
		}
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound(){
		return WizardrySounds.ENTITY_EVIL_WIZARD_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source){
		return WizardrySounds.ENTITY_EVIL_WIZARD_HURT;
	}

	@Override
	protected SoundEvent getDeathSound(){
		return WizardrySounds.ENTITY_EVIL_WIZARD_DEATH;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger(ELEMENT_VARIANT_KEY, this.getElementVariant());
		compound.setBoolean(IS_PATROL_LEADER_KEY, this.isPatrolLeader());
		compound.setTag(PATROL_PARTY_UUIDS_KEY, NBTExtras.listToNBT(this.getPatrolPartyUUIDs(), NBTUtil::createUUIDTag));
		compound.setInteger(PATROL_LEVEL, this.getPatrolLevel());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setElementVariant(compound.getInteger(ELEMENT_VARIANT_KEY));
		this.setPatrolLeader(compound.getBoolean(IS_PATROL_LEADER_KEY));
		this.setPatrolPartyUUIDs(new ArrayList<>(NBTExtras.NBTToList(compound.getTagList(PATROL_PARTY_UUIDS_KEY, 10), NBTUtil::getUUIDFromTag)));
		this.setPatrolLevel(compound.getInteger(PATROL_LEVEL));
	}

	public static class EntityAIPatrol extends EntityAIBase {

		private final EntityAbstractCultist patroller;
		private long nextMovementTime;

		public EntityAIPatrol(EntityAbstractCultist patroller) {
			this.patroller = patroller;
			this.nextMovementTime = 0L;
			this.setMutexBits(1);
		}

		@Override
		public boolean shouldExecute() {
			if (this.patroller.isPatrolLeader()) {
				if (this.patroller.world.getTotalWorldTime() > this.nextMovementTime) {
					return true;
				}
			}
			return false;
		}

		//Move to a random position 16 blocks horizontally and 8 blocks vertically, following the patrol leader
		@Override
		public void startExecuting() {
			Vec3d pos = RandomPositionGenerator.findRandomTarget(this.patroller, 16, 8);
			if (pos != null) {
				for (EntityAbstractCultist cultist : this.getPatrolPartyMembers()) {
					cultist.getNavigator().tryMoveToXYZ(pos.x, pos.y, pos.z, cultist.getMovementSpeed());
				}
				this.nextMovementTime = this.patroller.world.getTotalWorldTime() + this.nextMoveTimeIncrease();
			}
		}

		//Gather all the party members who are loaded into the world
		public List<EntityAbstractCultist> getPatrolPartyMembers() {
			List<UUID> uuids = this.patroller.getPatrolPartyUUIDs();
			List<EntityAbstractCultist> patrolParty = new ArrayList<>();
			for (UUID uuid : uuids) {
				Entity entity = EntityUtils.getEntityByUUID(this.patroller.world, uuid);
				if (entity instanceof EntityAbstractCultist) {
					patrolParty.add((EntityAbstractCultist)entity);
				}
			}
			return patrolParty;
		}

		//Move every 10-20 seconds
		public int nextMoveTimeIncrease() {
			return 200 + this.patroller.world.rand.nextInt(200);
		}

	}

}
