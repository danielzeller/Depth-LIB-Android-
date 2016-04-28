package no.agens.depth.lib.tween.interpolators;

import android.animation.TimeInterpolator;

/**
 * Created by danielzeller on 30.05.14.
 */
public class QuintIn implements TimeInterpolator {
    @Override
    public float getInterpolation(float t) {
        return t*t*t*t*t;
    }
}