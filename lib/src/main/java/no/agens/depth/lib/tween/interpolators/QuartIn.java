package no.agens.depth.lib.tween.interpolators;

import android.animation.TimeInterpolator;

/**
 * Created by danielzeller on 09.04.15.
 */
public class QuartIn implements TimeInterpolator {
    @Override
    public float getInterpolation(float t) {
        return t*t*t*t;
    }
}