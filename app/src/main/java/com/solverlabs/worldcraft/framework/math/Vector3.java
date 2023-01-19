package com.solverlabs.worldcraft.framework.math;

import android.opengl.Matrix;

import androidx.annotation.NonNull;

import com.solverlabs.droid.rugl.util.FloatMath;


public class Vector3 {
    private static final float[] matrix = new float[16];
    private static final float[] inVec = new float[4];
    private static final float[] outVec = new float[4];
    public float x;
    public float y;
    public float z;

    public Vector3() {
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(@NonNull Vector3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public Vector3 cpy() {
        return new Vector3(this.x, this.y, this.z);
    }

    public Vector3 set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3 set(@NonNull Vector3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
        return this;
    }

    public Vector3 add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3 add(@NonNull Vector3 other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        return this;
    }

    public Vector3 sub(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vector3 sub(@NonNull Vector3 other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        return this;
    }

    public Vector3 mul(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }

    public float len() {
        return FloatMath.sqrt((this.x * this.x) + (this.y * this.y) + (this.z * this.z));
    }

    public Vector3 nor() {
        float len = len();
        if (len != 0.0f) {
            this.x /= len;
            this.y /= len;
            this.z /= len;
        }
        return this;
    }

    public Vector3 rotate(float angle, float axisX, float axisY, float axisZ) {
        inVec[0] = this.x;
        inVec[1] = this.y;
        inVec[2] = this.z;
        inVec[3] = 1.0f;
        Matrix.setIdentityM(matrix, 0);
        Matrix.rotateM(matrix, 0, angle, axisX, axisY, axisZ);
        Matrix.multiplyMV(outVec, 0, matrix, 0, inVec, 0);
        this.x = outVec[0];
        this.y = outVec[1];
        this.z = outVec[2];
        return this;
    }

    public float dist(@NonNull Vector3 other) {
        float distX = this.x - other.x;
        float distY = this.y - other.y;
        float distZ = this.z - other.z;
        return FloatMath.sqrt((distX * distX) + (distY * distY) + (distZ * distZ));
    }

    public float dist(float x, float y, float z) {
        float distX = this.x - x;
        float distY = this.y - y;
        float distZ = this.z - z;
        return FloatMath.sqrt((distX * distX) + (distY * distY) + (distZ * distZ));
    }

    public float distSquared(@NonNull Vector3 other) {
        float distX = this.x - other.x;
        float distY = this.y - other.y;
        float distZ = this.z - other.z;
        return (distX * distX) + (distY * distY) + (distZ * distZ);
    }

    public float distSquared(float x, float y, float z) {
        float distX = this.x - x;
        float distY = this.y - y;
        float distZ = this.z - z;
        return (distX * distX) + (distY * distY) + (distZ * distZ);
    }
}
