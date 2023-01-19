package com.solverlabs.droid.rugl.geom;

import com.solverlabs.droid.rugl.gl.StackedRenderer;
import com.solverlabs.droid.rugl.util.Colour;
import com.solverlabs.droid.rugl.util.geom.Vector3f;
import com.solverlabs.droid.rugl.util.geom.Vector3i;
import com.solverlabs.worldcraft.GameMode;
import com.solverlabs.worldcraft.Player;
import com.solverlabs.worldcraft.World;
import com.solverlabs.worldcraft.chunk.Chunk;
import com.solverlabs.worldcraft.chunk.Chunklet;
import com.solverlabs.worldcraft.factories.BlockFactory;
import com.solverlabs.worldcraft.factories.ItemFactory;
import com.solverlabs.worldcraft.inventory.InventoryItem;
import com.solverlabs.worldcraft.skin.geometry.Parallelepiped;
import com.solverlabs.worldcraft.ui.Interaction;


public class LadderBlock {
    private static final byte DIRECTION_EAST = 5;
    private static final byte DIRECTION_NONE = -1;
    private static final byte DIRECTION_NORTH = 2;
    private static final byte DIRECTION_SOUTH = 3;
    private static final byte DIRECTION_WEST = 4;
    private static final Vector3f ZERO_VECTOR = new Vector3f(0.01f, -0.01f, 0.01f);
    private static final float WIDTH = 1.0f;
    private static final Vector3f ONE_VECTOR = new Vector3f(WIDTH, WIDTH, WIDTH);
    private static final Vector3f SOUTH_OFFSET_VECTOR = new Vector3f(0.89625f, -0.01f, 0.0f);
    private static final Vector3f WEST_OFFSET_VECTOR = new Vector3f(0.0f, -0.01f, 0.89625f);
    private static final float HEIGHT = 0.98f;
    private static final float DEPTH = 0.09375f;
    private static final Vector3f EAST_WEST_LADDER_SIZE = new Vector3f(WIDTH, HEIGHT, DEPTH);
    private static final Vector3f SOUTH_NORTH_LADDER_SIZE = new Vector3f(DEPTH, HEIGHT, WIDTH);
    private static final float[] tc = {3.0f, 5.0f, 3.0f, 5.0f, 3.0f, 5.0f, 3.0f, 5.0f, 3.0f, 5.0f, 3.0f, 5.0f};
    private static final float SXTN = 0.0625f;
    private static final Parallelepiped eastWestladderParallelepiped = Parallelepiped.createParallelepiped(WIDTH, HEIGHT, DEPTH, SXTN, tc);
    private static final Parallelepiped southNorthladderParallelepiped = Parallelepiped.createParallelepiped(DEPTH, HEIGHT, WIDTH, SXTN, tc);
    private static final ColouredShape eastLadderPreviewShape = new ColouredShape(ShapeUtil.cuboid(0.0f, 0.0f, -0.01f, WIDTH, HEIGHT, 0.113749996f), Colour.packFloat(WIDTH, WIDTH, WIDTH, 0.3f), ShapeUtil.state);
    private static final ColouredShape westLadderPreviewShape = new ColouredShape(ShapeUtil.cuboid(0.0f, 0.0f, 0.89625f, WIDTH, HEIGHT, 1.02f), Colour.packFloat(WIDTH, WIDTH, WIDTH, 0.3f), ShapeUtil.state);
    private static final ColouredShape northLadderPreviewShape = new ColouredShape(ShapeUtil.cuboid(0.01f, 0.0f, 0.0f, 0.113749996f, HEIGHT, WIDTH), Colour.packFloat(WIDTH, WIDTH, WIDTH, 0.3f), ShapeUtil.state);
    private static final ColouredShape southLadderPreviewShape = new ColouredShape(ShapeUtil.cuboid(0.89625f, 0.0f, 0.0f, 1.025f, HEIGHT, WIDTH), Colour.packFloat(WIDTH, WIDTH, WIDTH, 0.3f), ShapeUtil.state);

    private LadderBlock() {
    }

    public static boolean isLadder(BlockFactory.Block b) {
        return b != null && isLadder(Byte.valueOf(b.id));
    }

    public static boolean isLadder(Byte blockType) {
        return blockType != null && blockType.byteValue() == 76;
    }

    public static TexturedShape getItemShape(byte id) {
        return ItemFactory.Item.getShape(ItemFactory.LADDER_TEX_COORDS);
    }

    public static byte getDefaultState(byte blockType) {
        return (byte) 76;
    }

    public static void renderPreview(World world, StackedRenderer renderer, Vector3i previewLocation) {
        Byte previewBlockType = world.getPreviewBlockType();
        if (previewBlockType != null) {
            getPreviewShape(previewBlockType.byteValue(), world, previewLocation).render(renderer);
        }
    }

    public static void generateGeometry(Chunklet c, ShapeBuilder opaqueShapeBuilder, ShapeBuilder transpShapeBuilder, int x, int y, int z, byte blockId, int colour, byte data) {
        addFaces(c, x, y, z, colour, opaqueShapeBuilder, transpShapeBuilder, blockId, data);
    }

    public static void breakLadder(Chunk chunk, Vector3i blockLocation) {
        changeladderBlockType(chunk, blockLocation, (byte) 0);
    }

    public static boolean canSetLadder(byte blockType) {
        return (blockType == 0 || blockType == 90 || blockType == 122 || blockType == 121 || blockType == 75) ? false : true;
    }

    public static boolean placeLadder(Chunk chunk, Vector3i placementTargetBlock, BlockFactory.WorldSide blockSide, InventoryItem invItem, Player player) {
        int x = placementTargetBlock.x;
        int y = placementTargetBlock.y;
        int z = placementTargetBlock.z;
        byte direction = getDirection(blockSide);
        if (direction == -1) {
            return false;
        }
        chunk.setBlockTypeForPosition(x, y, z, invItem.getItemID(), direction);
        if (GameMode.isSurvivalMode()) {
            player.inventory.decItem(invItem);
        }
        return true;
    }

