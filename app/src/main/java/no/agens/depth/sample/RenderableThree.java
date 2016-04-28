package no.agens.depth.sample;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

import no.agens.depth.headers.Renderable;

/**
 * Created by danielzeller on 01.10.14.
 */
public class RenderableThree extends Renderable {
    private final float[] drawingVerts = new float[TOTAL_SLICES_COUNT * 2];
    private final float[] staticVerts = new float[TOTAL_SLICES_COUNT * 2];
    private static final int HORIZONTAL_SLICES = 1;
    private static final int VERTICAL_SLICES = 95;
    private static final int TOTAL_SLICES_COUNT = (HORIZONTAL_SLICES + 1) * (VERTICAL_SLICES + 1);
    private Paint p = new Paint();
    private float offsetInPercent;

    private Path pathLeft = new Path();
    private Path pathRight = new Path();
    private SpringSystem springSystem = SpringSystem.create();
    private boolean isBounceAnimatin = false;
    private Paint paint = new Paint();

    public RenderableThree(Bitmap bitmap, float x, float y, float alpha) {
        super(bitmap, x, y );
        p.setColor(Color.BLACK);
        p.setStrokeWidth(6);
        p.setStyle(Paint.Style.STROKE);
        createVerts();
        paint.setAlpha((int) (255*alpha));
    }

    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    private void createVerts() {

        float xDimesion = (float) bitmap.getWidth();
        float yDimesion = (float) bitmap.getHeight();

        int index = 0;

        for (int y = 0; y <= VERTICAL_SLICES; y++) {
            float fy = yDimesion * y / VERTICAL_SLICES;
            for (int x = 0; x <= HORIZONTAL_SLICES; x++) {
                float fx = xDimesion * x / HORIZONTAL_SLICES;
                setXY(drawingVerts, index, fx, fy);
                setXY(staticVerts, index, fx, fy);
                index += 1;
            }
        }
    }

    public void setXY(float[] array, int index, float x, float y) {
        array[index * 2 + 0] = x;
        array[index * 2 + 1] = y;
    }

    public void setXA(float[] array, int index, float x) {
        array[index * 2 + 0] = x;
    }

    public void setYA(float[] array, int index, float y) {
        array[index * 2 + 1] = staticVerts[index * 2 + 1] + y;
    }

    @Override
    public void draw(Canvas canvas) {
        createPath();
//        alphaCanvas.drawPath(pathLeft, debugPaint);
//        alphaCanvas.drawPath(pathRight, debugPaint);
        canvas.save();
        if (scaleX != 1.f || scaleY != 1f) {
            canvas.scale(scaleX, scaleY, x + bitmap.getWidth() / 2, y + bitmap.getHeight());
        }
        canvas.drawBitmapMesh(bitmap, HORIZONTAL_SLICES, VERTICAL_SLICES, drawingVerts, 0, null, 0, paint);
        canvas.restore();
    }


    private void createPath() {
        pathLeft.reset();
        pathLeft.moveTo(x, y + bitmap.getHeight());
        pathLeft.cubicTo(x, y + bitmap.getHeight(), x, y, x + bitmap.getWidth() * 1.5f * offsetInPercent, y);
        pathRight.reset();
        pathRight.moveTo(x + bitmap.getWidth(), y + bitmap.getHeight());
        pathRight.cubicTo(x + bitmap.getWidth(), y + bitmap.getHeight(), x + bitmap.getWidth(), y + bitmap.getWidth() * 0.3f * offsetInPercent, x + bitmap.getWidth() + bitmap.getWidth() / 2 * offsetInPercent, y + bitmap.getWidth() * 0.8f * offsetInPercent);
        matchVertsToPath();
    }

    private void matchVertsToPath() {
        PathMeasure pmLeft = new PathMeasure(pathLeft, false);
        PathMeasure pmRight = new PathMeasure(pathRight, false);
        float[] coords = new float[2];
        for (int i = 0; i < staticVerts.length / 2; i++) {

            float yIndexValue = staticVerts[i * 2 + 1];
            float xIndexValue = staticVerts[i * 2];
            if (xIndexValue == 0) {
                float percentOffsetY = (0.000001f + yIndexValue) / bitmap.getHeight();
                pmLeft.getPosTan(pmLeft.getLength() * (1f - percentOffsetY), coords, null);
                setXY(drawingVerts, i, coords[0], coords[1]);
            } else {
                float percentOffsetY = (0.000001f + yIndexValue) / bitmap.getHeight();
                pmRight.getPosTan(pmRight.getLength() * (1f - percentOffsetY), coords, null);
                setXY(drawingVerts, i, coords[0], coords[1]);

            }
        }
    }

    public void setOffsetPercent(float offset) {
        if (!isBounceAnimatin)
            offsetInPercent = offset ;
    }

    Spring spring;

    public void bounceBack() {
        cancelBounce();
        isBounceAnimatin = true;
        spring = springSystem.createSpring();
        spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(150, 4));
        final float offsetAtStart = offsetInPercent;
        spring.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                offsetInPercent = offsetAtStart - (offsetAtStart * value);
            }

            @Override
            public void onSpringAtRest(Spring spring) {
                super.onSpringAtRest(spring);
                isBounceAnimatin = false;
            }
        });
        spring.setEndValue(1);
    }

    public boolean isBounceAnimatin() {
        return isBounceAnimatin;
    }

    public void cancelBounce() {
        if (spring != null)
            spring.destroy();
        isBounceAnimatin = false;
    }

    @Override
    public void update(float deltaTime, float wind) {
        super.update(deltaTime, wind);
        setOffsetPercent(wind/100f);
    }
}
