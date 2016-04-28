package no.agens.depth.lib.headers;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Xfermode;
import no.agens.depth.lib.MathHelper;

public class NoiseEffect extends Renderable {

    private Paint paint = new Paint();
    BitmapShader shader;
    Matrix matrix;
    float scale;

    public NoiseEffect(Bitmap bitmap, int grainFPS, float scale) {
        super(bitmap, 0, 0);
        shader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        matrix = new Matrix();

        shader.setLocalMatrix(matrix);
        paint.setShader(shader);
        paint.setAlpha(144);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
        lastGrainOffset = System.currentTimeMillis();
        this.grainFPS = grainFPS;
        this.scale=scale;
    }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawPaint(paint);
    }

    long lastGrainOffset;
    private int grainFPS;

    @Override
    public void update(float deltaTime, float wind) {
        if (lastGrainOffset + grainFPS < System.currentTimeMillis()) {
            matrix.reset();
            matrix.setScale(scale, scale);
            matrix.postTranslate(MathHelper.randomRange(-bitmap.getWidth() * 10f, bitmap.getWidth() * 10f), MathHelper.randomRange(-bitmap.getHeight() * 10f, bitmap.getHeight() * 10f));
            shader.setLocalMatrix(matrix);
            lastGrainOffset = System.currentTimeMillis();

        }
    }

    private static final Xfermode[] sModes = {
            new PorterDuffXfermode(PorterDuff.Mode.CLEAR),
            new PorterDuffXfermode(PorterDuff.Mode.SRC),
            new PorterDuffXfermode(PorterDuff.Mode.DST),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
            new PorterDuffXfermode(PorterDuff.Mode.DST_IN),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.XOR),
            new PorterDuffXfermode(PorterDuff.Mode.DARKEN),
            new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
            new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
            new PorterDuffXfermode(PorterDuff.Mode.SCREEN)
    };

    public void setNoiseIntensity(float noiseIntensity) {
        paint.setAlpha((int) (255f * noiseIntensity));
    }
}
