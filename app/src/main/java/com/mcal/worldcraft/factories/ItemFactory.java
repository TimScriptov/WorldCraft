package com.mcal.worldcraft.factories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mcal.droid.rugl.geom.BedBlock;
import com.mcal.droid.rugl.geom.ColouredShape;
import com.mcal.droid.rugl.geom.DoorBlock;
import com.mcal.droid.rugl.geom.LadderBlock;
import com.mcal.droid.rugl.geom.Shape;
import com.mcal.droid.rugl.geom.ShapeUtil;
import com.mcal.droid.rugl.geom.TexturedShape;
import com.mcal.droid.rugl.gl.GLUtil;
import com.mcal.droid.rugl.gl.State;
import com.mcal.droid.rugl.gl.enums.MagFilter;
import com.mcal.droid.rugl.gl.enums.MinFilter;
import com.mcal.droid.rugl.res.BitmapLoader;
import com.mcal.droid.rugl.res.ResourceLoader;
import com.mcal.droid.rugl.texture.Texture;
import com.mcal.droid.rugl.texture.TextureFactory;
import com.mcal.droid.rugl.util.Colour;
import com.mcal.worldcraft.etc.Food;
import com.mcal.worldcraft.ui.FoodBar;
import com.mcal.worldcraft.ui.HealthBar;

