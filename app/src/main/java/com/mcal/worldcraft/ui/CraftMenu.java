package com.mcal.worldcraft.ui;

import android.opengl.GLES10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mcal.droid.rugl.Game;
import com.mcal.droid.rugl.geom.ColouredShape;
import com.mcal.droid.rugl.geom.Shape;
import com.mcal.droid.rugl.geom.ShapeUtil;
import com.mcal.droid.rugl.geom.TexturedShape;
import com.mcal.droid.rugl.gl.StackedRenderer;
import com.mcal.droid.rugl.input.TapPad;
import com.mcal.droid.rugl.input.Touch;
import com.mcal.droid.rugl.text.Font;
import com.mcal.droid.rugl.text.Readout;
import com.mcal.droid.rugl.text.TextLayout;
import com.mcal.droid.rugl.text.TextShape;
import com.mcal.droid.rugl.util.Colour;
import com.mcal.droid.rugl.util.geom.BoundingRectangle;
import com.mcal.worldcraft.chunk.tile_entity.Inventory;
import com.mcal.worldcraft.chunk.tile_entity.TileEntity;
import com.mcal.worldcraft.factories.CraftFactory;
import com.mcal.worldcraft.factories.DescriptionFactory;
import com.mcal.worldcraft.factories.ItemFactory;

import java.util.ArrayList;

public class CraftMenu implements Touch.TouchListener {
    private static final float RATIO_Y = Game.screenHeight / Game.gameHeight;
    private static final float RATIO_X = Game.screenWidth / Game.gameWidth;
    private final CustomButton blocksButton;
    private final CustomButton craftTap;
    private final TapPad.Listener craftTapListener;
    private final CustomTapPad exitTap;
    private final TapPad.Listener exitTapListener;
    private final TapPad.Listener groupButtonListener;
    private final Inventory inventory;
    private final CustomButton itemsButton;
    private final CustomButton toolsButton;
    private final ArrayList<CraftMenuTapItem> craftItems = new ArrayList<>();
    private final ArrayList<CustomButton> buttonsGroup = new ArrayList<>();
    public BoundingRectangle bounds = new BoundingRectangle(0.0f, 0.0f, Game.gameWidth, Game.gameHeight);
    public int innerColour = Colour.packInt(148, 134, 123, 255);
    public int boundsColour = Colour.packFloat(0.0f, 0.0f, 0.0f, 0.8f);
    public BoundingRectangle scissorBound = new BoundingRectangle(150.0f, 10.0f, 300.0f, 462.0f);
    private ColouredShape boundShape;
    private TextShape descriptionShape;
    private ColouredShape fillTitleShape;
    private ColouredShape innerShape;
    private boolean isWorkBanch;
    private Readout materialCountShape;
    private boolean needToScroll;
    private float prevYpoint;
    private ColouredShape scissorBoundShape;
    private boolean show;
    private TextLayout textLayout;
    private TextShape titleTextShape;
    private Touch.Pointer touch;
    private float touchDelta;
    private int activeGroupNumber = 1;

    public CraftMenu(Inventory inventory) {
        this.inventory = inventory;
        initCraftItems(false);
        exitTap = new CustomTapPad(Game.gameWidth - 68.0f, Game.gameHeight - 68.0f, 60.0f, 60.0f, GUI.getFont(), "X");
        exitTapListener = new TapPad.Listener() {
            @Override
            public void onTap(TapPad pad) {
                showOrHide(false);
            }

            @Override
            public void onLongPress(TapPad pad) {
            }

            @Override
            public void onFlick(TapPad pad, int horizontal, int vertical) {
            }

            @Override
            public void onDoubleTap(TapPad pad) {
            }
        };
        exitTap.listener = exitTapListener;
        craftTap = new CustomButton(500.0f, 200.0f, 200.0f, 180.0f, DescriptionFactory.emptyText);
        craftTapListener = new TapPad.Listener() {
            @Override
            public void onTap(TapPad pad) {
                doCraft();
            }

            @Override
            public void onLongPress(TapPad pad) {
            }

            @Override
            public void onFlick(TapPad pad, int horizontal, int vertical) {
            }

            @Override
            public void onDoubleTap(TapPad pad) {
            }
        };
        craftTap.listener = craftTapListener;
        toolsButton = new CustomButton(25.0f, Game.gameHeight - 125.0f, 100.0f, 100.0f, "1");
        toolsButton.drawText = false;
        buttonsGroup.add(toolsButton);
        blocksButton = new CustomButton(25.0f, Game.gameHeight - 250.0f, 100.0f, 100.0f, "2");
        blocksButton.drawText = false;
        buttonsGroup.add(blocksButton);
        itemsButton = new CustomButton(25.0f, Game.gameHeight - 375.0f, 100.0f, 100.0f, "3");
        itemsButton.drawText = false;
        buttonsGroup.add(itemsButton);
        groupButtonListener = new TapPad.Listener() {
            @Override
            public void onTap(TapPad pad) {
                for (CustomButton button : buttonsGroup) {
                    button.setSelected(false);
                }
                pad.isSelected = true;
                if (pad instanceof CustomButton) {
                    activeGroupNumber = Integer.parseInt(((CustomButton) pad).getText());
                }
                initCraftItems(isWorkBanch);
            }

            @Override
            public void onLongPress(TapPad pad) {
            }

            @Override
            public void onFlick(TapPad pad, int horizontal, int vertical) {
            }

            @Override
            public void onDoubleTap(TapPad pad) {
            }
        };
        toolsButton.listener = groupButtonListener;
        blocksButton.listener = groupButtonListener;
        itemsButton.listener = groupButtonListener;
    }

