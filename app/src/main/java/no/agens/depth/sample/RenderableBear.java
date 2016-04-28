package no.agens.depth.sample;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import no.agens.depth.headers.Renderable;

public class RenderableBear extends Renderable {

    public static final int FRAME_DELAY = 2500;
    long lastFrameChange;
    Bitmap[] bitmaps;
    int bitmapIndex = 0;

    public RenderableBear(Bitmap[] bitmaps, float x, float y) {
        super(null, x, y);
        lastFrameChange = System.currentTimeMillis();
        this.bitmaps = bitmaps;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();

        canvas.drawBitmap(bitmaps[bitmapIndex], x + translationX / 2, y + translationY, null);
        canvas.restore();
    }

    @Override
    public void update(float deltaTime, float wind) {
        super.update(deltaTime, wind);
        if (lastFrameChange + FRAME_DELAY < System.currentTimeMillis()) {
            lastFrameChange = System.currentTimeMillis();
            bitmapIndex += 1;
            if (bitmapIndex == bitmaps.length)
                bitmapIndex = 0;
        }
    }

    public void destroy() {
        for (Bitmap bitmap : bitmaps) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }

    }
}