import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ItemFactory {
    public static final int[] WOOD_DOOR_TEX_COORDS = {11, 2};
    public static final int[] IRON_DOOR_TEX_COORDS = {12, 2};
    public static final int[] BED_TEX_COORDS = {13, 2};
    public static final int[] LADDER_TEX_COORDS = {3, 5};
    private static final int WOOD_WEAPON_DURABILITY = 60;
    private static final int STONE_WEAPON_DURABILITY = 132;
    private static final int IRON_WEAPON_DURABILITY = 251;
    private static final int GOLD_WEAPON_DURABILITY = 33;
    private static final int DIAMOND_WEAPON_DURABILITY = 1562;
    /**
     * For rendering items in the inventory and hand
     */
    public static State itemState;
    public static Texture itemTexture;
    public static Map<Byte, Food> FOOD_ID_LIST = new HashMap<>();
    public static Map<Byte, Integer> WEAPON_ID_LIST = new HashMap<>();

    static {
        for (Food food : Food.values()) {
            FOOD_ID_LIST.put(food.getId(), food);
        }
        WEAPON_ID_LIST.put(BlockFactory.WOOD_SWORD_ID, 4);
        WEAPON_ID_LIST.put(BlockFactory.GOLD_SWORD_ID, 4);
        WEAPON_ID_LIST.put(BlockFactory.STONE_SWORD_ID, 5);
        WEAPON_ID_LIST.put(BlockFactory.IRON_SWORD_ID, 6);
        WEAPON_ID_LIST.put(BlockFactory.DIAMOND_SWORD_ID, 7);
        WEAPON_ID_LIST.put(BlockFactory.WOOD_AXE_ID, 3);
        WEAPON_ID_LIST.put(BlockFactory.GOLD_AXE_ID, 3);
        WEAPON_ID_LIST.put(BlockFactory.STONE_AXE_ID, 4);
        WEAPON_ID_LIST.put(BlockFactory.IRON_AXE_ID, 5);
        WEAPON_ID_LIST.put(BlockFactory.DIAMOND_AXE_ID, 6);
        WEAPON_ID_LIST.put(BlockFactory.WOOD_PICK_ID, 2);
        WEAPON_ID_LIST.put(BlockFactory.GOLD_PICK_ID, 2);
        WEAPON_ID_LIST.put(BlockFactory.STONE_PICK_ID, 3);
        WEAPON_ID_LIST.put(BlockFactory.IRON_PICK_ID, 4);
        WEAPON_ID_LIST.put(BlockFactory.DIAMOND_PICK_ID, 5);
        WEAPON_ID_LIST.put(BlockFactory.WOOD_SHOVEL_ID, 1);
        WEAPON_ID_LIST.put(BlockFactory.GOLD_SHOVEL_ID, 1);
        WEAPON_ID_LIST.put(BlockFactory.STONE_SHOVEL_ID, 2);
        WEAPON_ID_LIST.put(BlockFactory.IRON_SHOVEL_ID, 3);
        WEAPON_ID_LIST.put(BlockFactory.DIAMOND_SHOVEL_ID, 4);
        itemState = GLUtil.typicalState.with(MinFilter.NEAREST, MagFilter.NEAREST);
    }

    /**
     * Loads the item texture
     */
    public static void loadTexture() {
        ResourceLoader.loadNow(new BitmapLoader("items.png") {
            @Override
            public void complete() {
                Set<Byte> fuels = FurnaceItemFactory.getFuelList();
                Set<Byte> materials = FurnaceItemFactory.getMaterialList();
                ItemFactory.itemTexture = TextureFactory.buildTexture(resource, true, false);
                if (ItemFactory.itemTexture != null) {
                    ItemFactory.itemState = ItemFactory.itemTexture.applyTo(ItemFactory.itemState);
                    for (Item item : Item.values()) {
                        item.initShape();
                        if (fuels.contains(item.id)) {
                            item.setIsFuel(true);
                        }
                        if (materials.contains(item.id)) {
                            item.setIsMaterial(true);
                        }
                    }
                    HealthBar.initShapes(ItemFactory.itemState);
                    FoodBar.initShapes(ItemFactory.itemState);
                }
            }
        });
    }

    /**
     * Stuff that can appear in your inventory
     */
    public enum Item {
        WoodSword(BlockFactory.WOOD_SWORD_ID, 0, 4, 1, ItemFactory.WOOD_WEAPON_DURABILITY, DescriptionFactory.Sword),
        WoodShovel(BlockFactory.WOOD_SHOVEL_ID, 0, 5, 1, ItemFactory.WOOD_WEAPON_DURABILITY, DescriptionFactory.Shovel),
        WoodPick(BlockFactory.WOOD_PICK_ID, 0, 6, 1, ItemFactory.WOOD_WEAPON_DURABILITY, DescriptionFactory.Pickaxe),
        WoodAxe(BlockFactory.WOOD_AXE_ID, 0, 7, 1, ItemFactory.WOOD_WEAPON_DURABILITY, DescriptionFactory.Axe),
        StoneSword(BlockFactory.STONE_SWORD_ID, 1, 4, 1, ItemFactory.STONE_WEAPON_DURABILITY, DescriptionFactory.Sword),
        StoneShovel(BlockFactory.STONE_SHOVEL_ID, 1, 5, 1, ItemFactory.STONE_WEAPON_DURABILITY, DescriptionFactory.Shovel),
        StonePick(BlockFactory.STONE_PICK_ID, 1, 6, 1, ItemFactory.STONE_WEAPON_DURABILITY, DescriptionFactory.Pickaxe),
        StoneAxe(BlockFactory.STONE_AXE_ID, 1, 7, 1, ItemFactory.STONE_WEAPON_DURABILITY, DescriptionFactory.Axe),
        IronSword(BlockFactory.IRON_SWORD_ID, 2, 4, 1, ItemFactory.IRON_WEAPON_DURABILITY, DescriptionFactory.Sword),
        IronShovel(BlockFactory.IRON_SHOVEL_ID, 2, 5, 1, ItemFactory.IRON_WEAPON_DURABILITY, DescriptionFactory.Shovel),
        IronPick(BlockFactory.IRON_PICK_ID, 2, 6, 1, ItemFactory.IRON_WEAPON_DURABILITY, DescriptionFactory.Pickaxe),
        IronAxe(BlockFactory.IRON_AXE_ID, 2, 7, 1, ItemFactory.IRON_WEAPON_DURABILITY, DescriptionFactory.Axe),
        DiamondSword(BlockFactory.DIAMOND_SWORD_ID, 3, 4, 1, ItemFactory.DIAMOND_WEAPON_DURABILITY, DescriptionFactory.Sword),
        DiamondShovel(BlockFactory.DIAMOND_SHOVEL_ID, 3, 5, 1, ItemFactory.DIAMOND_WEAPON_DURABILITY, DescriptionFactory.Shovel),
        DiamondPick(BlockFactory.DIAMOND_PICK_ID, 3, 6, 1, ItemFactory.DIAMOND_WEAPON_DURABILITY, DescriptionFactory.Pickaxe),
        DiamondAxe(BlockFactory.DIAMOND_AXE_ID, 3, 7, 1, ItemFactory.DIAMOND_WEAPON_DURABILITY, DescriptionFactory.Axe),
        GoldSword(BlockFactory.GOLD_SWORD_ID, 4, 4, 1, ItemFactory.GOLD_WEAPON_DURABILITY, DescriptionFactory.Sword),
        GoldShovel(BlockFactory.GOLD_SHOVEL_ID, 4, 5, 1, ItemFactory.GOLD_WEAPON_DURABILITY, DescriptionFactory.Shovel),
        GoldPick(BlockFactory.GOLD_PICK_ID, 4, 6, 1, ItemFactory.GOLD_WEAPON_DURABILITY, DescriptionFactory.Pickaxe),
        GoldAxe(BlockFactory.GOLD_AXE_ID, 4, 7, 1, ItemFactory.GOLD_WEAPON_DURABILITY, DescriptionFactory.Axe),
        Stick(BlockFactory.STICK_ID, 5, 3, 64, DescriptionFactory.Stick),
        Stone(BlockFactory.Block.Stone, 64, DescriptionFactory.Stone),
        DirtWithGrass(BlockFactory.Block.DirtWithGrass, 64, DescriptionFactory.emptyText),
        Dirt(BlockFactory.Block.Dirt, 64, DescriptionFactory.emptyText),
        Cobble(BlockFactory.Block.Cobble, 64, DescriptionFactory.emptyText),
        WoodenPlanks(BlockFactory.Block.WoodenPlanks, 64, DescriptionFactory.emptyText),
        Bedrock(BlockFactory.Block.Bedrock, 64, DescriptionFactory.emptyText),
        Water(BlockFactory.Block.Water, 64, DescriptionFactory.emptyText),
        StillWater(BlockFactory.Block.StillWater, 64, DescriptionFactory.emptyText),
        Lava(BlockFactory.Block.Lava, 64, DescriptionFactory.emptyText),
        StillLava(BlockFactory.Block.StillLava, 64, DescriptionFactory.emptyText),
        Sand(BlockFactory.Block.Sand, 64, DescriptionFactory.emptyText),
        Gravel(BlockFactory.Block.Gravel, 64, DescriptionFactory.emptyText),
        CoalOre(BlockFactory.Block.CoalOre.id, 7, 0, 64, DescriptionFactory.emptyText),
        ClayOre(BlockFactory.Block.ClayOre.id, 9, 3, 64, DescriptionFactory.emptyText),
        GoldOre(BlockFactory.Block.GoldOre, 64, DescriptionFactory.emptyText),
        IronOre(BlockFactory.Block.IronOre, 64, DescriptionFactory.emptyText),
        DiamondOre(BlockFactory.Block.DiamondOre, 64, DescriptionFactory.emptyText),
        Wood(BlockFactory.Block.Wood, 64, DescriptionFactory.Wood),
        Leaves(BlockFactory.Block.Leaves, 64, DescriptionFactory.emptyText),
        Sponge(BlockFactory.Block.Sponge, 64, DescriptionFactory.emptyText),
        Glass(BlockFactory.Block.Glass, 64, DescriptionFactory.emptyText),
        LapisLazulOre(BlockFactory.Block.LapisLazuliOre, 64, DescriptionFactory.emptyText),
        LapisLazuliBlock(BlockFactory.Block.LapisLazuliBlock, 64, DescriptionFactory.emptyText),
        Dispenser(BlockFactory.Block.Dispenser, 64, DescriptionFactory.emptyText),
        SandStone(BlockFactory.Block.SandStone, 64, DescriptionFactory.emptyText),
        NoteBlock(BlockFactory.Block.NoteBlock, 64, DescriptionFactory.emptyText),
        Wool(BlockFactory.Block.Wool, 64, DescriptionFactory.emptyText),
        Gold(BlockFactory.Block.GoldBlock, 64, DescriptionFactory.ItemBlock),
        Iron(BlockFactory.Block.IronBlock, 64, DescriptionFactory.ItemBlock),
        DoubleSlab(BlockFactory.Block.DoubleSlab, 64, DescriptionFactory.emptyText),
        Slab(BlockFactory.Block.Slab, 64, DescriptionFactory.emptyText),
        BrickBlock(BlockFactory.Block.BrickBlock, 64, DescriptionFactory.emptyText),
        Brick(BlockFactory.BRICK_ID, 6, 1, 64, DescriptionFactory.emptyText),
        TNT(BlockFactory.Block.TNT, 64, DescriptionFactory.emptyText),
        Bookshelf(BlockFactory.Block.Bookshelf, 64, DescriptionFactory.emptyText),
        MossStone(BlockFactory.Block.MossStone, 64, DescriptionFactory.emptyText),
        Obsidian(BlockFactory.Block.Obsidian, 64, DescriptionFactory.emptyText),
        Chest(BlockFactory.Block.Chest, 64, DescriptionFactory.Chest),
        DiamondBlock(BlockFactory.Block.DiamondBlock, 64, DescriptionFactory.ItemBlock),
        CraftingTable(BlockFactory.Block.CraftingTable, 64, DescriptionFactory.WorkBench),
        Farmland(BlockFactory.Block.Farmland, 64, DescriptionFactory.emptyText),
        Furnace(BlockFactory.Block.Furnace, 64, DescriptionFactory.Furnace),
        FurnaceActive(BlockFactory.Block.Furnace, 64, DescriptionFactory.emptyText),
        ClosedWoodDoor(BlockFactory.Block.ClosedWoodDoor, ItemFactory.WOOD_DOOR_TEX_COORDS[0], ItemFactory.WOOD_DOOR_TEX_COORDS[1], 1, DescriptionFactory.emptyText),
        ClosedIronDoor(BlockFactory.Block.ClosedIronDoor, ItemFactory.IRON_DOOR_TEX_COORDS[0], ItemFactory.IRON_DOOR_TEX_COORDS[1], 1, DescriptionFactory.emptyText),
        Bed(BlockFactory.Block.Bed, ItemFactory.BED_TEX_COORDS[0], ItemFactory.BED_TEX_COORDS[1], 1, DescriptionFactory.emptyText),
        RedstoneOre(BlockFactory.Block.RedstoneOre, 64, DescriptionFactory.emptyText),
        SnowyGrass(BlockFactory.Block.SnowyGrass, 64, DescriptionFactory.emptyText),
        Snow(BlockFactory.Block.Snow, 64, DescriptionFactory.emptyText),
        Ice(BlockFactory.Block.Ice, 64, DescriptionFactory.emptyText),
        Cactus(BlockFactory.Block.Cactus, 64, DescriptionFactory.emptyText),
        Clay(BlockFactory.Block.ClayOre, 64, DescriptionFactory.emptyText),
        Jukebox(BlockFactory.Block.Jukebox, 64, DescriptionFactory.emptyText),
        Pumpkin(BlockFactory.Block.Pumpkin, 64, DescriptionFactory.emptyText),
        Netherrack(BlockFactory.Block.Netherrack, 64, DescriptionFactory.emptyText),
        SoulSand(BlockFactory.Block.SoulSand, 64, DescriptionFactory.emptyText),
        GlowStone(BlockFactory.Block.GlowStone, 64, DescriptionFactory.emptyText),
        Torch(BlockFactory.Block.Torch, 64, DescriptionFactory.Torch),
        Ladder(BlockFactory.Block.Ladder, 64, DescriptionFactory.emptyText),
        Grass(BlockFactory.Block.Grass.id, 9, 0, 64, DescriptionFactory.emptyText),
        Flower(BlockFactory.Block.Flower, 64, DescriptionFactory.emptyText),
        WoolBlack(BlockFactory.Block.WoolBlack, 64, DescriptionFactory.emptyText),
        WoolGray(BlockFactory.Block.WoolGray, 64, DescriptionFactory.emptyText),
        WoolRed(BlockFactory.Block.WoolRed, 64, DescriptionFactory.emptyText),
        WoolPink(BlockFactory.Block.WoolPink, 64, DescriptionFactory.emptyText),
        WoolGreen(BlockFactory.Block.WoolGreen, 64, DescriptionFactory.emptyText),
        WoolLime(BlockFactory.Block.WoolLime, 64, DescriptionFactory.emptyText),
        WoolBrown(BlockFactory.Block.WoolBrown, 64, DescriptionFactory.emptyText),
        WoolYellow(BlockFactory.Block.WoolYellow, 64, DescriptionFactory.emptyText),
        WoolBlue(BlockFactory.Block.WoolBlue, 64, DescriptionFactory.emptyText),
        WoolLightBlue(BlockFactory.Block.WoolLightBlue, 64, DescriptionFactory.emptyText),
        WoolMagenta(BlockFactory.Block.WoolMagenta, 64, DescriptionFactory.emptyText),
        WoolCyan(BlockFactory.Block.WoolCyan, 64, DescriptionFactory.emptyText),
        WoolOrange(BlockFactory.Block.WoolOrange, 64, DescriptionFactory.emptyText),
        WoolLightGray(BlockFactory.Block.WoolLightGray, 64, DescriptionFactory.emptyText),
        NetherBrick(BlockFactory.Block.NetherBrick, 64, DescriptionFactory.emptyText),
        Obsidian2(BlockFactory.Block.Obsidian2, 64, DescriptionFactory.emptyText),
        IronIgnot(BlockFactory.IRON_INGOT_ID, 7, 1, 64, DescriptionFactory.Ignots),
        GoldIgnot(BlockFactory.GOLD_INGOT_ID, 7, 2, 64, DescriptionFactory.Ignots),
        StoneBrick(BlockFactory.Block.StoneBrick, 64, DescriptionFactory.emptyText),
        WaterMellon(BlockFactory.Block.Melon, 64, DescriptionFactory.emptyText),
        DiamondIgnot(BlockFactory.DIAMOND_INGOT_ID, 7, 3, 64, DescriptionFactory.Ignots),
        Emerald(BlockFactory.Block.Emerald, 64, DescriptionFactory.emptyText),
        Sandstone2(BlockFactory.Block.Sandstone2, 64, DescriptionFactory.emptyText),
        MossStone2(BlockFactory.Block.MossStone2, 64, DescriptionFactory.emptyText),
        StoneBrickMossy(BlockFactory.Block.StoneBrickMossy, 64, DescriptionFactory.emptyText),
        WoodPlankPine(BlockFactory.Block.WoodPlankPine, 64, DescriptionFactory.emptyText),
        WoodPlankJungle(BlockFactory.Block.WoodPlankJungle, 64, DescriptionFactory.emptyText),
        LeavesJungle(BlockFactory.Block.LeavesJungle, 64, DescriptionFactory.emptyText),
        ItemsLabel(BlockFactory.ITEMS_LABEL_ID, 10, 1, 1, DescriptionFactory.emptyText),
        RawPorkchop(BlockFactory.RAW_PORKCHOP_ID, 7, 5, 64, DescriptionFactory.emptyText),
        Beef(BlockFactory.RAW_BEEF_ID, 9, 6, 64, DescriptionFactory.emptyText),
        RottenFlesh(BlockFactory.ROTTEN_FLESH_ID, 11, 5, 64, DescriptionFactory.emptyText),
        Shears(BlockFactory.SHEARS_ID, 13, 5, 1, DescriptionFactory.Shears),
        Steak(BlockFactory.STEAK_ID, 10, 6, 64, DescriptionFactory.emptyText),
        CookedPorkchop(BlockFactory.COOKED_PORKCHOP_ID, 8, 5, 64, DescriptionFactory.emptyText);

        /**
         * The block type represented by this item
         */
        public final BlockFactory.Block block;
        public final int durability;
        public final int maxCountInStack;
        public byte id;
        /**
         * 1-unit high, origin-centered, shape with the appropriate texture
         */
        public TexturedShape itemShape;
        private String description;
        private boolean isTool;
        private boolean isUseAsFuel;
        private boolean isUseAsMaterials;
        private Integer s;
        private Integer t;

        Item(byte id, int s, int t, int maxCountInStack, String description) {
            this(id, s, t, maxCountInStack, 1, description);
        }

        Item(byte id, int s, int t, int maxCountInStack, int durability, String description) {
            this(null, maxCountInStack, durability, description);
            this.id = id;
            this.description = description;
            this.s = s;
            this.t = t;
            initShape();
        }

        Item(BlockFactory.Block block, int maxCountInStack, String description) {
            this(block, maxCountInStack, 1, description);
        }

        Item(BlockFactory.Block block, int maxCountInStack, int durability, String description) {
            s = null;
            t = null;
            isTool = false;
            isUseAsFuel = false;
            isUseAsMaterials = false;
            this.block = block;
            this.description = description;
            if (block != null) {
                id = block.id;
                itemShape = block.blockItemShape;
            }
            this.maxCountInStack = maxCountInStack;
            this.durability = durability;
            if (durability > 1) {
                isTool = true;
            }
        }

        Item(BlockFactory.Block block, int s, int t, int maxCountInStack, String description) {
            this(block, maxCountInStack, 1, description);
            this.s = s;
            this.t = t;
            initShape();
            if (DoorBlock.isDoor(block)) {
                TexturedShape itemShape = DoorBlock.getItemShape(id);
                block.blockItemShape = itemShape;
                this.itemShape = itemShape;
            } else if (BedBlock.isBed(block)) {
                TexturedShape itemShape = BedBlock.getItemShape(id);
                block.blockItemShape = itemShape;
                this.itemShape = itemShape;
            }
        }

        @NonNull
        @Contract("_ -> new")
        public static TexturedShape getShape(@NonNull int[] texCoords) {
            return getShape(texCoords[0], texCoords[1]);
        }

        @NonNull
        @Contract("_, _ -> new")
        public static TexturedShape getShape(int s, int t) {
            float[] texCoords = ShapeUtil.vertFlipQuadTexCoords(ShapeUtil.getQuadTexCoords(1));
            for (int i = 0; i < texCoords.length; i += 2) {
                texCoords[i] += s;
                texCoords[i + 1] += t;

                texCoords[i] /= 16;
                texCoords[i + 1] /= 16;
            }
            Shape shape = ShapeUtil.filledQuad(-0.5f, -0.5f, 0.5f, 0.5f, 0.0f);
            ColouredShape cs = new ColouredShape(shape, Colour.white, ItemFactory.itemState);
            return new TexturedShape(cs, texCoords, ItemFactory.itemTexture);
        }

        @Nullable
        public static Item getItemByID(byte id) {
            if (DoorBlock.isDoor(id)) {
                id = DoorBlock.getClosedState(id);
            } else if (BedBlock.isBed(id)) {
                id = BedBlock.getDefaultState(id);
            } else if (LadderBlock.isLadder(id)) {
                id = LadderBlock.getDefaultState(id);
            }
            if (id == BlockFactory.FURNACE_ACTIVE_ID) {
                id = BlockFactory.FURNACE_ID;
            }
            Item[] values = values();
            for (int i = 0; i < values.length; i++) {
                if (values[i].id == id) {
                    return values[i];
                }
            }
            return null;
        }

        public void initShape() {
            if (s != null && t != null) {
                itemShape = getShape(s, t);
            }
        }

        @NonNull
        @Contract(value = " -> new", pure = true)
        public int[] getTexCoords() {
            return new int[]{s, t};
        }

        public boolean isTool() {
            return isTool;
        }

        public void setIsFuel(boolean value) {
            isUseAsFuel = value;
        }

        public void setIsMaterial(boolean value) {
            isUseAsMaterials = value;
        }

        public boolean isUseAsFuel() {
            return isUseAsFuel;
        }

        public boolean isUseAsMaterials() {
            return isUseAsMaterials;
        }

        public String getDescription() {
            return description;
        }
    }
}
