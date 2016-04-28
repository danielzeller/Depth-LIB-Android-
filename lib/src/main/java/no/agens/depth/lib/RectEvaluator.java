package no.agens.depth.lib;

import android.animation.TypeEvaluator;
import android.graphics.Rect;

public class RectEvaluator implements TypeEvaluator<Rect> {

    @Override
    public Rect evaluate(float fraction, Rect startValue, Rect endValue) {
        Rect rect = new Rect(startValue.left + (int) ((endValue.left - startValue.left) * fraction),
                startValue.top + (int) ((endValue.top - startValue.top) * fraction),
                startValue.right + (int) ((endValue.right - startValue.right) * fraction),
                startValue.bottom + (int) ((endValue.bottom - startValue.bottom) * fraction));

        return rect;
    }

}
