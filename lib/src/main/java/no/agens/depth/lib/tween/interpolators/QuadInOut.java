package no.agens.depth.lib.tween.interpolators;

import android.animation.TimeInterpolator;

/**
 * Created by danielzeller on 09.04.15.
 */
public class QuadInOut implements TimeInterpolator {
    @Override
    public float getInterpolation(float t) {
        if ((t*=2) < 1) return 0.5f*t*t;
        return -0.5f * ((--t)*(t-2) - 1);
    }
}