    public void initCraftItems(boolean isWorkBanch) {
        craftItems.clear();
        float y = 402.0f;
        for (int i = 0; i < CraftFactory.CraftItem.values().length; i++) {
            CraftFactory.CraftItem craftItem = CraftFactory.CraftItem.values()[i];
            if (isWorkBanch && craftItem.getGroup() == activeGroupNumber) {
                addCraftItem(150.0f, y, craftItem);
                y -= 70.0f;
            } else if (!craftItem.isNeedWorkBanch() && craftItem.getGroup() == activeGroupNumber) {
                addCraftItem(150.0f, y, craftItem);
                y -= 70.0f;
            }
        }
        craftItems.get(0).isSelected = true;
    }

    private void addCraftItem(float x, float y, CraftFactory.CraftItem craftItem) {
        CraftMenuTapItem tapItem = new CraftMenuTapItem(craftItem, inventory, x, y);
        tapItem.checkItem();
        craftItems.add(tapItem);
    }

    public void doCraft() {
        if (getSelectedItem() != null) {
            final CraftFactory.CraftItem craftItem = getSelectedItem().getCraftItem();
            if (getSelectedItem().canBeCrafted) {
                int count = craftItem.getCount();
                for (int i = 0; i < count; i++) {
                    inventory.add(craftItem.getID());
                }
                for (int i = 0; i < craftItem.getMaterial().length; i++) {
                    for (int j = 0; j < craftItem.getMaterial()[i][1]; j++) {
                        inventory.decItem(craftItem.getMaterial()[i][0]);
                    }
                }
                for (CraftMenuTapItem item : craftItems) {
                    item.checkItem();
                }
            }
        }
    }

    public void draw(StackedRenderer sr) {
        if (show) {
            drawInnerBound(sr);
            drawBound(sr);
            exitTap.draw(sr);
            sr.render();
            craftTap.draw(sr);
            toolsButton.draw(sr);
            drawButtonTitle(sr, toolsButton.getX() + (toolsButton.getWidth() / 2.0f), toolsButton.getY() + (toolsButton.getHeight() / 2.0f), ItemFactory.Item.IronPick.itemShape);
            blocksButton.draw(sr);
            drawButtonTitle(sr, blocksButton.getX() + (blocksButton.getWidth() / 2.0f), blocksButton.getY() + (blocksButton.getHeight() / 2.0f), ItemFactory.Item.BrickBlock.itemShape);
            itemsButton.draw(sr);
            drawButtonTitle(sr, itemsButton.getX() + (itemsButton.getWidth() / 2.0f), itemsButton.getY() + (itemsButton.getHeight() / 2.0f), ItemFactory.Item.ItemsLabel.itemShape);
            drawCraftMaterial(sr);
            drawCraftMaterialCount(sr);
            GLES10.glEnable(3089);
            GLES10.glScissor((int) (150.0f * RATIO_X), (int) (10.0f * RATIO_Y), (int) (400.0f * RATIO_X), (int) (462.0f * RATIO_Y));
            for (int i = 0; i < craftItems.size(); i++) {
                craftItems.get(i).draw(sr, touchDelta);
            }
            sr.render();
            GLES10.glDisable(3089);
            drawTitle(sr);
            drawScissorBound(sr);
            drawItemDescription(sr);
            sr.render();
        }
    }

