package no.agens.depth.lib.tween.interpolators;

import android.animation.TimeInterpolator;

/**
 * Created by danielzeller on 09.04.15.
 */
public class ExpoInOut implements TimeInterpolator {
    @Override
    public float getInterpolation(float t) {
        if (t==0) return 0;
        if (t==1) return 1;
        if ((t*=2) < 1) return 0.5f * (float) Math.pow(2, 10 * (t - 1));
        return 0.5f * (-(float)Math.pow(2, -10 * --t) + 2);
    }
}