package com.mcal.worldcraft.factories;

import androidx.annotation.NonNull;

import com.mcal.droid.rugl.geom.BedBlock;
import com.mcal.droid.rugl.geom.ColouredShape;
import com.mcal.droid.rugl.geom.DoorBlock;
import com.mcal.droid.rugl.geom.LadderBlock;
import com.mcal.droid.rugl.geom.Shape;
import com.mcal.droid.rugl.geom.ShapeBuilder;
import com.mcal.droid.rugl.geom.ShapeUtil;
import com.mcal.droid.rugl.geom.TexturedShape;
import com.mcal.droid.rugl.gl.GLUtil;
import com.mcal.droid.rugl.gl.State;
import com.mcal.droid.rugl.gl.enums.FogMode;
import com.mcal.droid.rugl.gl.enums.MagFilter;
import com.mcal.droid.rugl.gl.enums.MinFilter;
import com.mcal.droid.rugl.gl.facets.Fog;
import com.mcal.droid.rugl.res.BitmapLoader;
import com.mcal.droid.rugl.res.ResourceLoader;
import com.mcal.droid.rugl.texture.Texture;
import com.mcal.droid.rugl.texture.TextureFactory;
import com.mcal.droid.rugl.util.Colour;
import com.mcal.droid.rugl.util.Trig;
import com.mcal.worldcraft.material.Material;

import java.util.HashMap;
import java.util.Map;

public class BlockFactory {
    public static final byte BARE_HANDS = 0;
    public static final byte STONE_ID = 1;
    public static final byte DIRT_WITH_GRASS_ID = 2;
    public static final byte DIRT_ID = 3;
    public static final byte COBBLE_ID = 4;
    public static final byte WOODEN_PLANKS_ID = 5;

    public static final byte BEDROCK_ID = 7;
    public static final byte WATER_ID = 8;
    public static final byte STILL_WATER_ID = 9;
    public static final byte LAVA_ID = 10;
    public static final byte STILL_LAVA_ID = 11;
    public static final byte SAND_ID = 12;
    public static final byte GRAVEL_ID = 13;
    public static final byte GOLD_ORE_ID = 14;
    public static final byte IRON_ORE_ID = 15;
    public static final byte COAL_ORE_ID = 16;
    public static final byte WOOD_ID = 17;
    public static final byte LEAVES_ID = 18;
    public static final byte SPONGE_ID = 19;
    public static final byte GLASS_ID = 20;
    public static final byte LAPIS_LAZULI_ORE_ID = 21;
    public static final byte LAPIS_LAZULI_BLOCK_ID = 22;
    public static final byte DISPENSER_ID = 23;
    public static final byte SAND_STONE_ID = 24;
    public static final byte NOTE_BLOCK_ID = 25;
    public static final byte WOOD_SWORD_ID = 26;
    public static final byte WOOD_SHOVEL_ID = 27;
    public static final byte WOOD_PICK_ID = 28;
    public static final byte WOOD_AXE_ID = 29;
    public static final byte STONE_SWORD_ID = 30;
    public static final byte STONE_SHOVEL_ID = 31;
    public static final byte STONE_PICK_ID = 32;
    public static final byte STONE_AXE_ID = 33;
    public static final byte IRON_SWORD_ID = 34;
    public static final byte WOOL_ID = 35;
    public static final byte IRON_SHOVEL_ID = 36;
    public static final byte IRON_PICK_ID = 37;
    public static final byte IRON_AXE_ID = 38;
    public static final byte GOLD_SWORD_ID = 39;
    public static final byte GOLD_SHOVEL_ID = 40;
    public static final byte GOLD_BLOCK_ID = 41;
    public static final byte IRON_BLOCK_ID = 42;
    public static final byte DOUBLE_SLAB_ID = 43;
    public static final byte SLAB_ID = 44;
    public static final byte BRICK_BLOCK_ID = 45;
    public static final byte TNT_ID = 46;
    public static final byte BOOKSHELF_ID = 47;
    public static final byte MOSS_STONE_ID = 48;
    public static final byte OBSIDIAN_ID = 49;
    public static final byte GOLD_PICK_ID = 50;
    public static final byte GOLD_AXE_ID = 51;
    public static final byte DIAMOND_SWORD_ID = 52;
    public static final byte DIAMOND_SHOVEL_ID = 53;
    public static final byte CHEST_ID = 54;
    public static final byte DIAMOND_PICK_ID = 55;
    public static final byte DIAMOND_ORE_ID = 56;
    public static final byte DIAMOND_BLOCK_ID = 57;
    public static final byte CRAFTING_TABLE_ID = 58;
    public static final byte DIAMOND_AXE_ID = 59;
    public static final byte FARMLAND_ID = 60;
    public static final byte FURNACE_ID = 61;
    public static final byte CLOSED_WOOD_DOOR_ID = 62;
    public static final byte STICK_ID = 63;
    public static final byte OPENED_WOOD_DOOR_ID = 64;
    public static final byte CLOSED_IRON_DOOR_ID = 65;
    public static final byte OPENED_IRON_DOOR_ID = 66;
    public static final byte ITEMS_LABEL_ID = 67;
    public static final byte RAW_PORKCHOP_ID = 68;
    public static final byte RAW_BEEF_ID = 69;
    public static final byte ROTTEN_FLESH_ID = 70;
    public static final byte SHEARS_ID = 71;
    public static final byte STEAK_ID = 72;
    public static final byte REDSTONE_ORE_ID = 73;
    public static final byte COOKED_PORKCHOP_ID = 74;
    public static final byte BED_ID = 75;
    public static final byte LADDER_ID = 76;