    private void drawTitle(StackedRenderer sr) {
        if (titleTextShape == null) {
            Font font = GUI.getFont();
            titleTextShape = font.buildTextShape("Craft", Colour.white);
            titleTextShape.translate(((280.0f - font.getStringLength(TileEntity.FURNACE_ID)) / 2.0f) + 450.0f + 20.0f, (Game.gameHeight - font.size) - 20.0f, 0.0f);
            Shape s = ShapeUtil.filledQuad(450.0f, Game.gameHeight - 8.0f, 730.0f, Game.gameHeight - 70.0f, 0.0f);
            fillTitleShape = new ColouredShape(s, Colour.packFloat(0.0f, 0.0f, 0.0f, 0.5f), null);
        }
        fillTitleShape.render(sr);
        sr.render();
        titleTextShape.render(sr);
    }

    private void drawInnerBound(StackedRenderer sr) {
        if (innerShape == null) {
            Shape is = ShapeUtil.innerQuad(bounds.x.getMin(), bounds.y.getMin(), bounds.x.getMax(), bounds.y.getMax(), bounds.y.getSpan(), 0.0f);
            innerShape = new ColouredShape(is, innerColour, null);
        }
        innerShape.render(sr);
    }

    private void drawBound(StackedRenderer sr) {
        if (boundShape == null) {
            Shape bs = ShapeUtil.innerQuad(bounds.x.getMin(), bounds.y.getMin(), bounds.x.getMax(), bounds.y.getMax(), 8.0f, 0.0f);
            boundShape = new ColouredShape(bs, boundsColour, null);
        }
        boundShape.render(sr);
    }

    private void drawScissorBound(StackedRenderer sr) {
        if (scissorBoundShape == null) {
            Shape bs = ShapeUtil.innerQuad(150.0f, 10.0f, 450.0f, 472.0f, 2.0f, 0.0f);
            scissorBoundShape = new ColouredShape(bs, Colour.black, null);
        }
        scissorBoundShape.render(sr);
    }

    private void drawCraftMaterial(StackedRenderer sr) {
        float xOffset;
        float yOffset;
        if (getSelectedItem() != null) {
            CraftFactory.CraftItem craftItem = getSelectedItem().getCraftItem();
            for (int i = 0; i < craftItem.getMaterial().length; i++) {
                ItemFactory.Item item = ItemFactory.Item.getItemByID(craftItem.getMaterial()[i][0]);
                if (i < 2) {
                    xOffset = i * 90;
                    yOffset = 0.0f;
                } else {
                    xOffset = (i - 2) * 90;
                    yOffset = -80.0f;
                }
                sr.pushMatrix();
                sr.translate(550.0f + xOffset, 340.0f + yOffset, 0.0f);
                sr.scale(50.0f, 50.0f, 1.0f);
                item.itemShape.render(sr);
                sr.popMatrix();
            }
            sr.render();
        }
    }

    private void drawCraftMaterialCount(StackedRenderer sr) {
        float xOffset;
        float yOffset;
        if (materialCountShape == null) {
            materialCountShape = new Readout(GUI.getFont(), Colour.white, " ", false, 2, 1);
        }
        if (getSelectedItem() != null) {
            CraftFactory.CraftItem craftItem = getSelectedItem().getCraftItem();
            for (int i = 0; i < craftItem.getMaterial().length; i++) {
                byte itemID = craftItem.getMaterial()[i][0];
                float totalCount = inventory.getItemTotalCount(itemID);
                float neededCount = totalCount + ((craftItem.getMaterial()[i][1] * 1.0f) / 10.0f);
                materialCountShape.updateValue(neededCount);
                if (i < 2) {
                    xOffset = i * 90;
                    yOffset = 0.0f;
                } else {
                    xOffset = (i - 2) * 90;
                    yOffset = -80.0f;
                }
                sr.pushMatrix();
                sr.translate(510.0f + xOffset, 295.0f + yOffset, 0.0f);
                if (totalCount >= craftItem.getMaterial()[i][1]) {
                    materialCountShape.colours = ShapeUtil.expand(Colour.white, materialCountShape.vertexCount());
                } else {
                    materialCountShape.colours = ShapeUtil.expand(Colour.darkgrey, materialCountShape.vertexCount());
                }
                materialCountShape.render(sr);
                sr.popMatrix();
                sr.render();
            }
        }
    }

