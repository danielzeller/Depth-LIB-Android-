package no.agens.depth.lib.tween.interpolators;

import android.animation.TimeInterpolator;

/**
 * Created by danielzeller on 09.04.15.
 */
public class SineInOut implements TimeInterpolator {
    @Override
    public float getInterpolation(float t) {
        return -0.5f * ((float) Math.cos(Math.PI*t) - 1);
    }
}