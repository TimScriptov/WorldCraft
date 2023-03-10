package com.mcal.droid.rugl.geom;

import com.mcal.droid.rugl.gl.State;
import com.mcal.droid.rugl.texture.Texture;


public class TexturedShapeWelder extends ShapeWelder<TexturedShape> {
    private Texture texture = null;

    @Override
    public boolean addShape(TexturedShape s) {
        if (this.texture == null) {
            this.texture = s.texture;
        }
        if (s.texture.parent.id() == this.texture.parent.id()) {
            return super.addShape(s);
        }
        return false;
    }

    @Override
    public void clear() {
        super.clear();
        this.texture = null;
    }

    @Override
    public TexturedShape fuse() {
        float[] verts = new float[this.vertexCount * 3];
        short[] tris = new short[this.triangleCount];
        int[] colours = new int[this.vertexCount];
        float[] texCoords = new float[this.vertexCount * 2];
        int vi = 0;
        int ti = 0;
        int ci = 0;
        int tci = 0;
        State state = this.shapes.getFirst().state;
        while (!this.shapes.isEmpty()) {
            TexturedShape s = this.shapes.removeFirst();
            System.arraycopy(s.vertices, 0, verts, vi, s.vertices.length);
            System.arraycopy(s.colours, 0, colours, ci, s.colours.length);
            System.arraycopy(s.texCoords, 0, texCoords, tci, s.texCoords.length);
            System.arraycopy(s.indices, 0, tris, ti, s.indices.length);
            for (int i = 0; i < s.indices.length; i++) {
                int i2 = ti + i;
                tris[i2] = (short) (tris[i2] + (vi / 3));
            }
            vi += s.vertices.length;
            ti += s.indices.length;
            ci += s.colours.length;
            tci += s.texCoords.length;
        }
        TexturedShape ts = new TexturedShape(new ColouredShape(new Shape(verts, tris), colours, state), texCoords, this.texture);
        clear();
        return ts;
    }
}
