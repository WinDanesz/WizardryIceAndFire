package com.windanesz.ifspellpack.world;

import com.windanesz.ifspellpack.IFSpellPack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.util.*;

public class BeastPosData extends WorldSavedData {

    private static final String IDENTIFIER = IFSpellPack.MODID + "_BeastPosData";
    private final Map<UUID, BlockPos> beastPosData = new HashMap<>();
    private World world;

    public BeastPosData(String name) {
        super(name);
    }

    public BeastPosData(World world) {
        super(IDENTIFIER);
        this.world = world;
        this.markDirty();
    }

    public static BeastPosData get(World world) {
        MapStorage storage = world.getPerWorldStorage();
        BeastPosData instance = (BeastPosData)storage.getOrLoadData(BeastPosData.class, IDENTIFIER);

        if (instance == null) {
            instance = new BeastPosData(world);
            storage.setData(IDENTIFIER, instance);
        }
        instance.markDirty();
        return instance;
    }

    public void addBeast(UUID uuid, BlockPos pos) {
        beastPosData.put(uuid, pos);
        this.markDirty();
    }

    public void removeBeast(UUID uuid) {
        beastPosData.remove(uuid);
        this.markDirty();
    }

    public BlockPos getBeastPos(UUID uuid) {
        return beastPosData.get(uuid);
    }

    public World getWorld() {
        return world;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList nbttaglist = nbt.getTagList("BeastMap", 10);
        this.beastPosData.clear();
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            UUID uuid = nbttagcompound.getUniqueId("BeastUUID");
            BlockPos pos = new BlockPos(nbttagcompound.getInteger("BeastPosX"), nbttagcompound.getInteger("BeastPosY"), nbttagcompound.getInteger("BeastPosZ"));
            this.beastPosData.put(uuid, pos);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList nbttaglist = new NBTTagList();
        for (Map.Entry<UUID, BlockPos> pair : beastPosData.entrySet()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setUniqueId("BeastUUID", pair.getKey());
            nbttagcompound.setInteger("BeastPosX", pair.getValue().getX());
            nbttagcompound.setInteger("BeastPosY", pair.getValue().getY());
            nbttagcompound.setInteger("BeastPosZ", pair.getValue().getZ());
            nbttaglist.appendTag(nbttagcompound);
        }
        compound.setTag("BeastMap", nbttaglist);
        return compound;
    }
}