    public static final byte SNOWY_GRASS_ID = 78;
    public static final byte SNOW_ID = 79;
    public static final byte ICE_ID = 80;
    public static final byte CACTUS_ID = 81;
    public static final byte CLAY_ORE_ID = 82;

    public static final byte JUKEBOX_ID = 84;

    public static final byte PUMPKIN_ID = 86;
    public static final byte NETHERRACK_ID = 87;
    public static final byte SOUL_SAND_ID = 88;
    public static final byte GLOW_STONE_ID = 89;
    public static final byte TORCH_ID = 90;
    public static final byte WOOL_BLACK_ID = 91;
    public static final byte WOOL_GRAY_ID = 92;
    public static final byte WOOL_RED_ID = 93;
    public static final byte WOOL_PINK_ID = 94;
    public static final byte WOOL_GREEN_ID = 95;
    public static final byte WOOL_LIME_ID = 96;
    public static final byte WOOL_BROWN_ID = 97;
    public static final byte WOOL_YELLOW_ID = 98;
    public static final byte WOOL_BLUE_ID = 99;
    public static final byte WOOL_LIGHT_BLUE_ID = 100;
    public static final byte WOOL_MAGENTA_ID = 101;
    public static final byte WOOL_CYAN_ID = 102;
    public static final byte WOOL_ORANGE_ID = 103;
    public static final byte WOOL_LIGHT_GRAY_ID = 104;
    public static final byte NETHER_BRICK_ID = 105;
    public static final byte OBSIDIAN2_ID = 106;
    public static final byte IRON_INGOT_ID = 107;
    public static final byte GOLD_INGOT_ID = 108;
    public static final byte STONE_BRICK_ID = 109;
    public static final byte MELON_ID = 110;
    public static final byte DIAMOND_INGOT_ID = 111;
    public static final byte EMERALD_ID = 112;
    public static final byte SANDSTONE2_ID = 113;
    public static final byte MOSS_STONE2_ID = 114;
    public static final byte STONE_BRICK_MOSSY_ID = 115;
    public static final byte WOOD_PLANK_PINE_ID = 116;
    public static final byte WOOD_PLANK_JUNGLE_ID = 117;
    public static final byte LEAVES_JUNGLE_ID = 118;
    public static final byte FURNACE_ACTIVE_ID = 119;
    public static final byte BRICK_ID = 120;
    public static final byte GRASS_ID = 121;
    public static final byte FLOWER_ID = 122;

    /**
     * A map of block id values to {@link Block}s
     */
    private static final Map<Byte, Block> blocks;
    private static final ColouredShape itemShape;
    private static final float sxtn = 1.0f / 16;
    private static final float[] nbl = {0.0f, 0.0f, 0.0f};
    private static final float[] ntl = {0.0f, 1.0f, 0.0f};
    private static final float[] nbr = {1.0f, 0.0f, 0.0f};
    private static final float[] ntr = {1.0f, 1.0f, 0.0f};
    private static final float[] fbl = {0.0f, 0.0f, 1.0f};
    private static final float[] ftl = {0.0f, 1.0f, 1.0f};
    private static final float[] fbr = {1.0f, 0.0f, 1.0f};
    private static final float[] ftr = {1.0f, 1.0f, 1.0f};
    /**
     * Rendering state for blocks. Texture filtering is for wimps
     */
    public static State state;
    /**
     * The terrain.png texture
     */
    public static Texture texture;
    private static HashMap<Byte, Material> blockMaterials = null;

