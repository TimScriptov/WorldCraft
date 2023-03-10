package com.mcal.worldcraft.utils;

import com.mcal.droid.rugl.util.geom.Vector3f;
import com.mcal.worldcraft.domain.Damagable;

public class FallDetector {
    public static final int BLOCK_COUNT_DAMAGE_ON_FALL = 3;
    private final Damagable damagable;
    private final Vector3f position = new Vector3f();
    private float startFallPositionY = 0.0f;

    public FallDetector(Damagable damagable) {
        this.damagable = damagable;
    }

    public int set(Vector3f position) {
        return set(position, false);
    }

    public int set(Vector3f position, boolean onGround) {
        if (position == null) {
            return 0;
        }
        int fellBlockCount = 0;
        if (position.y < this.position.y && startFallPositionY == 0.0f) {
            startFallPositionY = this.position.y;
        } else if (startFallPositionY != 0.0f && (onGround || position.y == this.position.y)) {
            if (startFallPositionY - position.y > 0.0f) {
                fellBlockCount = (int) Math.abs(position.y - startFallPositionY);
            }
            startFallPositionY = 0.0f;
        }
        this.position.set(position.x, position.y, position.z);
        if (!damagable.isDead()) {
            fell(fellBlockCount);
            return fellBlockCount;
        }
        return fellBlockCount;
    }

    private void fell(int blockCount) {
        if (blockCount > 3) {
            damagable.takeDamage(blockCount - 3);
        }
    }

    public void reset(Vector3f position) {
        this.position.set(position);
    }
}
