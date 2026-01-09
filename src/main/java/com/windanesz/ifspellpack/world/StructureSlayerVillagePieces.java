package com.windanesz.ifspellpack.world;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.*;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StructureSlayerVillagePieces extends StructureComponentTemplate {

	protected int averageGroundLvl = -1;
	protected Start startPiece;
	private ITemplateProcessor templateProcessor;

	//Create origin that adds pieces and pieceWeights
	public StructureSlayerVillagePieces(Start start, Template template) {
		super();
		if (start != null) {
			this.componentType = start.componentType;
			this.startPiece = start;
		}
		if (template != null) {
			this.setup(template, template.getSize(), this.placeSettings);
		}
	}

	protected void writeStructureToNBT(NBTTagCompound tagCompound) {
		super.writeStructureToNBT(tagCompound);
		tagCompound.setInteger("HPos", this.averageGroundLvl);
		tagCompound.setByte("Type", (byte)this.componentType);
	}

	protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(tagCompound, templateManager);
		this.averageGroundLvl = tagCompound.getInteger("HPos");
		this.componentType = tagCompound.getByte("Type");
	}

	public void setTemplateProcessor(ITemplateProcessor templateProcessor) {
		this.templateProcessor = templateProcessor;
	}

	public ITemplateProcessor getTemplateProcessor() {
		return this.templateProcessor;
	}

	public boolean isGroundBlockReplaceable(IBlockState blockState, BlockPos blockPos, World world) {
		return (blockState.getMaterial() == Material.GRASS || blockState.getMaterial() == Material.SAND) && (world.isAirBlock(blockPos.up()) || blockState.getMaterial().isReplaceable());
	}

	public int getYPlacement(BlockPos groundPos, World world) {
		groundPos = world.getTopSolidOrLiquidBlock(groundPos).down();
		if (groundPos.getY() < world.getSeaLevel()) {
			groundPos = new BlockPos(groundPos.getX(), world.getSeaLevel(), groundPos.getZ());
		}
		while (groundPos.getY() >= world.getSeaLevel()) {
			IBlockState groundBlock = world.getBlockState(groundPos);
			if (this.isGroundBlockReplaceable(groundBlock, groundPos, world) || groundBlock.getMaterial().isLiquid()) {
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
		int y = this.getYPlacement(this.templatePosition, world);
		if (y < 0) {
			return true;
		}
		this.templatePosition = new BlockPos(this.templatePosition.getX(), y, this.templatePosition.getZ());
		this.placeSettings.setBoundingBox(structureBoundingBox);
		this.template.addBlocksToWorld(world, this.templatePosition, this.templateProcessor, this.placeSettings, 18);
		Map<BlockPos, String> map = this.template.getDataBlocks(this.templatePosition, this.placeSettings);
		for (Map.Entry<BlockPos, String> entry : map.entrySet()) {
			String s = entry.getValue();
			this.handleDataMarker(s, entry.getKey(), world, rand, structureBoundingBox);
		}
		return true;
	}

	@Override
	protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox structureBoundingBox) {
	}

    //Creates a structure piece accounting for piece weight
	@Nullable
    public StructureSlayerVillagePieces generateBuilding() {
		for (Piece piece : this.startPiece.buildingPieces) {
			//break if it cant spawn more pieces of the type or if there are more different pieces to place
			if (!piece.canSpawnMoreVillagePieces() || piece == this.startPiece.lastPlaced && this.startPiece.buildingPieces.size() > 1) {
				break;
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
    public StructureSlayerVillagePieces generateAndAddBuilding(List<StructureComponent> structureComponents, Random rand, int x, int y, int z) {
        if (Math.abs(x - this.startPiece.getBoundingBox().minX) <= this.startPiece.radius && Math.abs(z - this.startPiece.getBoundingBox().minZ) <= this.startPiece.radius) {
            StructureSlayerVillagePieces structureComponent = this.generateBuilding();
            if (structureComponent != null) {
            	//.setup(structurecomponent.template, new BlockPos(structureMinX, structureMinY, structureMinZ), structurecomponent.placeSettings.getRotation().rotate(EnumFacing.EAST));
            	//structureComponent.templatePosition = new BlockPos(x, y, z);

            	int randInt = rand.nextInt(Rotation.values().length);
            	structureComponent.placeSettings.setRotation(structureComponent.placeSettings.getRotation().add(Rotation.values()[randInt]));
				System.out.println(structureComponent.templatePosition.toString() + " " + randInt + " " + structureComponent.placeSettings.getRotation().name());
            	structureComponent.template.getZeroPositionWithTransform(structureComponent.templatePosition, structureComponent.placeSettings.getMirror(), structureComponent.placeSettings.getRotation());

            	//Need to offset bounding box to the position for StructureStart$generateStructure to see that the bounding boxes have intersected and call addComponentParts
            	//structureComponent.getBoundingBox().offset(x, y, z);
            	structureComponent.setup(structureComponent.template, new BlockPos(x, y, z), structureComponent.placeSettings);
            	for (StructureComponent structureComponentExisting : structureComponents) {
            		if (structureComponentExisting.getBoundingBox().intersectsWith(structureComponent.boundingBox)) {
            			return null;
					}
				}
                structureComponents.add(structureComponent);
                this.startPiece.pendingHouses.add(structureComponent);
                return structureComponent;
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public StructureSlayerVillagePieces generateRoad() {
		return new StructureSlayerVillagePieces.Road(this.startPiece, this.startPiece.roadPiece.template);
	}

    //Adds road to start generation
	@Nullable
    public StructureComponent generateAndAddRoad(List<StructureComponent> structureComponents, Random rand, int x, int y, int z) {
        if (Math.abs(x - this.startPiece.getBoundingBox().minX) <= this.startPiece.radius && Math.abs(z - this.startPiece.getBoundingBox().minZ) <= this.startPiece.radius) {
			StructureSlayerVillagePieces structureComponent = generateRoad();
			if (structureComponent != null) {
/*				structureComponent.templatePosition = new BlockPos(x, y, z);
				structureComponent.template.getZeroPositionWithTransform(structureComponent.templatePosition, structureComponent.placeSettings.getMirror(), structureComponent.placeSettings.getRotation());
				structureComponent.getBoundingBox().offset(x, y, z);*/
				structureComponent.setup(structureComponent.template, new BlockPos(x, y, z), structureComponent.placeSettings);
				structureComponents.add(structureComponent);
				this.startPiece.pendingRoads.add(structureComponent);
			}
			return structureComponent;
        }
        else {
            return null;
        }
    }

    public static class Road extends StructureSlayerVillagePieces {

		public Road(Start start, Template template) {
			super(start, template);
			this.setTemplateProcessor(new RoadTemplateProcessor(this));
		}

		@Override
		public boolean addComponentParts(World world, Random rand, StructureBoundingBox structureBoundingBox) {
			int i = 0;
			return super.addComponentParts(world, rand, structureBoundingBox);
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

	public static class RoadTemplateProcessor implements ITemplateProcessor {

		private StructureSlayerVillagePieces.Road road;

		public RoadTemplateProcessor(StructureSlayerVillagePieces.Road road) {
			super();
			this.road = road;
		}

		@Nullable
		@Override
		public Template.BlockInfo processBlock(World world, BlockPos pos, Template.BlockInfo blockInfo) {
			int y = road.getYPlacement(pos, world);
			BlockPos blockPos = new BlockPos(pos.getX(), y, pos.getZ());
			IBlockState groundBlock = world.getBlockState(blockPos);
			if (road.isGroundBlockReplaceable(groundBlock, blockPos, world)) {
				return new Template.BlockInfo(blockPos, blockInfo.blockState, null);
			}
			else if (groundBlock.getMaterial().isLiquid()) {
				return new Template.BlockInfo(blockPos, road.startPiece.getDockBlock(), null);
			}
			return null;
		}
	}

	public static class Start extends StructureSlayerVillagePieces {

		public Piece lastPlaced;
		public int radius;
		private final IBlockState dockBlock;
		public List<Piece> buildingPieces = new ArrayList<>();
		public Piece roadPiece;
 		public List<StructureSlayerVillagePieces> pendingHouses = new ArrayList<>();
		public List<StructureComponent> pendingRoads = new ArrayList<>();

		public Start(int type, int x, int z, int radius, IBlockState dockBlock) {
			super(null, null);
			this.componentType = type;
			this.boundingBox = new StructureBoundingBox(x, z, x, z);
			this.radius = radius;
			this.dockBlock = dockBlock;
			this.startPiece = this;
		}

		public IBlockState getDockBlock() {
			return this.dockBlock;
		}

		@Override
		public void buildComponent(StructureComponent start, List<StructureComponent> structureComponents, Random rand) {
			this.generateAndAddBuilding(structureComponents, rand, this.boundingBox.minX - 1, this.boundingBox.maxY, this.boundingBox.minZ + 1);
			this.generateAndAddRoad(structureComponents, rand, this.boundingBox.minX - 1, this.boundingBox.maxY, this.boundingBox.minZ + 1);
			this.generateAndAddRoad(structureComponents, rand, this.boundingBox.maxX + 5, this.boundingBox.maxY, this.boundingBox.minZ + 1);
			this.generateAndAddRoad(structureComponents, rand, this.boundingBox.minX + 1, this.boundingBox.maxY, this.boundingBox.minZ - 1);
			this.generateAndAddRoad(structureComponents, rand, this.boundingBox.minX + 1, this.boundingBox.maxY, this.boundingBox.maxZ + 5);
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