    static {
        float[] hexVerts = new float[2 * 7];
        hexVerts[0] = 0.0f;
        hexVerts[1] = 0.0f;

        float[] angles = {30.0f, 90.0f, 150.0f, 210.0f, 270.0f, 330.0f};
        for (int i = 0; i < angles.length; i++) {
            hexVerts[(i + 1) * 2] = 0.5f * Trig.cos(Trig.toRadians(angles[i]));
            hexVerts[((i + 1) * 2) + 1] = 0.5f * Trig.sin(Trig.toRadians(angles[i]));
        }

        float[] itemCoords = new float[36];
        int i = 0;
        // top
        addVert(itemCoords, i++, hexVerts, 0);
        addVert(itemCoords, i++, hexVerts, 3);
        addVert(itemCoords, i++, hexVerts, 1);
        addVert(itemCoords, i++, hexVerts, 2);

        // left
        addVert(itemCoords, i++, hexVerts, 4);
        addVert(itemCoords, i++, hexVerts, 3);
        addVert(itemCoords, i++, hexVerts, 5);
        addVert(itemCoords, i++, hexVerts, 0);

        // right
        addVert(itemCoords, i++, hexVerts, 5);
        addVert(itemCoords, i++, hexVerts, 0);
        addVert(itemCoords, i++, hexVerts, 6);
        addVert(itemCoords, i++, hexVerts, 1);

        short[] tris = ShapeUtil.makeQuads(12, 0, null, 0);

        int[] colours = new int[12];
        int top = Colour.packFloat(1.0f, 1.0f, 1.0f, 1.0f);
        int left = Colour.packFloat(0.75f, 0.75f, 0.75f, 1.0f);
        int right = Colour.packFloat(0.5f, 0.5f, 0.5f, 1.0f);
        for (i = 0; i < 4; i++) {
            colours[i] = top;
            colours[i + 4] = left;
            colours[i + 8] = right;
        }
        itemShape = new ColouredShape(new Shape(itemCoords, tris), colours, null);
        state = GLUtil.typicalState.with(MinFilter.NEAREST, MagFilter.NEAREST).with(new Fog(FogMode.LINEAR, 0.5f, 30.0f, 40.0f, Colour.packFloat(0.7f, 0.7f, 0.9f, 1.0f)));
        blocks = new HashMap<>();
        Block[] blocks1 = Block.values();
        for (Block b : blocks1) {
            blocks.put(b.id, b);
        }
    }

    private static void addVert(@NonNull float[] itemVerts, int index, @NonNull float[] hexCoords, int vertIndex) {
        itemVerts[index * 3] = hexCoords[vertIndex * 2];
        itemVerts[(index * 3) + 1] = hexCoords[(vertIndex * 2) + 1];
        itemVerts[(index * 3) + 2] = 0.0f;
    }

    /**
     * Synchronously loads the terrain texture
     */
    public static void loadTexture() {
        ResourceLoader.loadNow(new BitmapLoader("terrain.png") {
            @Override
            public void complete() {
                BlockFactory.texture = TextureFactory.buildTexture(resource, true, true);
                if (BlockFactory.texture != null) {
                    BlockFactory.state = BlockFactory.texture.applyTo(BlockFactory.state);
                    Block[] blocks = Block.values();
                    for (Block b : blocks) {
                        if (b.blockItemShape != null) {
                            b.blockItemShape.state = BlockFactory.state;
                        }
                    }
                }
                resource.bitmap.recycle();
            }
        });
    }

    /**
     * @param id
     * @return The so-typed block
     */
    public static Block getBlock(byte id) {
        return blocks.get(id);
    }

    /**
     * @param id
     * @return <code>true</code> if the block is opaque, <code>false</code> if
     * transparent
     */
    public static boolean opaque(byte id) {
        Block b = blocks.get(id);
        if (b == null) {
            return false;
        }
        return b.opaque;
    }

