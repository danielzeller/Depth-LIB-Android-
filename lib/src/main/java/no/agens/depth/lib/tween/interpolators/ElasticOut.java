package no.agens.depth.lib.tween.interpolators;

import android.animation.TimeInterpolator;

/**
 * Created by danielzeller on 09.04.15.
 */
public class ElasticOut implements TimeInterpolator {

    protected float param_a;
    protected float param_p;
    protected boolean setA = false;
    protected boolean setP = false;


    @Override
    public float getInterpolation(float t) {
        float a = param_a;
        float p = param_p;
        if (t==0) return 0;  if (t==1) return 1; if (!setP) p=.3f;
        float s;
        if (!setA || a < 1) { a=1; s=p/4; }
        else s = p/(2*(float)Math.PI) * (float)Math.asin(1/a);
        return a*(float)Math.pow(2,-10*t) * (float)Math.sin( (t-s)*(2*Math.PI)/p ) + 1;
    }

    public ElasticOut a(float a) {
        param_a = a;
        this.setA = true;
        return this;
    }

    public ElasticOut p(float p) {
        param_p = p;
        this.setP = true;
        return this;
    }
}