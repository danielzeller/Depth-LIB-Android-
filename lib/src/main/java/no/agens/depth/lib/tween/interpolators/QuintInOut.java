package no.agens.depth.lib.tween.interpolators;

import android.animation.TimeInterpolator;

/**
 * Created by danielzeller on 29.05.14.
 */
public class QuintInOut implements TimeInterpolator {
    @Override
    public float getInterpolation(float t) {
        if ((t*=2) < 1) return 0.5f*t*t*t*t*t;
        return 0.5f*((t-=2)*t*t*t*t + 2);
    }
}