    private static void initBlockMaterials() {
        blockMaterials = new HashMap<>();
        blockMaterials.put(STONE_ID, Material.STONE);
        blockMaterials.put(DIRT_WITH_GRASS_ID, Material.GRASS);
        blockMaterials.put(DIRT_ID, Material.GRAVEL);
        blockMaterials.put(COBBLE_ID, Material.STONE);
        blockMaterials.put(WOODEN_PLANKS_ID, Material.WOOD);
        blockMaterials.put(BEDROCK_ID, Material.STONE);
        blockMaterials.put(WATER_ID, Material.WATER);
        blockMaterials.put(STILL_WATER_ID, Material.WATER);
        blockMaterials.put(LAVA_ID, Material.LAVA);
        blockMaterials.put(STILL_LAVA_ID, Material.LAVA);
        blockMaterials.put(SAND_ID, Material.GRAVEL);
        blockMaterials.put(GRAVEL_ID, Material.GRAVEL);
        blockMaterials.put(GOLD_ORE_ID, Material.STONE);
        blockMaterials.put(IRON_ORE_ID, Material.STONE);
        blockMaterials.put(COAL_ORE_ID, Material.STONE);
        blockMaterials.put(WOOD_ID, Material.WOOD);
        blockMaterials.put(LEAVES_ID, Material.GRASS);
        blockMaterials.put(GLASS_ID, Material.STONE);
        blockMaterials.put(LAPIS_LAZULI_ORE_ID, Material.STONE);
        blockMaterials.put(LAPIS_LAZULI_BLOCK_ID, Material.STONE);
        blockMaterials.put(DISPENSER_ID, Material.STONE);
        blockMaterials.put(SAND_STONE_ID, Material.STONE);
        blockMaterials.put(WOOD_SWORD_ID, Material.WOOD);
        blockMaterials.put(WOOD_SHOVEL_ID, Material.WOOD);
        blockMaterials.put(WOOD_PICK_ID, Material.WOOD);
        blockMaterials.put(WOOD_AXE_ID, Material.WOOD);
        blockMaterials.put(STONE_SWORD_ID, Material.STONE);
        blockMaterials.put(STONE_SHOVEL_ID, Material.STONE);
        blockMaterials.put(STONE_PICK_ID, Material.STONE);
        blockMaterials.put(STONE_AXE_ID, Material.STONE);
        blockMaterials.put(IRON_SWORD_ID, Material.STONE);
        blockMaterials.put(WOOL_ID, Material.GRAVEL);
        blockMaterials.put(IRON_SHOVEL_ID, Material.STONE);
        blockMaterials.put(IRON_PICK_ID, Material.STONE);
        blockMaterials.put(IRON_AXE_ID, Material.STONE);
        blockMaterials.put(GOLD_SWORD_ID, Material.STONE);
        blockMaterials.put(GOLD_SHOVEL_ID, Material.STONE);
        blockMaterials.put(GOLD_BLOCK_ID, Material.STONE);
        blockMaterials.put(IRON_BLOCK_ID, Material.STONE);
        blockMaterials.put(DOUBLE_SLAB_ID, Material.STONE);
        blockMaterials.put(SLAB_ID, Material.STONE);
        blockMaterials.put(BRICK_BLOCK_ID, Material.STONE);
        blockMaterials.put(TNT_ID, Material.STONE);
        blockMaterials.put(BOOKSHELF_ID, Material.WOOD);
        blockMaterials.put(MOSS_STONE_ID, Material.STONE);
        blockMaterials.put(OBSIDIAN_ID, Material.STONE);
        blockMaterials.put(GOLD_PICK_ID, Material.STONE);
        blockMaterials.put(GOLD_AXE_ID, Material.STONE);
        blockMaterials.put(DIAMOND_SWORD_ID, Material.STONE);
        blockMaterials.put(DIAMOND_SHOVEL_ID, Material.STONE);
        blockMaterials.put(CHEST_ID, Material.WOOD);
        blockMaterials.put(DIAMOND_PICK_ID, Material.STONE);
        blockMaterials.put(DIAMOND_ORE_ID, Material.STONE);
        blockMaterials.put(DIAMOND_BLOCK_ID, Material.STONE);
        blockMaterials.put(CRAFTING_TABLE_ID, Material.WOOD);
        blockMaterials.put(DIAMOND_AXE_ID, Material.STONE);
        blockMaterials.put(FARMLAND_ID, Material.GRAVEL);
        blockMaterials.put(FURNACE_ID, Material.STONE);
        blockMaterials.put(CLOSED_WOOD_DOOR_ID, Material.WOOD);
        blockMaterials.put(STICK_ID, Material.WOOD);
        blockMaterials.put(OPENED_WOOD_DOOR_ID, Material.WOOD);
        blockMaterials.put(CLOSED_IRON_DOOR_ID, Material.STONE);
        blockMaterials.put(OPENED_IRON_DOOR_ID, Material.STONE);
        blockMaterials.put(ITEMS_LABEL_ID, Material.WOOD);
        blockMaterials.put(REDSTONE_ORE_ID, Material.STONE);
        blockMaterials.put(BED_ID, Material.WOOD);
        blockMaterials.put(LADDER_ID, Material.WOOD);
        blockMaterials.put(SNOWY_GRASS_ID, Material.GRASS);
        blockMaterials.put(SNOW_ID, Material.GRAVEL);
        blockMaterials.put(ICE_ID, Material.STONE);
        blockMaterials.put(CACTUS_ID, Material.GRASS);
        blockMaterials.put(CLAY_ORE_ID, Material.STONE);
        blockMaterials.put(JUKEBOX_ID, Material.WOOD);
        blockMaterials.put(PUMPKIN_ID, Material.GRASS);
        blockMaterials.put(NETHERRACK_ID, Material.GRAVEL);
        blockMaterials.put(SOUL_SAND_ID, Material.GRAVEL);
        blockMaterials.put(GLOW_STONE_ID, Material.STONE);
        blockMaterials.put(WOOL_BLACK_ID, Material.GRAVEL);
        blockMaterials.put(WOOL_GRAY_ID, Material.GRAVEL);
        blockMaterials.put(WOOL_RED_ID, Material.GRAVEL);
        blockMaterials.put(WOOL_PINK_ID, Material.GRAVEL);
        blockMaterials.put(WOOL_GREEN_ID, Material.GRAVEL);
        blockMaterials.put(WOOL_LIME_ID, Material.GRAVEL);
        blockMaterials.put(WOOL_BROWN_ID, Material.GRAVEL);
        blockMaterials.put(WOOL_YELLOW_ID, Material.GRAVEL);
        blockMaterials.put(WOOL_BLUE_ID, Material.GRAVEL);
        blockMaterials.put(WOOL_LIGHT_BLUE_ID, Material.GRAVEL);
        blockMaterials.put(WOOL_MAGENTA_ID, Material.GRAVEL);
        blockMaterials.put(WOOL_CYAN_ID, Material.GRAVEL);
        blockMaterials.put(WOOL_ORANGE_ID, Material.GRAVEL);
        blockMaterials.put(WOOL_LIGHT_GRAY_ID, Material.GRAVEL);
        blockMaterials.put(NETHER_BRICK_ID, Material.STONE);
        blockMaterials.put(OBSIDIAN2_ID, Material.STONE);
        blockMaterials.put(STONE_BRICK_ID, Material.STONE);
        blockMaterials.put(EMERALD_ID, Material.STONE);
        blockMaterials.put(SANDSTONE2_ID, Material.STONE);
        blockMaterials.put(MOSS_STONE2_ID, Material.STONE);
        blockMaterials.put(STONE_BRICK_MOSSY_ID, Material.STONE);
        blockMaterials.put(WOOD_PLANK_PINE_ID, Material.WOOD);
        blockMaterials.put(WOOD_PLANK_JUNGLE_ID, Material.WOOD);
        blockMaterials.put(LEAVES_JUNGLE_ID, Material.GRASS);
        blockMaterials.put(FURNACE_ACTIVE_ID, Material.STONE);
        blockMaterials.put(BRICK_ID, Material.STONE);
        blockMaterials.put(GRASS_ID, Material.GRASS);
        blockMaterials.put(FLOWER_ID, Material.GRASS);
        blockMaterials.put(TORCH_ID, Material.WOOD);
    }

