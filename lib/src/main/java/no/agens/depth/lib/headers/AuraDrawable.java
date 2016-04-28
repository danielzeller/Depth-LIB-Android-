package no.agens.depth.lib.headers;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import no.agens.depth.lib.MathHelper;

public class AuraDrawable extends Renderable {
    private Drawable drawable;
    long lastflicker;

    public AuraDrawable(Drawable drawable, Rect position) {
        super(null, 0, 0);
        drawable.setBounds(position);
        this.drawable = drawable;
        lastflicker = System.currentTimeMillis();
    }

    @Override
    public void draw(Canvas canvas) {
        drawable.draw(canvas);
    }

    public void update(float deltaTime, float wind) {
        if (lastflicker + 50 < System.currentTimeMillis()) {
            drawable.setAlpha((int) (255 * (30f + (float) MathHelper.rand.nextInt(25)) / 100f));
            lastflicker = System.currentTimeMillis();
        }
    }

}
