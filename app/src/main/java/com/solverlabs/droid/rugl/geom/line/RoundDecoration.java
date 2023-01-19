package com.solverlabs.droid.rugl.geom.line;

import com.solverlabs.droid.rugl.util.geom.LineUtils;
import com.solverlabs.droid.rugl.util.geom.Vector2f;
import com.solverlabs.droid.rugl.util.geom.VectorUtils;

import java.util.List;


public class RoundDecoration implements LineCap, LineJoin {
    public float maxSegmentLength = 10.0f;

    @Override
    public void createVerts(Vector2f endPoint, Vector2f lineDirection, short leftIndex, short rightIndex, float width, List<Vector2f> verts, List<Short> indices) {
        float c = (float) ((3.141592653589793d * width) / 2.0d);
        int segments = (int) Math.ceil(c / this.maxSegmentLength);
        VectorUtils.rotate90(lineDirection);
        lineDirection.scale(width / 2.0f);
        float angleIncrement = (float) (3.141592653589793d / segments);
        Short left = new Short(leftIndex);
        Short prev = null;
        for (int i = 0; i < segments - 1; i++) {
            VectorUtils.rotate(lineDirection, angleIncrement);
            verts.add(Vector2f.add(endPoint, lineDirection, null));
            Short index = new Short((short) (verts.size() - 1));
            if (prev != null) {
                indices.add(left);
                indices.add(prev);
                indices.add(index);
            }
            prev = index;
        }
        if (prev != null) {
            indices.add(left);
            indices.add(prev);
            indices.add(new Short(rightIndex));
        }
    }

    @Override
    public void createVerts(Vector2f v1, Vector2f join, Vector2f v2, Vector2f corner, List<Vector2f> verts, List<Short> indices) {
        Short i1 = new Short((short) (verts.size() - 3));
        Short root = new Short((short) (verts.size() - 2));
        Short i2 = new Short((short) (verts.size() - 1));
        Vector2f n1 = Vector2f.sub(v1, join, null);
        n1.scale(0.5f);
        Vector2f n2 = Vector2f.sub(v2, join, null);
        n2.scale(0.5f);
        int ccw = LineUtils.relativeCCW(v1, join, v2);
        float angle = Vector2f.angle(n1, n2);
        float circ = (float) ((((n1.length() * 2.0f) * 3.141592653589793d) * angle) / 6.283185307179586d);
        int segments = (int) Math.ceil(circ / this.maxSegmentLength);
        float angleIncrement = (ccw * angle) / segments;
        Short prev = new Short((short) verts.size());
        verts.add(Vector2f.add(corner, n1, null));
        Line.addTriangle(root, i1, prev, ccw, indices);
        for (int i = 0; i < segments - 1; i++) {
            VectorUtils.rotate(n1, angleIncrement);
            verts.add(Vector2f.add(corner, n1, null));
            Short current = new Short((short) (verts.size() - 1));
            Line.addTriangle(root, prev, current, ccw, indices);
            prev = current;
        }
        verts.add(Vector2f.add(corner, n2, null));
        Short j = new Short((short) (verts.size() - 1));
        Line.addTriangle(root, prev, j, ccw, indices);
        Line.addTriangle(root, j, i2, ccw, indices);
    }
}