    public static Material getBlockMaterial(byte id) {
        if (blockMaterials == null) {
            initBlockMaterials();
        }
        return blockMaterials.containsKey(id) ? blockMaterials.get(id) : Material.UNKNOWN;
    }

    public enum WorldSide {
        South,
        West,
        East,
        North,
        Top,
        Bottom,
        Empty
    }

    /**
     * Holds vertex positions for each face of a unit cube
     */
    public enum Face {
        /**
         * -ve z direction
         */
        East(BlockFactory.fbl, BlockFactory.ftl, BlockFactory.nbl, BlockFactory.ntl),
        /**
         * +ve z direction
         */
        West(BlockFactory.nbr, BlockFactory.ntr, BlockFactory.fbr, BlockFactory.ftr),
        /**
         * +ve x direction
         */
        South(BlockFactory.nbl, BlockFactory.ntl, BlockFactory.nbr, BlockFactory.ntr),
        /**
         * -ve x direction
         */
        North(BlockFactory.fbr, BlockFactory.ftr, BlockFactory.fbl, BlockFactory.ftl),
        /**
         * +ve y direction
         */
        Top(BlockFactory.ntl, BlockFactory.ftl, BlockFactory.ntr, BlockFactory.ftr),
        /**
         * -ve y direction
         */
        Bottom(BlockFactory.nbl, BlockFactory.fbl, BlockFactory.nbr, BlockFactory.fbr);

        private final float[] verts = new float[12];

        Face(@NonNull float[]... verts) {
            for (int i = 0; i < verts.length; i++) {
                System.arraycopy(verts[i], 0, this.verts, i * 3, 3);
            }
        }
    }

