package com.windanesz.ifspellpack.world;

import com.windanesz.ifspellpack.IFSpellPack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.util.*;

public class EntityPosData extends WorldSavedData {

    public static final String MAP_KEY = "EntityMap";
    public static final String UUID_KEY = "EntityUUID";
    public static final String POSX_KEY = "EntityPosX";
    public static final String POSY_KEY = "EntityPosY";
    public static final String POSZ_KEY = "EntityPosZ";

    private static final String IDENTIFIER = IFSpellPack.MODID + "_EntityPosData";
    private final Map<UUID, BlockPos> entityPosData = new HashMap<>();
    private World world;

    public EntityPosData(String name) {
        super(name);
    }

    public EntityPosData(World world) {
        super(IDENTIFIER);
        this.world = world;
        this.markDirty();
    }

    public static EntityPosData get(World world) {
        MapStorage storage = world.getPerWorldStorage();
        EntityPosData instance = (EntityPosData)storage.getOrLoadData(EntityPosData.class, IDENTIFIER);

        if (instance == null) {
            instance = new EntityPosData(world);
            storage.setData(IDENTIFIER, instance);
        }
        instance.markDirty();
        return instance;
    }

    public void addEntity(UUID uuid, BlockPos pos) {
        entityPosData.put(uuid, pos);
        this.markDirty();
    }

    public void removeEntity(UUID uuid) {
        entityPosData.remove(uuid);
        this.markDirty();
    }

    public BlockPos getEntityPos(UUID uuid) {
        return entityPosData.get(uuid);
    }

    public World getWorld() {
        return world;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList nbttaglist = nbt.getTagList(MAP_KEY, 10);
        this.entityPosData.clear();
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            UUID uuid = nbttagcompound.getUniqueId(UUID_KEY);
            BlockPos pos = new BlockPos(nbttagcompound.getInteger(POSX_KEY), nbttagcompound.getInteger(POSY_KEY), nbttagcompound.getInteger(POSZ_KEY));
            this.entityPosData.put(uuid, pos);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList nbttaglist = new NBTTagList();
        for (Map.Entry<UUID, BlockPos> pair : entityPosData.entrySet()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setUniqueId(UUID_KEY, pair.getKey());
            nbttagcompound.setInteger(POSX_KEY, pair.getValue().getX());
            nbttagcompound.setInteger(POSY_KEY, pair.getValue().getY());
            nbttagcompound.setInteger(POSZ_KEY, pair.getValue().getZ());
            nbttaglist.appendTag(nbttagcompound);
        }
        compound.setTag(MAP_KEY, nbttaglist);
        return compound;
    }
}
