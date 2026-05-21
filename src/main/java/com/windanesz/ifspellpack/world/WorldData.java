package com.windanesz.ifspellpack.world;

import com.windanesz.ifspellpack.IFSpellPack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.util.*;

public class WorldData extends WorldSavedData {

    public static final String MAP_KEY = "EntityMap";
    public static final String UUID_KEY = "EntityUUID";
    public static final String POSX_KEY = "EntityPosX";
    public static final String POSY_KEY = "EntityPosY";
    public static final String POSZ_KEY = "EntityPosZ";
    public static final String NEXT_POSSIBLE_PATROL_TIME_KEY = "NextPossiblePatrolTime";

    private static final String IDENTIFIER = IFSpellPack.MODID + "_WorldData";
    private final Map<UUID, BlockPos> worldData = new HashMap<>();
    private World world;
    private long nextPossiblePatrolTime;

    public WorldData(String name) {
        super(name);
    }

    public WorldData(World world) {
        super(IDENTIFIER);
        this.world = world;
        this.markDirty();
    }

    public static WorldData get(World world) {
        MapStorage storage = world.getPerWorldStorage();
        WorldData instance = (WorldData)storage.getOrLoadData(WorldData.class, IDENTIFIER);

        if (instance == null) {
            instance = new WorldData(world);
            storage.setData(IDENTIFIER, instance);
        }
        instance.markDirty();
        return instance;
    }

    public void addEntity(UUID uuid, BlockPos pos) {
        worldData.put(uuid, pos);
        this.markDirty();
    }

    public void removeEntity(UUID uuid) {
        worldData.remove(uuid);
        this.markDirty();
    }

    public BlockPos getEntityPos(UUID uuid) {
        return worldData.get(uuid);
    }

    public long getNextPossiblePatrolTime() {
        return this.nextPossiblePatrolTime;
    }

    public void setNextPossiblePatrolTime(long nextPossiblePatrolTime) {
        this.nextPossiblePatrolTime = nextPossiblePatrolTime;
        this.markDirty();
    }

    public World getWorld() {
        return this.world;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList nbttaglist = nbt.getTagList(MAP_KEY, 10);
        this.worldData.clear();
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            UUID uuid = nbttagcompound.getUniqueId(UUID_KEY);
            BlockPos pos = new BlockPos(nbttagcompound.getInteger(POSX_KEY), nbttagcompound.getInteger(POSY_KEY), nbttagcompound.getInteger(POSZ_KEY));
            this.worldData.put(uuid, pos);
        }
        this.setNextPossiblePatrolTime(nbt.getLong(NEXT_POSSIBLE_PATROL_TIME_KEY));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList nbttaglist = new NBTTagList();
        for (Map.Entry<UUID, BlockPos> pair : worldData.entrySet()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setUniqueId(UUID_KEY, pair.getKey());
            nbttagcompound.setInteger(POSX_KEY, pair.getValue().getX());
            nbttagcompound.setInteger(POSY_KEY, pair.getValue().getY());
            nbttagcompound.setInteger(POSZ_KEY, pair.getValue().getZ());
            nbttaglist.appendTag(nbttagcompound);
        }
        compound.setTag(MAP_KEY, nbttaglist);
        compound.setLong(NEXT_POSSIBLE_PATROL_TIME_KEY, this.getNextPossiblePatrolTime());
        return compound;
    }
}