    /**
     * Block types
     */
    public enum Block {
        Stone(BlockFactory.STONE_ID, true, 1, 0),
        DirtWithGrass(BlockFactory.DIRT_WITH_GRASS_ID, true, 3, 0, 0, 0, 2, 0),
        Dirt(BlockFactory.DIRT_ID, true, 2, 0),
        Cobble(BlockFactory.COBBLE_ID, true, 0, 1),
        WoodenPlanks(BlockFactory.WOODEN_PLANKS_ID, true, 4, 0),
        Bedrock(BlockFactory.BEDROCK_ID, true, 1, 1),
        Water(BlockFactory.WATER_ID, false, 15, 12),
        StillWater(BlockFactory.STILL_WATER_ID, false, 15, 12),
        Lava(BlockFactory.LAVA_ID, true, 15, 15),
        StillLava(BlockFactory.STILL_LAVA_ID, true, 15, 15),
        Sand(BlockFactory.SAND_ID, true, 2, 1),
        Gravel(BlockFactory.GRAVEL_ID, true, 3, 1),
        GoldOre(BlockFactory.GOLD_ORE_ID, true, 0, 2),
        IronOre(BlockFactory.IRON_ORE_ID, true, 1, 2),
        CoalOre(BlockFactory.COAL_ORE_ID, true, 2, 2),
        Wood(BlockFactory.WOOD_ID, true, 4, 1, 5, 1),
        Leaves(BlockFactory.LEAVES_ID, true, 5, 3),
        Sponge(BlockFactory.SPONGE_ID, true, 0, 3),
        Glass(BlockFactory.GLASS_ID, false, 1, 3),
        LapisLazuliOre(BlockFactory.LAPIS_LAZULI_ORE_ID, true, 0, 10),
        LapisLazuliBlock(BlockFactory.LAPIS_LAZULI_BLOCK_ID, true, 0, 9),
        Dispenser(BlockFactory.DISPENSER_ID, true, 13, 2, 13, 2, 13, 2, 14, 2, 14, 3, 14, 3),
        SandStone(BlockFactory.SAND_STONE_ID, true, 0, 12, 0, 11, 0, 13),
        NoteBlock(BlockFactory.NOTE_BLOCK_ID, true, 10, 4),
        Wool(BlockFactory.WOOL_ID, true, 0, 4),
        GoldBlock(BlockFactory.GOLD_BLOCK_ID, true, 7, 1),
        IronBlock(BlockFactory.IRON_BLOCK_ID, true, 6, 1),
        DoubleSlab(BlockFactory.DOUBLE_SLAB_ID, true, 5, 0, 6, 0),
        Slab(BlockFactory.SLAB_ID, true, 5, 0, 6, 0),
        BrickBlock(BlockFactory.BRICK_BLOCK_ID, true, 7, 0),
        TNT(BlockFactory.TNT_ID, true, 8, 0, 9, 0, 10, 0),
        Bookshelf(BlockFactory.BOOKSHELF_ID, true, 3, 2),
        MossStone(BlockFactory.MOSS_STONE_ID, true, 4, 2),
        Obsidian(BlockFactory.OBSIDIAN_ID, true, 5, 2),
        Chest(BlockFactory.CHEST_ID, true, 10, 1, 10, 1, 10, 1, 11, 1, 8, 2, 8, 2),
        DiamondOre(BlockFactory.DIAMOND_ORE_ID, true, 2, 3),
        DiamondBlock(BlockFactory.DIAMOND_BLOCK_ID, true, 8, 1),
        CraftingTable(CRAFTING_TABLE_ID, true, 11, 3, 12, 3, 11, 3, 12, 3, 11, 2, 11, 2),
        Farmland(BlockFactory.FARMLAND_ID, true, 2, 0, 7, 5, 2, 0),
        Furnace(BlockFactory.FURNACE_ID, true, 13, 2, 13, 2, 13, 2, 12, 2, 14, 3, 14, 3),
        FurnaceActive(BlockFactory.FURNACE_ACTIVE_ID, true, 13, 2, 13, 2, 13, 2, 13, 3, 14, 3, 14, 3),
        ClosedWoodDoor(BlockFactory.CLOSED_WOOD_DOOR_ID, false, 1, 5, 1, 6),
        OpenedWoodDoor(BlockFactory.OPENED_WOOD_DOOR_ID, false, 1, 5, 1, 6),
        ClosedIronDoor(BlockFactory.CLOSED_IRON_DOOR_ID, false, 2, 5, 2, 6),
        OpenedIronDoor(BlockFactory.OPENED_IRON_DOOR_ID, false, 2, 5, 2, 6),
        Bed(BlockFactory.BED_ID, false, 5, 9, 6, 9),
        Ladder(BlockFactory.LADDER_ID, false, false, 3, 5),
        RedstoneOre(BlockFactory.REDSTONE_ORE_ID, true, 3, 3),
        SnowyGrass(BlockFactory.SNOWY_GRASS_ID, true, 4, 4, 2, 4, 2, 0),
        Snow(BlockFactory.SNOW_ID, true, 2, 4),
        Ice(BlockFactory.ICE_ID, true, 3, 4),
        Cactus(BlockFactory.CACTUS_ID, false, 6, 4, 5, 4, 7, 4),
        ClayOre(BlockFactory.CLAY_ORE_ID, true, 8, 4),
        Jukebox(BlockFactory.JUKEBOX_ID, true, 10, 4, 11, 4, 10, 4),
        Pumpkin(BlockFactory.PUMPKIN_ID, true, 6, 7, 6, 7, 6, 7, 7, 7, 6, 6, 6, 6),
        Netherrack(BlockFactory.NETHERRACK_ID, true, 7, 6),
        SoulSand(BlockFactory.SOUL_SAND_ID, true, 8, 6),
        GlowStone(BlockFactory.GLOW_STONE_ID, true, 9, 6),
        Torch(BlockFactory.TORCH_ID, false, false, 0, 5),
        Grass(BlockFactory.GRASS_ID, false, false, 7, 2),
        Flower(BlockFactory.FLOWER_ID, false, false, 13, 0),
        WoolBlack(BlockFactory.WOOL_BLACK_ID, true, 1, 7),
        WoolGray(BlockFactory.WOOL_GRAY_ID, true, 2, 7),
        WoolRed(BlockFactory.WOOL_RED_ID, true, 1, 8),
        WoolPink(BlockFactory.WOOL_PINK_ID, true, 2, 8),
        WoolGreen(BlockFactory.WOOL_GREEN_ID, true, 1, 9),
        WoolLime(BlockFactory.WOOL_LIME_ID, true, 2, 9),
        WoolBrown(BlockFactory.WOOL_BROWN_ID, true, 1, 10),
        WoolYellow(BlockFactory.WOOL_YELLOW_ID, true, 2, 10),
        WoolBlue(BlockFactory.WOOL_BLUE_ID, true, 1, 11),
        WoolLightBlue(BlockFactory.WOOL_LIGHT_BLUE_ID, true, 2, 11),
        WoolMagenta(BlockFactory.WOOL_MAGENTA_ID, true, 2, 12),
        WoolCyan(BlockFactory.WOOL_CYAN_ID, true, 1, 13),
        WoolOrange(BlockFactory.WOOL_ORANGE_ID, true, 2, 13),
        WoolLightGray(BlockFactory.WOOL_LIGHT_GRAY_ID, true, 1, 14),
        NetherBrick(BlockFactory.NETHER_BRICK_ID, true, 0, 14),
        Obsidian2(BlockFactory.OBSIDIAN2_ID, true, 5, 2),
        StoneBrick(BlockFactory.STONE_BRICK_ID, true, 6, 3),
        Melon(BlockFactory.MELON_ID, true, 8, 8, 9, 8),
        Emerald(BlockFactory.EMERALD_ID, true, 9, 1),
        Sandstone2(BlockFactory.SANDSTONE2_ID, true, 5, 14),
        MossStone2(BlockFactory.MOSS_STONE2_ID, true, 4, 2),
        StoneBrickMossy(BlockFactory.STONE_BRICK_MOSSY_ID, true, 4, 6),
        WoodPlankPine(BlockFactory.WOOD_PLANK_PINE_ID, true, 6, 12),
        WoodPlankJungle(BlockFactory.WOOD_PLANK_JUNGLE_ID, true, 7, 12),
        LeavesJungle(BlockFactory.LEAVES_JUNGLE_ID, true, 5, 12);

