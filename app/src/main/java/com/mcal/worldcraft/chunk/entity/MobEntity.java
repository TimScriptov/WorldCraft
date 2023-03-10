package com.mcal.worldcraft.chunk.entity;

import androidx.annotation.Nullable;

import com.mcal.worldcraft.mob.Mob;
import com.mcal.worldcraft.mob.cow.Cow;
import com.mcal.worldcraft.mob.pig.Pig;
import com.mcal.worldcraft.mob.sheep.Sheep;
import com.mcal.worldcraft.mob.zombie.Zombie;
import com.mcal.worldcraft.nbt.Tag;

import java.util.HashMap;
import java.util.Map;

public class MobEntity extends Entity {
    private static final String HEALTH = "Health";
    private Map<String, Tag> extraTags;
    private short health;

    public MobEntity(String id) {
        super(id);
    }

    public MobEntity(String id, Tag tag) {
        super(id, tag);
        health = (Short) tag.findTagByName(HEALTH).getValue();
    }

    @Nullable
    private static Mob createMob(String id) {
        if (Cow.SAVE_ID.equals(id)) {
            return new Cow();
        }
        if (Pig.SAVE_ID.equals(id)) {
            return new Pig();
        }
        if (Sheep.SAVE_ID.equals(id)) {
            return new Sheep();
        }
        if (Zombie.SAVE_ID.equals(id)) {
            return new Zombie();
        }
        return null;
    }

    @Override
    protected Map<String, Tag> getExtraTags() {
        if (this.extraTags == null) {
            createExtraTags();
        } else {
            updateExtraTags();
        }
        return extraTags;
    }

    private void updateExtraTags() {
        Tag healthTag = extraTags.get(HEALTH);
        healthTag.setValue(health);
    }

    private void createExtraTags() {
        this.extraTags = new HashMap<>();
        this.extraTags.put(HEALTH, new Tag(Tag.Type.TAG_Short, HEALTH, this.health));
    }

    public short getHealth() {
        return health;
    }

    public void setHealth(short health) {
        this.health = health;
    }

    public Mob extractMob() {
        Mob mob = createMob(getId());
        mob.setPosition(getPosition());
        mob.setAngle(getYaw());
        mob.setHealthPoints(getHealth());
        return mob;
    }
}
