package no.agens.depth.lib.tween.interpolators;

import android.animation.TimeInterpolator;

/**
 * Created by danielzeller on 09.04.15.
 */
public class BackOut implements TimeInterpolator {
    @Override
    public float getInterpolation(float t) {
        float s = param_s;
        return (t-=1)*t*((s+1)*t + s) + 1;
    }
    protected float param_s = 1.70158f;

    public BackOut amount(float s) {
        param_s = s;
        return this;
    }
}