    private static byte getDirection(BlockFactory.WorldSide blockSide) {
        switch (blockSide) {
            case South:
                return (byte) 2;
            case North:
                return (byte) 3;
            case West:
                return (byte) 5;
            case East:
                return (byte) 4;
            default:
                return (byte) -1;
        }
    }

    private static void addFaces(Chunklet c, int x, int y, int z, int colour, ShapeBuilder opaque, ShapeBuilder transparent, byte id, byte data) {
        Parallelepiped p = getParallelepiped(id, data);
        Vector3f blockSize = getBlockSize(id, data);
        Vector3f offset = getOffset(data);
        float faceX = x + offset.x;
        float faceY = y + offset.y;
        float faceZ = z + offset.z;
        addFace(p, faceX, faceY, faceZ, p.south, blockSize.z, blockSize.y, colour, opaque, false);
        addFace(p, faceX, faceY, faceZ, p.north, blockSize.z, blockSize.y, colour, opaque, false);
        addFace(p, faceX, faceY, faceZ, p.west, blockSize.x, blockSize.y, colour, opaque, true);
        addFace(p, faceX, faceY, faceZ, p.east, blockSize.x, blockSize.y, colour, opaque, false);
        addFace(p, faceX, faceY, faceZ, p.bottom, blockSize.x, blockSize.z, colour, opaque, false);
        addFace(p, faceX, faceY, faceZ, p.top, blockSize.x, blockSize.z, colour, opaque, false);
    }

    private static Vector3f getOffset(byte data) {
        if (data == 5) {
            return ZERO_VECTOR;
        }
        if (data == 4) {
            return WEST_OFFSET_VECTOR;
        }
        if (data == 3) {
            return SOUTH_OFFSET_VECTOR;
        }
        if (data == 2) {
            return ZERO_VECTOR;
        }
        return ZERO_VECTOR;
    }

    private static Parallelepiped getParallelepiped(byte id, byte data) {
        return (data == 5 || data == 4) ? eastWestladderParallelepiped : southNorthladderParallelepiped;
    }

    private static void changeladderBlockType(Chunk chunk, Vector3i blockLocation, byte blockType) {
        chunk.setBlockTypeForPosition(blockLocation.x, blockLocation.y, blockLocation.z, blockType, (byte) 0);
    }

    private static ColouredShape getPreviewShape(byte blockId, World world, Vector3i previewLocation) {
        byte direction = world.blockData(previewLocation.x, previewLocation.y, previewLocation.z);
        if (direction == 4) {
            return westLadderPreviewShape;
        }
        if (direction == 2) {
            return northLadderPreviewShape;
        }
        if (direction == 3) {
            return southLadderPreviewShape;
        }
        return eastLadderPreviewShape;
    }

    public static void parentDestroyed(Chunk chunk, Interaction interaction, World world, Vector3i targetBlockLocation) {
        if (world.blockType(targetBlockLocation.x + 1, targetBlockLocation.y, targetBlockLocation.z) == 76 && world.blockData(targetBlockLocation.x + 1, targetBlockLocation.y, targetBlockLocation.z) == 2) {
            breakLadder(chunk, interaction, targetBlockLocation, WIDTH, 0.0f);
        }
        if (world.blockType(targetBlockLocation.x - 1, targetBlockLocation.y, targetBlockLocation.z) == 76 && world.blockData(targetBlockLocation.x - 1, targetBlockLocation.y, targetBlockLocation.z) == 3) {
            breakLadder(chunk, interaction, targetBlockLocation, -1.0f, 0.0f);
        }
        if (world.blockType(targetBlockLocation.x, targetBlockLocation.y, targetBlockLocation.z + 1) == 76 && world.blockData(targetBlockLocation.x, targetBlockLocation.y, targetBlockLocation.z + 1) == 5) {
            breakLadder(chunk, interaction, targetBlockLocation, 0.0f, WIDTH);
        }
        if (world.blockType(targetBlockLocation.x, targetBlockLocation.y, targetBlockLocation.z - 1) == 76 && world.blockData(targetBlockLocation.x, targetBlockLocation.y, targetBlockLocation.z - 1) == 4) {
            breakLadder(chunk, interaction, targetBlockLocation, 0.0f, -1.0f);
        }
    }

    private static void breakLadder(Chunk chunk, Interaction interaction, Vector3i targetBlockLocation, float xOffset, float zOffset) {
        targetBlockLocation.x = (int) (targetBlockLocation.x + xOffset);
        targetBlockLocation.z = (int) (targetBlockLocation.z + zOffset);
        if (GameMode.isSurvivalMode()) {
            chunk.world.addDroppableItem((byte) 76, targetBlockLocation);
        }
        breakLadder(chunk, targetBlockLocation);
        targetBlockLocation.x = (int) (targetBlockLocation.x - xOffset);
        targetBlockLocation.z = (int) (targetBlockLocation.z - zOffset);
    }

    private static Vector3f getBlockSize(byte blockId, byte data) {
        if (data == 5 || data == 4) {
            return EAST_WEST_LADDER_SIZE;
        }
        if (data == 3 || data == 2) {
            return SOUTH_NORTH_LADDER_SIZE;
        }
        return ONE_VECTOR;
    }

    private static void addFace(Parallelepiped facing, float x, float y, float z, Parallelepiped.Face f, float width, float height, int colour, ShapeBuilder shapBuilder, boolean invertTexture) {
        facing.face(f, x, y, z, width, height, colour, shapBuilder, invertTexture);
    }
}