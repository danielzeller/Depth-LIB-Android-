package no.agens.depth.lib.headers;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.animation.LinearInterpolator;

public class PathBitmapMesh {
    public float[] drawingVerts;
    public final float[] staticVerts;
    private static final int HORIZONTAL_SLICES = 6;
    private static final int VERTICAL_SLICES = 1;
    private int totaolSlicesCount;
    int horizontalSlices, verticalSlices;
    public Bitmap bitmap;

    public PathBitmapMesh(int horizontalSlices, int verticalSlices, Bitmap bitmap, int animDuration) {
        totaolSlicesCount = (HORIZONTAL_SLICES + 1) * (VERTICAL_SLICES + 1);
        drawingVerts = new float[totaolSlicesCount * 2];
        staticVerts = new float[totaolSlicesCount * 2];
        this.horizontalSlices = horizontalSlices;
        this.verticalSlices = verticalSlices;
        this.bitmap = bitmap;
        createVerts();
        startWaveAnim(bitmap, horizontalSlices, animDuration);
    }

    private void startWaveAnim(Bitmap bitmap, float waves, int animDuration) {
        pathOffset = ValueAnimator.ofFloat(0, ((bitmap.getWidth() / waves) * 2f) / bitmap.getWidth()).setDuration(animDuration);
        pathOffset.setRepeatCount(ValueAnimator.INFINITE);
        pathOffset.setRepeatMode(ValueAnimator.RESTART);
        pathOffset.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                pathOffsetPercent = (float) animation.getAnimatedValue();
            }
        });
        pathOffset.setInterpolator(new LinearInterpolator());
        pathOffset.start();
    }

    public ValueAnimator pathOffset;

    public void destroy() {
        pathOffset.cancel();
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
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

    Paint paint = new Paint();


    public void setXY(float[] array, int index, float x, float y) {
        array[index * 2 + 0] = x;
        array[index * 2 + 1] = y;
    }

    public void matchVertsToPath(Path path, float bottomCoord, float extraOffset) {
        PathMeasure pm = new PathMeasure(path, false);

        for (int i = 0; i < staticVerts.length / 2; i++) {

            float yIndexValue = staticVerts[i * 2 + 1];
            float xIndexValue = staticVerts[i * 2];


            float percentOffsetX = (0.000001f + xIndexValue) / bitmap.getWidth();
            float percentOffsetX2 = (0.000001f + xIndexValue) / (bitmap.getWidth() + extraOffset);
            percentOffsetX2 += pathOffsetPercent;
            pm.getPosTan(pm.getLength() * (1f - percentOffsetX), coords, null);
            pm.getPosTan(pm.getLength() * (1f - percentOffsetX2), coords2, null);

            if (yIndexValue == 0) {
                setXY(drawingVerts, i, coords[0], coords2[1]);
            } else {
                float desiredYCoord = bottomCoord;
                setXY(drawingVerts, i, coords[0], desiredYCoord);

            }
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }


    public float[] coords = new float[2];
    public float[] coords2 = new float[2];
    public float pathOffsetPercent = 1;

    public void draw(Canvas canvas) {
        canvas.drawBitmapMesh(bitmap, HORIZONTAL_SLICES, VERTICAL_SLICES, drawingVerts, 0, null, 0, paint);
    }

    public void pause() {
        pathOffset.pause();
    }

    public void resume() {
        pathOffset.resume();
    }
}
