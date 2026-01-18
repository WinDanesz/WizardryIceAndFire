package com.windanesz.ifspellpack.world;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.*;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StructureSlayerVillagePieces extends StructureComponentTemplate {

	protected Start startPiece;

	//Create origin that adds pieces and pieceWeights
	public StructureSlayerVillagePieces(Start start, Template template) {
		super();
		if (start != null) {
			this.startPiece = start;
		}
		if (template != null) {
			this.setup(template, template.getSize(), this.placeSettings);
		}
	}

	protected void writeStructureToNBT(NBTTagCompound tagCompound) {
		super.writeStructureToNBT(tagCompound);
	}

	protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(tagCompound, templateManager);
	}

	public boolean isGroundBlockReplaceable(IBlockState blockState, BlockPos blockPos) {
		return (blockState.getMaterial() == Material.GRASS || blockState.getMaterial() == Material.SAND) && (this.startPiece.world.isAirBlock(blockPos.up()) || this.startPiece.world.getBlockState(blockPos.up()).getMaterial().isReplaceable());
	}

	public int getYPlacement(BlockPos groundPos) {
		groundPos = this.startPiece.world.getTopSolidOrLiquidBlock(groundPos).down();
		if (groundPos.getY() < this.startPiece.world.getSeaLevel()) {
			groundPos = new BlockPos(groundPos.getX(), this.startPiece.world.getSeaLevel(), groundPos.getZ());
		}
		while (groundPos.getY() >= this.startPiece.world.getSeaLevel()) {
			IBlockState groundBlock = this.startPiece.world.getBlockState(groundPos);
			if (this.isGroundBlockReplaceable(groundBlock, groundPos) || groundBlock.getMaterial().isLiquid()) {
				break;
			}
			groundPos = groundPos.down();
		}
		return groundPos.getY();
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox structureBoundingBox) {
		if (this.template == null) {
			return true;
		}
		Rotation rotation = this.placeSettings.getRotation();
		return super.addComponentParts(world, rand, structureBoundingBox);
	}

	@Override
	protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox structureBoundingBox) {
	}

	@Nullable
    public StructureSlayerVillagePieces generateBuilding() {
		for (Piece piece : this.startPiece.buildingPieces) {
			//continue if it cant spawn more pieces of the type or if there are more different pieces to place, dont break like in StructureVillagePieces
			if (!piece.canSpawnMoreVillagePieces() || piece == this.startPiece.lastPlaced && this.startPiece.buildingPieces.size() > 1) {
				continue;
			}
			++piece.villagePiecesSpawned;
			this.startPiece.lastPlaced = piece;
			if (!piece.canSpawnMoreVillagePieces()) {
				this.startPiece.buildingPieces.remove(piece);
			}
			return new StructureSlayerVillagePieces(this.startPiece, piece.template);
		}
        return null;
    }

    //Adds house to start generation
	@Nullable
    public StructureSlayerVillagePieces generateAndAddBuilding(List<StructureComponent> structureComponents, Random rand, int x, int z, Rotation rotation) {
        if (Math.abs(x - this.startPiece.getBoundingBox().minX) <= this.startPiece.radius && Math.abs(z - this.startPiece.getBoundingBox().minZ) <= this.startPiece.radius) {
            StructureSlayerVillagePieces building = this.generateBuilding();
            if (building != null) {
				int y = building.getYPlacement(new BlockPos(x, 0, z));
				if (y < 0) {
					return null;
				}
				building.templatePosition = new BlockPos(x, y, z);
            	building.placeSettings.setRotation(rotation);
				building.templatePosition = building.template.getZeroPositionWithTransform(building.templatePosition, building.placeSettings.getMirror(), building.placeSettings.getRotation());
				building.getBoundingBox().offset(building.templatePosition.getX(), building.templatePosition.getY(), building.templatePosition.getZ());
				BlockPos pos = building.templatePosition;
				StructureBoundingBox box = building.getBoundingBox();
/*            	for (StructureComponent structureComponentExisting : structureComponents) {
            		if (structureComponentExisting.getBoundingBox().intersectsWith(structureComponent.boundingBox)) {
            			return null;
					}
				}*/
                structureComponents.add(building);
                this.startPiece.pendingHouses.add(building);
                return building;
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

	//Generates a boundingbox 7 - 35 blocks long as it doesnt intersect with an existing structure
	@Nullable
	public StructureSlayerVillagePieces.Road generateRoad(List<StructureComponent> structureComponents, Random rand, int x, int z, Rotation rotation) {
		for (int i = 7 * MathHelper.getInt(rand, 3, 5); i >= 7; i -= 7) {
			StructureSlayerVillagePieces.Road road = new StructureSlayerVillagePieces.Road(this.startPiece, i);
			road.placeSettings.setRotation(rotation);
			int y = road.getYPlacement(new BlockPos(x, 0, z));
			if (y < 0) {
				continue;
			}
			road.templatePosition = new BlockPos(x, y, z);
			BlockPos size = new BlockPos(3, 3, i);
			switch (road.placeSettings.getRotation())
			{
				case COUNTERCLOCKWISE_90:
				case CLOCKWISE_90:
					size = new BlockPos(size.getZ(), size.getY(), size.getX());
			}
			road.boundingBox = new StructureBoundingBox(0, 0, 0, size.getX() - 1, size.getY() - 1, size.getZ() - 1);
			switch (road.placeSettings.getRotation()) {
				case NONE:
				default:
					break;
				case CLOCKWISE_90:
					road.getBoundingBox().offset(-size.getX(), 0, 0);
					break;
				case COUNTERCLOCKWISE_90:
					road.getBoundingBox().offset(0, 0, -size.getZ());
					break;
				case CLOCKWISE_180:
					road.getBoundingBox().offset(-size.getX(), 0, -size.getZ());
			}
			road.getBoundingBox().offset(road.templatePosition.getX(), road.templatePosition.getY(), road.templatePosition.getZ());
			//Ignore intellij saying it cant be null
			//if (StructureComponent.findIntersecting(structureComponents, road.getBoundingBox()) == null) {
				return road;
			//}
		}
		return null;
	}

    //Adds road to start generation
	@Nullable
    public StructureComponent generateAndAddRoad(List<StructureComponent> structureComponents, Random rand, int x, int z, Rotation rotation) {
        if (Math.abs(x - this.startPiece.getBoundingBox().minX) <= this.startPiece.radius && Math.abs(z - this.startPiece.getBoundingBox().minZ) <= this.startPiece.radius) {
			StructureSlayerVillagePieces.Road road = this.generateRoad(structureComponents, rand, x, z, rotation);
			if (road != null) {
				structureComponents.add(road);
				this.startPiece.pendingRoads.add(road);
			}
			return road;
        }
        else {
            return null;
        }
    }

    public static class Road extends StructureSlayerVillagePieces {

		private int length;

		public Road(Start start, int length) {
			super(start, null);
			this.length = length;
		}

/*		@Override
		public void buildComponent(StructureComponent structureComponent, List<StructureComponent> structureComponents, Random rand) {
			boolean flag = false;
			//Adds houses every 2 - 7 + the house's width blocks
			for (int i = rand.nextInt(5); i < this.length - 8; i += 2 + rand.nextInt(5)) {
				StructureComponent house1 = this.getNextComponentNN(structureComponents, rand, i);
				if (house1 != null) {
					i += Math.max(house1.getBoundingBox().getXSize(), house1.getBoundingBox().getZSize());
					flag = true;
				}
			}
			//Adds houses every 2 - 7 + the house's width blocks
			for (int j = rand.nextInt(5); j < this.length - 8; j += 2 + rand.nextInt(5)) {
				StructureComponent house2 = this.getNextComponentPP(structureComponents, rand, j);
				if (house2 != null) {
					j += Math.max(house2.getBoundingBox().getXSize(), house2.getBoundingBox().getZSize());
					flag = true;
				}
			}
			//Generates roads to the west or north of the end randomly
			if (flag && rand.nextInt(3) > 0 && enumfacing != null) {
				switch (enumfacing) {
					case NORTH:
					default:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST);
						break;
					case SOUTH:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.WEST);
						break;
					case WEST:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH);
						break;
					case EAST:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH);
				}
			}
			//Generates roads to the east or south of the end randomly
			if (flag && rand.nextInt(3) > 0 && enumfacing != null) {
				switch (enumfacing) {
					case NORTH:
					default:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST);
						break;
					case SOUTH:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.EAST);
						break;
					case WEST:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH);
						break;
					case EAST:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH);
				}
			}
		}*/

		//Creates the road blocks in the world
		@Override
		public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
			IBlockState roadBlock = Blocks.STONEBRICK.getDefaultState();
			IBlockState dockBlock = this.startPiece.getDockBlock();
			for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i) {
				for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j) {
					BlockPos blockpos = new BlockPos(i, this.boundingBox.minY, j);
					int y = this.getYPlacement(blockpos);
					blockpos = new BlockPos(blockpos.getX(), y, blockpos.getZ());
					IBlockState groundBlock = world.getBlockState(blockpos);
					if (isGroundBlockReplaceable(groundBlock, blockpos)) {
						world.setBlockState(blockpos, roadBlock, 2);
					}
					else if (groundBlock.getMaterial().isLiquid()) {
						world.setBlockState(blockpos, dockBlock, 2);
					}
				}
			}
			return true;
		}

/*		@Nullable
		protected StructureComponent getNextComponentNN(List<StructureComponent> structureComponents, Random rand, int shift) {
			EnumFacing enumfacing = this.getCoordBaseMode();
			if (enumfacing != null) {
				switch (enumfacing) {
					case NORTH:
					default:
						return this.generateAndAddBuilding(structureComponents, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + shift);
					case WEST:
					case EAST:
						return this.generateAndAddBuilding(structureComponents, rand, this.boundingBox.minX + shift, this.boundingBox.minY, this.boundingBox.minZ - 1);
				}
			} else {
				return null;
			}
		}

		@Nullable
		protected StructureComponent getNextComponentPP(List<StructureComponent> structureComponents, Random rand, int shift) {
			EnumFacing enumfacing = this.getCoordBaseMode();
			if (enumfacing != null) {
				switch (enumfacing) {
					case NORTH:
					default:
						return this.generateAndAddBuilding(structureComponents, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + shift);
					case WEST:
					case EAST:
						return this.generateAndAddBuilding(structureComponents, rand, this.boundingBox.minX + shift, this.boundingBox.minY, this.boundingBox.maxZ + 1);
				}
			} else {
				return null;
			}
		}*/

		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound) {
			tagCompound.setInteger("Length", this.length);
		}

		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
			this.length = tagCompound.getInteger("Length");
		}

	}

    public static class Piece {

		public Template template;
		public int villagePiecesSpawned;
		public int villagePiecesNumber;

		public Piece(Template template, int number) {
			this.template = template;
			this.villagePiecesNumber = number;
		}

		public boolean canSpawnMoreVillagePieces() {
			return this.villagePiecesNumber == 0 || this.villagePiecesSpawned < this.villagePiecesNumber;
		}
	}

	public static class Start extends StructureSlayerVillagePieces {

		public World world;
		public Piece lastPlaced;
		public int radius;
		private final IBlockState dockBlock;
		public List<Piece> buildingPieces = new ArrayList<>();
 		public List<StructureSlayerVillagePieces> pendingHouses = new ArrayList<>();
		public List<StructureComponent> pendingRoads = new ArrayList<>();

		public Start(World world, int x, int z, int radius, IBlockState dockBlock) {
			super(null, null);
			this.world = world;
			this.boundingBox = new StructureBoundingBox(x, z, x, z);
			this.radius = radius;
			this.dockBlock = dockBlock;
			this.startPiece = this;
		}

		public IBlockState getDockBlock() {
			return this.dockBlock;
		}

		public Start setRotation(Rotation rotation) {
			this.placeSettings.setRotation(rotation);
			return this;
		}

		@Override
		public void buildComponent(StructureComponent start, List<StructureComponent> structureComponents, Random rand) {
			this.generateAndAddBuilding(structureComponents, rand, this.boundingBox.maxX, this.boundingBox.minZ, Rotation.NONE);
			this.generateAndAddBuilding(structureComponents, rand, this.boundingBox.maxX, this.boundingBox.minZ, Rotation.CLOCKWISE_180);
			this.generateAndAddRoad(structureComponents, rand, this.boundingBox.minX, this.boundingBox.maxZ, Rotation.NONE);
			this.generateAndAddRoad(structureComponents, rand, this.boundingBox.maxX, this.boundingBox.maxZ, Rotation.CLOCKWISE_90);
			this.generateAndAddRoad(structureComponents, rand, this.boundingBox.maxX, this.boundingBox.maxZ, Rotation.CLOCKWISE_180);
			this.generateAndAddRoad(structureComponents, rand, this.boundingBox.maxX, this.boundingBox.maxZ, Rotation.COUNTERCLOCKWISE_90);
		}

	}