    private void drawButtonTitle(@NonNull StackedRenderer sr, float x, float y, @NonNull TexturedShape itemShape) {
        sr.pushMatrix();
        sr.translate(x, y, 0.0f);
        sr.scale(60.0f, 60.0f, 1.0f);
        itemShape.render(sr);
        sr.popMatrix();
        sr.render();
    }

    private void drawItemDescription(StackedRenderer sr) {
        String description;
        Font font = GUI.getFont();
        if (getSelectedItem() != null && (description = getSelectedItem().getItem().getDescription()) != null && !description.equals(DescriptionFactory.emptyText)) {
            textLayout = new TextLayout(description, font, null, 230.0f, Colour.white);
            textLayout.textShape.scale(1.2f, 1.2f, 1.2f);
            textLayout.textShape.translate(500.0f, 150.0f, 0.0f);
            textLayout.textShape.render(sr);
        }
    }

    @Override
    public boolean pointerAdded(Touch.Pointer p) {
        if (touch == null && bounds.contains(p.x, p.y) && show) {
            touch = p;
            for (CraftMenuTapItem item : craftItems) {
                item.pointerAdded(touch);
            }
            exitTap.pointerAdded(touch);
            craftTap.pointerAdded(touch);
            toolsButton.pointerAdded(touch);
            blocksButton.pointerAdded(touch);
            itemsButton.pointerAdded(touch);
            if (scissorBound.contains(p.x, p.y)) {
                needToScroll = true;
                prevYpoint = touch.y;
                return true;
            }
            return true;
        }
        return false;
    }

    @Override
    public void pointerRemoved(Touch.Pointer p) {
        if (touch == p && touch != null) {
            for (CraftMenuTapItem item : craftItems) {
                item.pointerRemoved(touch);
                item.translateYOffset(touchDelta);
            }
            exitTap.pointerRemoved(touch);
            craftTap.pointerRemoved(touch);
            toolsButton.pointerRemoved(touch);
            blocksButton.pointerRemoved(touch);
            itemsButton.pointerRemoved(touch);
            touch = null;
            touchDelta = 0.0f;
            needToScroll = false;
        }
    }

    @Override
    public void reset() {
    }

    @Nullable
    private CraftMenuTapItem getSelectedItem() {
        for (int i = 0; i < craftItems.size(); i++) {
            if (craftItems.get(i).isSelected) {
                return craftItems.get(i);
            }
        }
        return null;
    }

    public void advance() {
        if (show) {
            exitTap.advance();
            craftTap.advance();
            toolsButton.advance();
            blocksButton.advance();
            itemsButton.advance();
            if (CraftMenuTapItem.isResetFocus) {
                for (CraftMenuTapItem item : craftItems) {
                    item.isSelected = false;
                }
                CraftMenuTapItem.isResetFocus = false;
            }
            if (needToScroll && craftItems.size() > 7) {
                touchDelta = touch.y - prevYpoint;
            }
            if (!craftItems.isEmpty() && craftItems.size() > 7) {
                normalizeScroll();
            }
        }
    }

    private void normalizeScroll() {
        CraftMenuTapItem lastItem = craftItems.get(craftItems.size() - 1);
        float bottomPoint = lastItem.getY();
        float topPoint = craftItems.get(0).bounds.y.getMax();
        if (touchDelta + topPoint < scissorBound.y.getMax()) {
            touchDelta = 0.0f;
            for (CraftMenuTapItem item : craftItems) {
                item.setYOffset(0.0f);
            }
        }
        if (touchDelta + bottomPoint > bounds.y.getMin() && craftItems.get(0).getYOffset() != 0.0f) {
            float yOffset = (70.0f * (craftItems.size() - 1)) - 394.0f;
            touchDelta = 0.0f;
            for (CraftMenuTapItem item2 : craftItems) {
                item2.setYOffset(yOffset);
            }
        }
    }

    public boolean isVisible() {
        return show;
    }

    public void setShow(boolean isShow) {
        show = isShow;
        for (CraftMenuTapItem item : craftItems) {
            item.setShown(isShow);
        }
    }

    public void showOrHide(boolean isWorkBanch) {
        setShow(!show);
        this.isWorkBanch = isWorkBanch;
        if (isVisible()) {
            initCraftItems(isWorkBanch);
        }
    }
}
