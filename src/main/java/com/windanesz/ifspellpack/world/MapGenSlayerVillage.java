package com.windanesz.ifspellpack.world;

import com.windanesz.ifspellpack.IFSpellPack;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.*;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.List;
import java.util.Random;

public class MapGenSlayerVillage implements IWorldGenerator {

    private final int radius = 32;
    private final BiomeDictionary.Type[] generationBiomes = new BiomeDictionary.Type[]{BiomeDictionary.Type.JUNGLE, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.HILLS};

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (rand.nextInt(IFSpellPack.settings.slayerVillageChance) == 0) {
			//this doesnt actually randomly change the BlockPos
			BlockPos blockPos = new BlockPos(chunkX * 16 + rand.nextInt(16), 64, chunkZ * 16 + rand.nextInt(16));
			BiomeProvider biomeProvider = world.getBiomeProvider();
			Biome biome = biomeProvider.getBiome(blockPos, Biomes.DEFAULT);
			int biomeType = 0;
/*			int biomeType = -1;
			for (int i = 0; i < this.getGenerationBiomes().length; i++) {
				if (BiomeDictionary.hasType(biome, getGenerationBiomes()[i])) {
					biomeType = i;
					break;
				}
			}
			if (biomeType == -1) {
				return;
			}*/
/*			this.getStructureStart(world, chunkX, chunkZ, biomeType, rand, radius).generateStructure(world, rand, new StructureBoundingBox(blockPos.getX(), blockPos.getZ(), blockPos.getX(), blockPos.getZ()));*/
			this.getStructureStart(world, chunkX, chunkZ, biomeType, rand, radius).generateStructure(world, rand, new StructureBoundingBox(blockPos.getX() - this.radius, blockPos.getZ() - this.radius, blockPos.getX() + this.radius, blockPos.getZ() + this.radius));
		}
	}

	public BiomeDictionary.Type[] getGenerationBiomes() {
		return this.generationBiomes;
	}

	public StructureStart getStructureStart(World world, int chunkX, int chunkZ, int biomeType, Random random, int radius) {
		return new MapGenSlayerVillage.Start(world, random, chunkX, chunkZ, biomeType, radius);
	}

    public static class Start extends StructureStart {

		private boolean hasMoreThanTwoComponents;

		public Start(World world, Random rand, int x, int z, int biomeType, int radius) {
			super(x, z);
			StructureSlayerVillagePieces.Start start = new StructureSlayerVillagePieces.Start(biomeType, (x << 4) + 2, (z << 4) + 2, radius);
			StructureSlayerVillagePieces.Piece church = new StructureSlayerVillagePieces.Piece(this.getTemplate(new ResourceLocation[]{new ResourceLocation(IFSpellPack.MODID, "church"), new ResourceLocation(IFSpellPack.MODID, "church"), new ResourceLocation(IFSpellPack.MODID, "church")}, biomeType, world), 20, 5);
			start.pieces.add(church);
			//start.pieces.add(new StructureSlayerVillagePieces.Piece(church, 20, MathHelper.getInt(rand, 1, 2)));
			this.components.add(start);
			start.buildComponent(start, this.components, rand);
			List<StructureComponent> roads = start.pendingRoads;
			List<StructureSlayerVillagePieces> houses = start.pendingHouses;
			while (!roads.isEmpty() || !houses.isEmpty()) {
				if (roads.isEmpty()) {
					int i = rand.nextInt(houses.size());
					StructureSlayerVillagePieces house = houses.remove(i);
					house.buildComponent(start, this.components, rand);
				}
				else {
					int j = rand.nextInt(roads.size());
					StructureComponent road = roads.remove(j);
					road.buildComponent(start, this.components, rand);
				}
			}
			this.updateBoundingBox();
			int k = 0;
			for (StructureComponent structureComponent : this.components) {
				if (!(structureComponent instanceof StructureVillagePieces.Road)) {
					++k;
				}
			}
			this.hasMoreThanTwoComponents = k > 2;
		}

		public Template getTemplate(ResourceLocation[] resourceLocations, int biomeType, World world) {
			return world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), resourceLocations[biomeType]);
		}

		public boolean isSizeableStructure() {
			return this.hasMoreThanTwoComponents;
		}

		public void writeToNBT(NBTTagCompound tagCompound) {
			super.writeToNBT(tagCompound);
			tagCompound.setBoolean("Valid", this.hasMoreThanTwoComponents);
		}

		public void readFromNBT(NBTTagCompound tagCompound) {
			super.readFromNBT(tagCompound);
			this.hasMoreThanTwoComponents = tagCompound.getBoolean("Valid");
		}
	}
}
