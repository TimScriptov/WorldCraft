package com.solverlabs.worldcraft.dialog.component;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;


public class MolotCheckBox extends CheckBox {
    public MolotCheckBox(Context context) {
        super(context);
        setMolotTypeFace();
    }

    public MolotCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMolotTypeFace();
    }

    public MolotCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setMolotTypeFace();
    }

    private void setMolotTypeFace() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/molot.otf");
            setTypeface(tf);
        }
    }
}