        public final byte id;
        public final boolean isCuboid;
        public final Material material;
        public final boolean opaque;
        /**
         * Sides then top, then bottom
         */
        public final int[] texCoords;
        /**
         * Shape with which to draw this block in the gui. It's 1 unit high and
         * centered on the origin
         */
        public TexturedShape blockItemShape;

        Block(byte id, boolean opaque, int... tc) {
            this(id, opaque, true, tc);
        }

        /**
         * @param id     block type identifier
         * @param opaque <code>true</code> if you can't see through the block
         * @param tc     coordinates of the face textures in terrain.png. e.g.: grass
         *               is (0,0), stone is (1,0), mossy cobblestone is (4,2)
         */
        Block(byte id, boolean opaque, boolean isCuboid, @NonNull int... tc) {
            this.id = id;
            this.opaque = opaque;
            this.isCuboid = isCuboid;
            material = BlockFactory.getBlockMaterial(id);
            if (tc.length == 6) {
                texCoords = new int[]{tc[0], tc[1], tc[0], tc[1], tc[0], tc[1], tc[0], tc[1], tc[2], tc[3], tc[4], tc[5]};
            } else if (tc.length == 4) {
                texCoords = new int[]{tc[0], tc[1], tc[0], tc[1], tc[0], tc[1], tc[0], tc[1], tc[2], tc[3], tc[2], tc[3]};
            } else if (tc.length == 2) {
                texCoords = new int[]{tc[0], tc[1], tc[0], tc[1], tc[0], tc[1], tc[0], tc[1], tc[0], tc[1], tc[0], tc[1]};
            } else {
                texCoords = tc;
            }
            if (!isCuboid) {
                float[] texCoords = ShapeUtil.vertFlipQuadTexCoords(ShapeUtil.getQuadTexCoords(1));
                for (int i = 0; i < texCoords.length; i += 2) {
                    texCoords[i] = texCoords[i] + tc[0];
                    int i2 = i + 1;
                    texCoords[i2] = texCoords[i2] + tc[1];
                    texCoords[i] = texCoords[i] / 16.0f;
                    int i3 = i + 1;
                    texCoords[i3] = texCoords[i3] / 16.0f;
                }
                Shape shape = ShapeUtil.filledQuad(-0.5f, -0.5f, 0.5f, 0.5f, 0.0f);
                ColouredShape cs = new ColouredShape(shape, Colour.white, null);
                this.blockItemShape = new TexturedShape(cs, texCoords, BlockFactory.texture);
                return;
            }
            float[] itc = new float[3 * 4 * 2];
            faceTexCoords(Face.Top, itc, 0);
            faceTexCoords(Face.North, itc, 8);
            faceTexCoords(Face.West, itc, 16);
            this.blockItemShape = new TexturedShape(BlockFactory.itemShape, itc, BlockFactory.texture);
        }

