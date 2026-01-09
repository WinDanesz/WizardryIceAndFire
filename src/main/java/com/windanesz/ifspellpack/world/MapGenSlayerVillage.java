package com.windanesz.ifspellpack.world;

import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.Wizardry;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.*;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MapGenSlayerVillage implements IWorldGenerator {

    private final int radius;
    private final BiomeDictionary.Type[] generationBiomes = new BiomeDictionary.Type[]{BiomeDictionary.Type.JUNGLE, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.HILLS};

    public MapGenSlayerVillage(int radius) {
    	this.radius = radius;
	}

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (rand.nextInt(IFSpellPack.settings.slayerVillageChance) == 0) {
			int posX = chunkX * 16 + rand.nextInt(16);
			int posZ = chunkZ * 16 + rand.nextInt(16);
			BlockPos blockPos = new BlockPos(posX, 64, posZ);
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
			this.getStructureStart(world, chunkX, chunkZ, posX, posZ, biomeType, rand, radius).generateStructure(world, rand, new StructureBoundingBox(blockPos.getX() - this.radius, blockPos.getZ() - this.radius, blockPos.getX() + this.radius, blockPos.getZ() + this.radius));
		}
	}

	public BiomeDictionary.Type[] getGenerationBiomes() {
		return this.generationBiomes;
	}

	public StructureStart getStructureStart(World world, int chunkX, int chunkZ, int posX, int posZ, int biomeType, Random random, int radius) {
		return new MapGenSlayerVillage.Start(world, random, chunkX, chunkZ, posX, posZ, biomeType, radius);
	}

    public static class Start extends StructureStart {

		public Start(World world, Random rand, int chunkX, int chunkZ, int posX, int posZ, int biomeType, int radius) {
			super(chunkX, chunkZ);
			StructureSlayerVillagePieces.Start start = new StructureSlayerVillagePieces.Start(biomeType, posX, posZ, radius, Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK));
			start.setCoordBaseMode(EnumFacing.values()[rand.nextInt(EnumFacing.values().length)]);
			StructureSlayerVillagePieces.Piece church = new StructureSlayerVillagePieces.Piece(this.getTemplate(new ResourceLocation[]{new ResourceLocation(IFSpellPack.MODID, "church"), new ResourceLocation(IFSpellPack.MODID, "church"), new ResourceLocation(IFSpellPack.MODID, "church")}, biomeType, world), 3);
			StructureSlayerVillagePieces.Piece tower = new StructureSlayerVillagePieces.Piece(this.getTemplate(new ResourceLocation[]{new ResourceLocation(Wizardry.MODID, "wizard_tower_0"), new ResourceLocation(Wizardry.MODID, "wizard_tower_1"), new ResourceLocation(Wizardry.MODID, "wizard_tower_2")}, biomeType, world), 3);
			start.buildingPieces.addAll(Arrays.asList(church, tower));

			StructureSlayerVillagePieces.Piece road = new StructureSlayerVillagePieces.Piece(this.getTemplate(new ResourceLocation[]{new ResourceLocation(IFSpellPack.MODID, "road"), new ResourceLocation(IFSpellPack.MODID, "road"), new ResourceLocation(IFSpellPack.MODID, "road")}, biomeType, world), 0);
			start.roadPiece = road;

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
					StructureComponent road2 = roads.remove(j);
					road2.buildComponent(start, this.components, rand);
				}
			}
			this.updateBoundingBox();
		}

		public Template getTemplate(ResourceLocation[] resourceLocations, int biomeType, World world) {
			return world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), resourceLocations[biomeType]);
		}

		public void writeToNBT(NBTTagCompound tagCompound) {
			super.writeToNBT(tagCompound);
		}

		public void readFromNBT(NBTTagCompound tagCompound) {
			super.readFromNBT(tagCompound);
		}
	}
}

