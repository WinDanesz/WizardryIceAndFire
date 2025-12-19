package com.windanesz.ifspellpack.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
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

	protected int averageGroundLvl = -1;
	private int villagersSpawned;
	protected Start startPiece;

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
		tagCompound.setInteger("VCount", this.villagersSpawned);
		tagCompound.setByte("Type", (byte)this.componentType);
	}

	protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(tagCompound, templateManager);
		this.averageGroundLvl = tagCompound.getInteger("HPos");
		this.villagersSpawned = tagCompound.getInteger("VCount");
		this.componentType = tagCompound.getByte("Type");
	}

	public Template getTemplate() {
		return this.template;
	}

	public int getAverageGroundLevel(World worldIn, StructureBoundingBox structurebb) {
		int i = 0;
		int j = 0;
		BlockPos.MutableBlockPos blockPos$mutableBlockPos = new BlockPos.MutableBlockPos();
		for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k) {
			for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l) {
				blockPos$mutableBlockPos.setPos(l, 64, k);
				if (structurebb.isVecInside(blockPos$mutableBlockPos)) {
					i += Math.max(worldIn.getTopSolidOrLiquidBlock(blockPos$mutableBlockPos).getY(), worldIn.provider.getAverageGroundLevel() - 1);
					++j;
				}
			}
		}
		if (j == 0) {
			return -1;
		}
		else {
			return i / j;
		}
	}

	protected boolean canVillageGoDeeper() {
		return this.boundingBox.minY > 10;
	}

	@Override
	public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
		if (this.template == null) {
			return true;
		}
		if (this.averageGroundLvl < 0) {
			this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
			if (this.averageGroundLvl < 0) {
				return true;
			}
			this.templatePosition.offset(EnumFacing.UP, this.averageGroundLvl);
		}
		return super.addComponentParts(worldIn, randomIn, structureBoundingBoxIn);
	}

	@Override
	protected void handleDataMarker(String function, BlockPos pos, World worldIn, Random rand, StructureBoundingBox sbb) {
	}

    //determines the total weight of all of the structures
    public int updatePieceWeight(List<Piece> pieces) {
        boolean flag = false;
        int i = 0;
        for (Piece piece : pieces) {
            if (piece.villagePiecesLimit > 0 && piece.villagePiecesSpawned < piece.villagePiecesLimit) {
                flag = true;
            }
            i += piece.villagePieceWeight;
        }
        return flag ? i : -1;
    }

    //Creates a structure piece accounting for piece weight
	@Nullable
    public StructureSlayerVillagePieces generateComponent(Random rand) {
        int i = this.updatePieceWeight(this.startPiece.pieces);
        if (i <= 0) {
            return null;
        }
        else {
            int j = 0;
            //see what this does without the loop
            while (j < 5) {
                ++j;
                int k = rand.nextInt(i);
                for (Piece piece : this.startPiece.pieces) {
                    k -= piece.villagePieceWeight;
                    if (k < 0) {
                    	//break if it cant spawn more pieces of the type or if there are more different pieces to place
                        if (!piece.canSpawnMoreVillagePieces() || piece == this.startPiece.lastPlaced && this.startPiece.pieces.size() > 1) {
                            break;
                        }
                        if (this.canVillageGoDeeper()) {
                            ++piece.villagePiecesSpawned;
                            this.startPiece.lastPlaced = piece;
                            if (!piece.canSpawnMoreVillagePieces()) {
                                this.startPiece.pieces.remove(piece);
                            }
                            return new StructureSlayerVillagePieces(this.startPiece, piece.template);
                        }
                    }
                }
            }
        }
        return null;
    }

    //Adds house to start generation
	@Nullable
    public StructureSlayerVillagePieces generateAndAddHouse(List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing) {
        if (Math.abs(structureMinX - this.startPiece.getBoundingBox().minX) <= this.startPiece.radius && Math.abs(structureMinZ - this.startPiece.getBoundingBox().minZ) <= this.startPiece.radius) {
            StructureSlayerVillagePieces structurecomponent = this.generateComponent(rand);
            if (structurecomponent != null) {
            	structurecomponent.templatePosition = new BlockPos(structureMinX, structureMinY, structureMinZ);
            	structurecomponent.placeSettings.getRotation().rotate(facing);
            	//Need to offset bounding box to the position for StructureStart$generateStructure to see that the bounding boxes have intersected and call addComponentParts
            	structurecomponent.getBoundingBox().offset(structureMinX, structureMinY, structureMinZ);
                structureComponents.add(structurecomponent);
                this.startPiece.pendingHouses.add(structurecomponent);
                return structurecomponent;
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    //Adds road to start generation
	@Nullable
    public StructureComponent generateAndAddRoad(List<StructureComponent> structureComponents, Random rand, int x, int y, int z, EnumFacing facing, int type) {
        if (Math.abs(x - this.startPiece.getBoundingBox().minX) <= this.startPiece.radius && Math.abs(z - this.startPiece.getBoundingBox().minZ) <= this.startPiece.radius) {
            StructureBoundingBox structureBoundingBox = StructureSlayerVillagePieces.Path.findPieceBox(structureComponents, rand, x, y, z, facing);
            if (structureBoundingBox != null && structureBoundingBox.minY > 10) {
                StructureComponent structurecomponent = new StructureSlayerVillagePieces.Path(this.startPiece, type, structureBoundingBox, facing);
                structureComponents.add(structurecomponent);
                this.startPiece.pendingRoads.add(structurecomponent);
                return structurecomponent;
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public static class Path extends StructureComponent {

		private int length;
		private StructureSlayerVillagePieces.Start start;

		public Path(StructureSlayerVillagePieces.Start start, int type, StructureBoundingBox structureBoundingBox, EnumFacing facing) {
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

		public void buildComponent(StructureComponent structureComponent, List<StructureComponent> structureComponents, Random rand) {
			boolean flag = false;
			//Adds houses every 2 - 7 + the house's width blocks
			for (int i = rand.nextInt(5); i < this.length - 8; i += 2 + rand.nextInt(5)) {
				StructureComponent house1 = this.getNextComponentNN(structureComponents, rand, 0, i);
				if (house1 != null) {
					i += Math.max(house1.getBoundingBox().getXSize(), house1.getBoundingBox().getZSize());
					flag = true;
				}
			}
			//Adds houses every 2 - 7 + the house's width blocks
			for (int j = rand.nextInt(5); j < this.length - 8; j += 2 + rand.nextInt(5)) {
				StructureComponent house2 = this.getNextComponentPP(structureComponents, rand, 0, j);
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
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, this.getComponentType());
						break;
					case SOUTH:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ, EnumFacing.WEST, this.getComponentType());
						break;
					case WEST:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.NORTH, this.getComponentType());
						break;
					case EAST:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.NORTH, this.getComponentType());
				}
			}
			//Generates roads to the east or south of the end randomly
			if (flag && rand.nextInt(3) > 0 && enumfacing != null) {
				switch (enumfacing) {
					case NORTH:
					default:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, this.getComponentType());
						break;
					case SOUTH:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ, EnumFacing.EAST, this.getComponentType());
						break;
					case WEST:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ, EnumFacing.SOUTH, this.getComponentType());
						break;
					case EAST:
						this.start.generateAndAddRoad(structureComponents, rand, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ, EnumFacing.SOUTH, this.getComponentType());
				}
			}
		}

		//Generates a path 7 - 35 blocks long
		public static StructureBoundingBox findPieceBox(List<StructureComponent> structureComponents, Random rand, int x, int y, int z, EnumFacing facing) {
			for (int i = 7 * MathHelper.getInt(rand, 3, 5); i >= 7; i -= 7) {
				StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 3, 3, i, facing);
				if (StructureComponent.findIntersecting(structureComponents, structureboundingbox) == null) {
					return structureboundingbox;
				}
			}
			return null;
		}

		//Creates the path blocks in the world
		public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
			IBlockState pathBlock = Blocks.STONEBRICK.getDefaultState();
			for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i) {
				for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j) {
					BlockPos blockpos = new BlockPos(i, 64, j);
					if (structureBoundingBox.isVecInside(blockpos)) {
						blockpos = world.getTopSolidOrLiquidBlock(blockpos).down();
						if (blockpos.getY() < world.getSeaLevel()) {
							blockpos = new BlockPos(blockpos.getX(), world.getSeaLevel() - 1, blockpos.getZ());
						}
						while (blockpos.getY() >= world.getSeaLevel() - 1) {
							IBlockState groundBlock = world.getBlockState(blockpos);
							if (groundBlock.getBlock() == Blocks.GRASS && world.isAirBlock(blockpos.up())) {
								world.setBlockState(blockpos, pathBlock, 2);
								break;
							}
							if (groundBlock.getMaterial().isLiquid()) {
								world.setBlockState(blockpos, pathBlock, 2);
								break;
							}
							blockpos = blockpos.down();
						}
					}
				}
			}
			return true;
		}

		@Nullable
		protected StructureComponent getNextComponentNN(List<StructureComponent> structureComponents, Random rand, int shift1, int shift2) {
			EnumFacing enumfacing = this.getCoordBaseMode();
			if (enumfacing != null) {
				switch (enumfacing) {
					case NORTH:
					default:
						return start.generateAndAddHouse(structureComponents, rand, this.boundingBox.minX - 1, this.boundingBox.minY + shift1, this.boundingBox.minZ + shift2, EnumFacing.WEST);
					case WEST:
					case EAST:
						return start.generateAndAddHouse(structureComponents, rand, this.boundingBox.minX + shift2, this.boundingBox.minY + shift1, this.boundingBox.minZ - 1, EnumFacing.NORTH);
				}
			} else {
				return null;
			}
		}

		@Nullable
		protected StructureComponent getNextComponentPP(List<StructureComponent> structureComponents, Random rand, int shift1, int shift2) {
			EnumFacing enumfacing = this.getCoordBaseMode();
			if (enumfacing != null) {
				switch (enumfacing) {
					case NORTH:
					default:
						return start.generateAndAddHouse(structureComponents, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + shift1, this.boundingBox.minZ + shift2, EnumFacing.EAST);
					case WEST:
					case EAST:
						return start.generateAndAddHouse(structureComponents, rand, this.boundingBox.minX + shift2, this.boundingBox.minY + shift1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH);
				}
			} else {
				return null;
			}
		}
	}

    public static class Piece {

		public Template template;
		public final int villagePieceWeight;
		public int villagePiecesSpawned;
		public int villagePiecesLimit;

		public Piece(Template template, int weight, int limit) {
			this.template = template;
			this.villagePieceWeight = weight;
			this.villagePiecesLimit = limit;
		}

		public boolean canSpawnMoreVillagePieces() {
			return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
		}
	}

	public static class Start extends StructureSlayerVillagePieces {

		public Piece lastPlaced;
		public int radius;
		public List<Piece> pieces = new ArrayList<>();
		public List<StructureSlayerVillagePieces> pendingHouses = new ArrayList<>();
		public List<StructureComponent> pendingRoads = new ArrayList<>();

		public Start(int type, int x, int z, int radius) {
			super(null, null);
			this.componentType = type;
			this.boundingBox = new StructureBoundingBox(x, 64, z, x, 78, z);
			this.radius = radius;
			this.startPiece = this;
			this.setCoordBaseMode(EnumFacing.values()[EnumFacing.values().length - 1]);
		}

		@Override
		public void buildComponent(StructureComponent start, List<StructureComponent> structureComponents, Random rand) {
			this.generateAndAddRoad(structureComponents, rand, this.boundingBox.minX - 1, this.boundingBox.maxY, this.boundingBox.minZ + 1, EnumFacing.WEST, this.getComponentType());
			this.generateAndAddRoad(structureComponents, rand, this.boundingBox.maxX + 5, this.boundingBox.maxY, this.boundingBox.minZ + 1, EnumFacing.EAST, this.getComponentType());
			this.generateAndAddRoad(structureComponents, rand, this.boundingBox.minX + 1, this.boundingBox.maxY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			this.generateAndAddRoad(structureComponents, rand, this.boundingBox.minX + 1, this.boundingBox.maxY, this.boundingBox.maxZ + 5, EnumFacing.SOUTH, this.getComponentType());
		}

	}

}