        private void faceTexCoords(@NonNull Face face, @NonNull float[] tc, int index) {
            int txco = face.ordinal() * 2;
            float bu = BlockFactory.sxtn * texCoords[txco];
            float bv = BlockFactory.sxtn * (texCoords[txco + 1] + 1);
            float tu = BlockFactory.sxtn * (texCoords[txco] + 1);
            float tv = BlockFactory.sxtn * texCoords[txco + 1];

            tc[index++] = bu;
            tc[index++] = bv;
            tc[index++] = bu;
            tc[index++] = tv;
            tc[index++] = tu;
            tc[index++] = bv;
            tc[index++] = tu;
            tc[index++] = tv;
        }

        /**
         * Adds a face to the {@link ShapeBuilder}
         *
         * @param f      which side
         * @param bx     block coordinate
         * @param by     block coordinate
         * @param bz     block coordinate
         * @param colour Vertex colour
         * @param sb
         */
        public void face(Face f, float bx, float by, float bz, int colour, ShapeBuilder sb) {
            if (!DoorBlock.isDoor(id) && !BedBlock.isBed(id) && !LadderBlock.isLadder(id)) {
                if (isCuboid || (f != Face.Top && f != Face.Bottom && f != Face.East && f != Face.South)) {
                    sb.ensureCapacity(4, 2);

                    // add vertices
                    System.arraycopy(f.verts, 0, sb.vertices, sb.vertexOffset, f.verts.length);
                    for (int i = 0; i < 4; i++) {
                        // translation
                        sb.vertices[sb.vertexOffset++] += bx;
                        sb.vertices[sb.vertexOffset++] += by;
                        sb.vertices[sb.vertexOffset++] += bz;

                        // colour
                        sb.colours[sb.colourOffset++] = colour;
                    }
                    // texcoords
                    int txco = f.ordinal() * 2;
                    float bu = BlockFactory.sxtn * texCoords[txco];
                    float bv = BlockFactory.sxtn * (texCoords[txco + 1] + 1);
                    float tu = BlockFactory.sxtn * (texCoords[txco] + 1);
                    float tv = BlockFactory.sxtn * texCoords[txco + 1];

                    if (id == BlockFactory.SLAB_ID && f != Face.Bottom) {
                        if (f == Face.Top) {
                            // shift all down
                            sb.vertices[sb.vertexOffset - 2] -= 0.5f;
                            sb.vertices[sb.vertexOffset - 5] -= 0.5f;
                            sb.vertices[sb.vertexOffset - 8] -= 0.5f;
                            sb.vertices[sb.vertexOffset - 11] -= 0.5f;
                        } else {
                            // shift top down
                            sb.vertices[sb.vertexOffset - 2] -= 0.5f;
                            sb.vertices[sb.vertexOffset - 8] -= 0.5f;

                            // half texcoords
                            bv *= 0.5f;
                            tv *= 0.5f;
                        }
                    }
                    if (!isCuboid) {
                        if (f == Face.North) {
                            sb.vertices[sb.vertexOffset - 1] -= 0.5f;
                            sb.vertices[sb.vertexOffset - 4] -= 0.5f;
                            sb.vertices[sb.vertexOffset - 7] -= 0.5f;
                            sb.vertices[sb.vertexOffset - 10] -= 0.5f;
                        }
                        if (f == Face.West) {
                            sb.vertices[sb.vertexOffset - 3] -= 0.5f;
                            sb.vertices[sb.vertexOffset - 6] -= 0.5f;
                            sb.vertices[sb.vertexOffset - 9] -= 0.5f;
                            sb.vertices[sb.vertexOffset - 12] -= 0.5f;
                        }
                    }
                    sb.texCoords[sb.texCoordOffset++] = bu;
                    sb.texCoords[sb.texCoordOffset++] = bv;
                    sb.texCoords[sb.texCoordOffset++] = bu;
                    sb.texCoords[sb.texCoordOffset++] = tv;
                    sb.texCoords[sb.texCoordOffset++] = tu;
                    sb.texCoords[sb.texCoordOffset++] = bv;
                    sb.texCoords[sb.texCoordOffset++] = tu;
                    sb.texCoords[sb.texCoordOffset++] = tv;

                    sb.relTriangle(0, 2, 1);
                    sb.relTriangle(2, 3, 1);

                    sb.vertexCount += 4;
                }
            }
        }
    }
}
