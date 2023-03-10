package com.mcal.worldcraft.framework.math;

public class Rectangle {
    public final Vector2 lowerLeft;
    public float height;
    public float width;

    public Rectangle(float x, float y, float width, float height) {
        lowerLeft = new Vector2(x, y);
        this.width = width;
        this.height = height;
    }
}
