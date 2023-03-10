package com.mcal.worldcraft.ui;

import androidx.annotation.NonNull;

import com.mcal.droid.rugl.util.Colour;
import com.mcal.worldcraft.Player;
import com.mcal.worldcraft.World;

public class SleepBar extends FadeInOutBar {
    private static final long FADE_OUT_DURATION = 5000;
    private static final int SLEEP_BAR_COLOR = Colour.packFloat(0.0f, 0.0f, 0.0f, 1.0f);
    private final Player player;

    public SleepBar(Player player, @NonNull final World world) {
        super(SLEEP_BAR_COLOR, FADE_OUT_DURATION, 1.0f, FadeInOutBar.FadingType.FadeOutThanFadeIn);
        this.player = player;
        setOnChangedListener(world::setDayTimePeriod);
    }

    @Override
    public void advance(float delta) {
        setFadingStartedAt(player.getKeptDownAt());
        super.advance(delta);
    }
}
