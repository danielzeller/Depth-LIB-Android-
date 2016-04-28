package no.agens.depth.lib.tween.interpolators;

import android.animation.TimeInterpolator;

/**
 * Created by danielzeller on 09.04.15.
 */
public class ElasticInOut implements TimeInterpolator {
    @Override
    public float getInterpolation(float t) {
        float a = param_a;
        float p = param_p;
        if (t==0) return 0;  if ((t*=2)==2) return 1; if (!setP) p=.3f*1.5f;
        float s;
        if (!setA || a < 1) { a=1; s=p/4; }
        else s = p/(2*(float)Math.PI) * (float)Math.asin(1/a);
        if (t < 1) return -.5f*(a*(float)Math.pow(2,10*(t-=1)) * (float)Math.sin( (t-s)*(2*(float)Math.PI)/p ));
        return a*(float)Math.pow(2,-10*(t-=1)) * (float)Math.sin( (t-s)*(2*(float)Math.PI)/p )*.5f + 1;
    }
    protected float param_a;
    protected float param_p;
    protected boolean setA = false;
    protected boolean setP = false;

    public ElasticInOut a(float a) {
        param_a = a;
        this.setA = true;
        return this;
    }

    public ElasticInOut p(float p) {
        param_p = p;
        this.setP = true;
        return this;
    }
}