/*	public static class Road extends StructureComponent {

		private int length;
		private StructureSlayerVillagePieces.Start start;

		public Road(StructureSlayerVillagePieces.Start start, int type, StructureBoundingBox structureBoundingBox, EnumFacing facing) {
			super(type);
			this.start = start;
			this.setCoordBaseMode(facing);
			this.boundingBox = structureBoundingBox;
			this.length = Math.max(structureBoundingBox.getXSize(), structureBoundingBox.getZSize());
		}

		protected void writeStructureToNBT(NBTTagCompound tagCompound) {
			tagCompound.setInteger("Length", this.length);
		}

		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
			this.length = tagCompound.getInteger("Length");
		}

		//This allows the placement of the road in the ground
		public int getYPlacement(BlockPos blockPos, World world, StructureBoundingBox structureBoundingBox) {
			int y = -1;
			if (structureBoundingBox.isVecInside(blockPos)) {
				blockPos = world.getTopSolidOrLiquidBlock(blockPos).down();
				if (blockPos.getY() < world.getSeaLevel()) {
					blockPos = new BlockPos(blockPos.getX(), world.getSeaLevel(), blockPos.getZ());
				}
				while (blockPos.getY() >= world.getSeaLevel()) {
					IBlockState groundBlock = world.getBlockState(blockPos);
					if (isGroundBlockReplaceable(groundBlock, blockPos, world) || groundBlock.getMaterial().isLiquid()) {
						break;
					}
					blockPos = blockPos.down();
				}
				y = blockPos.getY();
			}
			return y;
		}

		public void buildComponent(StructureComponent structureComponent, List<StructureComponent> structureComponents, Random rand) {
			boolean flag = false;
			//Adds houses every 2 - 7 + the house's width blocks
			for (int i = rand.nextInt(5); i < this.length - 8; i += 2 + rand.nextInt(5)) {
				StructureComponent house1 = this.getNextComponentNN(structureComponents, rand, i);
				if (house1 != null) {
					i += Math.max(house1.getBoundingBox().getXSize(), house1.getBoundingBox().getZSize());
					flag = true;
				}
			}
			//Adds houses every 2 - 7 + the house's width blocks
			for (int j = rand.nextInt(5); j < this.length - 8; j += 2 + rand.nextInt(5)) {
				StructureComponent house2 = this.getNextComponentPP(structureComponents, rand, j);
				if (house2 != null) {
					j += Math.max(house2.getBoundingBox().getXSize(), house2.getBoundingBox().getZSize());
					flag = true;
				}
			}
			EnumFacing enumfacing = this.getCoordBaseMode();
			//Generates roads to the west or north of the end randomly
			if (flag && rand.nextInt(3) > 0 && enumfacing != null) {
				switch (enumfacing) {
					case NORTH:
					default:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST);
						break;
					case SOUTH:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.WEST);
						break;
					case WEST:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH);
						break;
					case EAST:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH);
				}
			}
			//Generates roads to the east or south of the end randomly
			if (flag && rand.nextInt(3) > 0 && enumfacing != null) {
				switch (enumfacing) {
					case NORTH:
					default:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST);
						break;
					case SOUTH:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.EAST);
						break;
					case WEST:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH);
						break;
					case EAST:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH);
				}
			}
		}

		//Generates a boundingbox 7 - 35 blocks long as it doesnt intersect with an existing structure
		@Nullable
		public static StructureBoundingBox findPieceBox(List<StructureComponent> structureComponents, Random rand, int x, int y, int z, EnumFacing facing) {
			for (int i = 7 * MathHelper.getInt(rand, 3, 5); i >= 7; i -= 7) {
				StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 3, 3, i, facing);
				//Ignore intellij saying it cant be null
				if (StructureComponent.findIntersecting(structureComponents, structureboundingbox) == null) {
					return structureboundingbox;
				}
			}
			return null;
		}

		//Creates the road blocks in the world
		public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
			IBlockState roadBlock = Blocks.STONEBRICK.getDefaultState();
			IBlockState dockBlock = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK);
			for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i) {
				for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j) {
					BlockPos blockpos = new BlockPos(i, this.boundingBox.minY, j);
					int y = this.getYPlacement(blockpos, world, structureBoundingBox);
					if (structureBoundingBox.isVecInside(blockpos)) {
						blockpos = new BlockPos(blockpos.getX(), y, blockpos.getZ());
						IBlockState groundBlock = world.getBlockState(blockpos);
						if (isGroundBlockReplaceable(groundBlock, blockpos, world)) {
							world.setBlockState(blockpos, roadBlock, 2);
						}
						else if (groundBlock.getMaterial().isLiquid()) {
							world.setBlockState(blockpos, dockBlock, 2);
						}
					}
				}
			}
			return true;
		}

		@Nullable
		protected StructureComponent getNextComponentNN(List<StructureComponent> structureComponents, Random rand, int shift) {
			EnumFacing enumfacing = this.getCoordBaseMode();
			if (enumfacing != null) {
				switch (enumfacing) {
					case NORTH:
					default:
						return start.generateAndAddHouse(structureComponents, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + shift, EnumFacing.WEST);
					case WEST:
					case EAST:
						return start.generateAndAddHouse(structureComponents, rand, this.boundingBox.minX + shift, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH);
				}
			} else {
				return null;
			}
		}

		@Nullable
		protected StructureComponent getNextComponentPP(List<StructureComponent> structureComponents, Random rand, int shift) {
			EnumFacing enumfacing = this.getCoordBaseMode();
			if (enumfacing != null) {
				switch (enumfacing) {
					case NORTH:
					default:
						return start.generateAndAddHouse(structureComponents, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + shift, EnumFacing.EAST);
					case WEST:
					case EAST:
						return start.generateAndAddHouse(structureComponents, rand, this.boundingBox.minX + shift, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH);
				}
			} else {
				return null;
			}
		}
	}*/

}
