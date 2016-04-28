package no.agens.depth.lib.tween.interpolators;

import android.animation.TimeInterpolator;

/**
 * Created by danielzeller on 09.04.15.
 */
public class BackInOut implements TimeInterpolator {
    @Override
    public float getInterpolation(float t) {
        float s = param_s;
        if ((t*=2) < 1) return 0.5f*(t*t*(((s*=(1.525f))+1)*t - s));
        return 0.5f*((t-=2)*t*(((s*=(1.525f))+1)*t + s) + 2);
    }
    protected float param_s = 1.70158f;

    public BackInOut amount(float s) {
        param_s = s;
        return this;
    }
}