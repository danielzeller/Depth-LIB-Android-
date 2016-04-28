package no.agens.depth.lib.tween.interpolators;

import android.animation.TimeInterpolator;

/**
 * Created by danielzeller on 09.04.15.
 */
public class QuadIn implements TimeInterpolator {
    @Override
    public float getInterpolation(float t) {
        return t*t;
    }
}