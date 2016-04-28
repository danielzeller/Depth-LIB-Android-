package no.agens.depth.lib.tween.interpolators;

import android.animation.TimeInterpolator;

/**
 * Created by danielzeller on 09.04.15.
 */
public class ElasticIn implements TimeInterpolator {
    @Override
    public float getInterpolation(float t) {
        float a = param_a;
        float p = param_p;
        if (t==0) return 0;  if (t==1) return 1; if (!setP) p=.3f;
        float s;
        if (!setA || a < 1) { a=1; s=p/4f; }
        else s = p/(2f*(float)Math.PI) * (float)Math.asin(1/a);
        return -(a*(float)Math.pow(2,10*(t-=1)) * (float)Math.sin( (t-s)*(2*Math.PI)/p ));
    }
    protected float param_a;
    protected float param_p;
    protected boolean setA = false;
    protected boolean setP = false;

    public ElasticIn a(float a) {
        param_a = a;
        this.setA = true;
        return this;
    }

    public ElasticIn p(float p) {
        param_p = p;
        this.setP = true;
        return this;
    }
}