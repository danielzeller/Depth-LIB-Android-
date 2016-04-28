package no.agens.depth.lib.headers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by danielzeller on 01.10.14.
 */
public class Renderable {
    public float x;

    public void setY(float y) {
        this.y = y;
    }

    public float y;

    public float translationY;
    public float translationX;
    public Bitmap bitmap;

    public float scaleX = 1f;
    public float scaleY = 1f;

    public Renderable(Bitmap bitmap, float x, float y) {
        this.x = x;
        this.y = y;
        this.bitmap = bitmap;
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.drawBitmap(bitmap, x + translationX / 2, y + translationY, null);
        canvas.restore();
    }

    public void drawStretched(Canvas canvas, float parentWidth) {
        canvas.save();
        canvas.drawBitmap(bitmap, null, new RectF(x + translationX / 2, y + translationY, x + translationX / 2 + parentWidth, y + translationY + bitmap.getHeight()), null);
        canvas.restore();
    }

    public void setTranslationY(Float translationY) {
        this.translationY = translationY;
    }

    public float getTranslationY() {
        return translationY;
    }

    public void setTranslationY(float translationY) {
        this.translationY = translationY;
    }

    public float getTranslationX() {
        return translationX;
    }

    public void setTranslationX(float translationX) {
        this.translationX = translationX;
    }

    public void setScale(float scale, float scale1) {

    }

    public void update(float deltaTime, float wind) {

    }

    public void destroy() {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    public void pause() {

    }

    public void resume() {

    }
}

