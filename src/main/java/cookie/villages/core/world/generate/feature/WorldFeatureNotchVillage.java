package cookie.villages.core.world.generate.feature;

import cookie.villages.core.entity.MobPigman;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeature;

import java.util.Random;

public class WorldFeatureNotchVillage extends WorldFeature {

	@Override
	public boolean place(World world, Random rand, int x, int y, int z) {
		while (y > 0 && !world.getBlockMaterial(x, y - 1, z).isSolid()) {
			--y;
		}

		int buildingWidth = rand.nextInt(7) + 7;
		int buildingHeight = 4 + rand.nextInt(3) / 2;
		int buildingDepth = rand.nextInt(7) + 7;
		int xCoordStart = x - buildingWidth / 2;
		int yFloor = y;
		int zCoordStart = z - buildingDepth / 2;
		int doorSide = rand.nextInt(4);

		if (doorSide < 2) {
			buildingDepth += 2;
		} else {
			buildingWidth += 2;
		}

		for (int xPos = xCoordStart; xPos < xCoordStart + buildingWidth; xPos++) {
			for (int zPos = zCoordStart; zPos < zCoordStart + buildingDepth; zPos++) {
				Material blockMaterial = world.getBlockMaterial(xPos, y - 1, zPos);
				if (blockMaterial.isLiquid() || blockMaterial == Material.ice) {
					return false;
				}

				boolean canPlaceDoor = false;
				if (doorSide == 0 && xPos < xCoordStart + 2) {
					canPlaceDoor = true;
				}

				if (doorSide == 1 && xPos > xCoordStart + buildingWidth - 1 - 2) {
					canPlaceDoor = true;
				}

				if (doorSide == 2 && zPos < zCoordStart + 2) {
					canPlaceDoor = true;
				}

				if (doorSide == 3 && zPos > zCoordStart + buildingDepth - 1 - 2) {
					canPlaceDoor = true;
				}

				int blockID = world.getBlockId(xPos, y, zPos);
				if (canPlaceDoor) {
					if (blockID != 0) {
						return false;
					}
				} else if (blockID == Blocks.COBBLE_STONE.id() || blockID == Blocks.COBBLE_STONE_MOSSY.id()) {
					return false;
				}
			}
		}

		if (doorSide == 0) {
			xCoordStart++;
			buildingWidth--;
		} else if (doorSide == 1) {
			buildingWidth--;
		} else if (doorSide == 2) {
			zCoordStart++;
			buildingDepth--;
		} else if (doorSide == 3) {
			buildingDepth--;
		}

		int interiorWestX = xCoordStart;
		int interiorEastX = xCoordStart + buildingWidth - 1;
		int interiorNorthZ = zCoordStart;
		int interiorSouthZ = zCoordStart + buildingDepth - 1;
		if (doorSide >= 2) {
			interiorWestX = xCoordStart + 1;
			interiorEastX--;
		} else {
			interiorNorthZ = zCoordStart + 1;
			interiorSouthZ--;
		}

		for (int xPos = xCoordStart; xPos < xCoordStart + buildingWidth; xPos++) {
			for (int zPos = zCoordStart; zPos < zCoordStart + buildingDepth; zPos++) {
				int distFromNorthEdge = zPos - zCoordStart;
				int distFromSouthEdge = zCoordStart + buildingDepth - 1 - zPos;
				if (doorSide < 2) {
					distFromNorthEdge = xPos - xCoordStart;
					distFromSouthEdge = xCoordStart + buildingWidth - 1 - xPos;
				}

				if (distFromSouthEdge < distFromNorthEdge) {
					distFromNorthEdge = distFromSouthEdge;
				}

				// Calculate height for this position based on distance from edge (creates slope)
				int currentHeight = buildingHeight + distFromNorthEdge;

				for (int height = yFloor - 1; height < yFloor + currentHeight; height++) {
					int blockID = -1;
					if (height == yFloor + currentHeight - 1) {
						blockID = Blocks.PLANKS_OAK.id();
					} else if (xPos >= interiorWestX && xPos <= interiorEastX && zPos >= interiorNorthZ && zPos <= interiorSouthZ) {
						blockID = 0;
						if (height == yFloor - 1 || height == yFloor + currentHeight - 1 || xPos == interiorWestX || zPos == interiorNorthZ || xPos == interiorEastX || zPos == interiorSouthZ) {
							if (height <= yFloor + rand.nextInt(3)) {
								blockID = Blocks.COBBLE_STONE_MOSSY.id();
							} else {
								blockID = Blocks.COBBLE_STONE.id();
							}
						}
					}

					if (blockID >= 0) {
						world.setBlockWithNotify(xPos, height, zPos, blockID);
					}
				}
			}
		}

		int featPosOne = xCoordStart + rand.nextInt(buildingWidth - 4) + 2;
		int featPosTwo = zCoordStart + rand.nextInt(buildingDepth - 4) + 2;
		if (doorSide == 0) {
			featPosOne = xCoordStart;
		}

		if (doorSide == 1) {
			featPosOne = xCoordStart + buildingWidth - 1;
		}

		if (doorSide == 2) {
			featPosTwo = zCoordStart;
		}

		if (doorSide == 3) {
			featPosTwo = zCoordStart + buildingDepth - 1;
		}

		world.setBlockWithNotify(featPosOne, yFloor, featPosTwo, 0);
		world.setBlockWithNotify(featPosOne, yFloor + 1, featPosTwo, 0);

		world.setBlockWithNotify(featPosOne, yFloor, featPosTwo, Blocks.DOOR_PLANKS_OAK_BOTTOM.id());
		world.setBlockWithNotify(featPosOne, yFloor + 1, featPosTwo, Blocks.DOOR_PLANKS_OAK_TOP.id());

		world.setBlockMetadataWithNotify(featPosOne, yFloor, featPosTwo, doorSide);
		world.setBlockMetadataWithNotify(featPosOne, yFloor + 1, featPosTwo, doorSide);

		for (int width = 0; width < (buildingWidth * 2 + buildingDepth * 2) * 3; width++) {
			int featPosX = xCoordStart + rand.nextInt(buildingWidth - 4) + 2; // Fixed variable name
			int featPosZ = zCoordStart + rand.nextInt(buildingDepth - 4) + 2; // Fixed variable name
			int randSpot = rand.nextInt(4);

			if (randSpot == 0) {
				featPosX = interiorWestX;
			}
			if (randSpot == 1) {
				featPosX = interiorEastX;
			}
			if (randSpot == 2) {
				featPosZ = interiorNorthZ;
			}
			if (randSpot == 3) {
				featPosZ = interiorSouthZ;
			}

			if (!world.canBlockSeeTheSky(featPosX, yFloor + 1, featPosZ)) {
				int var42 = 0;
				if (world.isBlockNormalCube(featPosX - 1, yFloor + 1, featPosZ) && world.isBlockNormalCube(featPosX + 1, yFloor + 1, featPosZ)) {
					var42++;
				}

				if (world.isBlockNormalCube(featPosX, yFloor + 1, featPosZ - 1) && world.isBlockNormalCube(featPosX, yFloor + 1, featPosZ + 1)) {
					var42++;
				}

				if (var42 == 1) {
					if (world.getBlockId(featPosX, yFloor + 1, featPosZ) == Blocks.DOOR_PLANKS_OAK_TOP.id()) continue;
					world.setBlockWithNotify(featPosX, yFloor + 1, featPosZ, Blocks.GLASS.id());
				}
			}
		}


		featPosOne = interiorEastX - interiorWestX;
		featPosTwo = interiorSouthZ - interiorNorthZ;

		for (int i = 0; i < featPosOne * 2 + featPosTwo * 2; i++) {
			int torchPosX = interiorWestX + rand.nextInt(featPosOne - 1) + 1;
			int torchPosZ = interiorNorthZ + rand.nextInt(featPosTwo - 1) + 1;
			if (!world.isBlockNormalCube(torchPosX, yFloor + 2, torchPosZ)) {
				int var44 = 0;
				if (world.isBlockNormalCube(torchPosX - 1, yFloor + 2, torchPosZ)) {
					var44++;
				}

				if (world.isBlockNormalCube(torchPosX + 1, yFloor + 2, torchPosZ)) {
					var44++;
				}

				if (world.isBlockNormalCube(torchPosX, yFloor + 2, torchPosZ - 1)) {
					var44++;
				}

				if (world.isBlockNormalCube(torchPosX, yFloor + 2, torchPosZ + 1)) {
					var44++;
				}

				if (var44 == 1) {
					world.setBlockWithNotify(torchPosX, yFloor + 2, torchPosZ, Blocks.TORCH_COAL.id());
				}
			}
		}

		MobPigman mobPigman = new MobPigman(world);
		mobPigman.setPos(xCoordStart + buildingWidth / 2.0F, yFloor + 0.5F, zCoordStart + buildingDepth / 2.0F);
		world.entityJoinedWorld(mobPigman);
		return true;
	}
}
