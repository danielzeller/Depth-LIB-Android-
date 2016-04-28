package no.agens.depth.lib.tween.interpolators;

import android.animation.TimeInterpolator;

/**
 * Created by danielzeller on 14.04.15.
 */
public class CircIn implements TimeInterpolator {

    @Override
    public float getInterpolation(float t) {
        return  (float)Math.sqrt(1f - t*t);
    }
}
