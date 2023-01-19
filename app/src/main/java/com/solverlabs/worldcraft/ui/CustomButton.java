package com.solverlabs.worldcraft.ui;

import com.solverlabs.droid.rugl.geom.ColouredShape;
import com.solverlabs.droid.rugl.geom.Shape;
import com.solverlabs.droid.rugl.geom.ShapeUtil;
import com.solverlabs.droid.rugl.gl.StackedRenderer;
import com.solverlabs.droid.rugl.gl.State;
import com.solverlabs.droid.rugl.input.TapPad;
import com.solverlabs.droid.rugl.text.Font;
import com.solverlabs.droid.rugl.text.TextShape;
import com.solverlabs.droid.rugl.util.Colour;

import org.apache.commons.compress.archivers.cpio.CpioConstants;


public class CustomButton extends TapPad {
    private final Font font;
    private final float height;
    private final String text;
    private final float width;
    private final float x;
    private final float y;
    public boolean drawText;
    public boolean isStroke;
    private ColouredShape buttonBottomBound;
    private ColouredShape buttonLeftBound;
    private ColouredShape buttonRightBound;
    private ColouredShape buttonUpBound;
    private ColouredShape innerShape;
    private TextShape textShape;

    public CustomButton(float x, float y, float width, float height, String text) {
        super(x, y, width, height);
        this.drawText = true;
        this.isStroke = false;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.font = GUI.getFont();
        this.text = text;
    }

    @Override
    public void draw(StackedRenderer sr) {
        if (this.buttonBottomBound == null) {
            Shape s = ShapeUtil.line(3.0f, 0.0f, 0.0f, this.width, 0.0f);
            this.buttonUpBound = new ColouredShape(s, Colour.white, (State) null);
            this.buttonBottomBound = new ColouredShape(s, Colour.darkgrey, (State) null);
            Shape s2 = ShapeUtil.line(3.0f, 0.0f, 0.0f, 0.0f, this.height);
            this.buttonLeftBound = new ColouredShape(s2, Colour.withAlphai(Colour.white, (int) CpioConstants.C_IWUSR), (State) null);
            this.buttonRightBound = new ColouredShape(s2, Colour.withAlphai(Colour.darkgrey, (int) CpioConstants.C_IWUSR), (State) null);
        }
        sr.pushMatrix();
        sr.translate(this.x, this.y, 0.0f);
        this.buttonBottomBound.render(sr);
        sr.translate(0.0f, this.height, 0.0f);
        this.buttonUpBound.render(sr);
        sr.popMatrix();
        sr.pushMatrix();
        sr.translate(this.x, this.y, 0.0f);
        this.buttonLeftBound.render(sr);
        sr.translate(this.width, 0.0f, 0.0f);
        this.buttonRightBound.render(sr);
        sr.popMatrix();
        sr.render();
        if (this.textShape != null && this.drawText) {
            this.textShape.render(sr);
        }
        if (this.isSelected || this.touch != null) {
            drawInnerBound(sr);
        }
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public String getText() {
        return this.text;
    }

    private void drawInnerBound(StackedRenderer sr) {
        if (this.innerShape == null) {
            if (!this.isStroke) {
                Shape is = ShapeUtil.innerQuad(this.x, this.y, this.x + this.width, this.y + this.height, this.height, 0.0f);
                this.innerShape = new ColouredShape(is, Colour.withAlphai(Colour.white, (int) CpioConstants.C_IWUSR), (State) null);
            } else {
                Shape is2 = ShapeUtil.innerQuad(this.x, this.y, this.x + this.width, this.y + this.height, 4.0f, 0.0f);
                this.innerShape = new ColouredShape(is2, Colour.white, (State) null);
            }
        }
        this.innerShape.render(sr);
    }

    public void setSelected(boolean select) {
        this.isSelected = select;
    }
}
