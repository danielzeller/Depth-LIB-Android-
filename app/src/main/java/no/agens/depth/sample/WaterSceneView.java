package no.agens.depth.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import no.agens.depth.sample.R;
import no.agens.depth.headers.NoiseEffect;
import no.agens.depth.headers.Renderable;
import no.agens.depth.tween.FrameRateCounter;

/**
 * Created by danielzeller on 01.10.14.
 */
public class WaterSceneView extends View {


    private Renderable[] renderables;
    private Water water;
    private NoiseEffect noiseScratchEffect;
    private NoiseEffect noise;

    public WaterSceneView(Context context) {
        super(context);

    }

    public WaterSceneView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WaterSceneView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (renderables == null) {
            init();
        }
    }

    private void init() {
        renderables = new Renderable[4];
        Bitmap waterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.water);
        Bitmap foam = BitmapFactory.decodeResource(getResources(), R.drawable.foam);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        water = new Water(waterBitmap, foam, getYCoordByPercent(0.65f), getYCoordByPercent(1f), getXCoordByPercent(1f), 6);
        renderables[0] = water;
        Bitmap aura = BitmapFactory.decodeResource(getResources(), R.drawable.sun_aura);
        renderables[1] = new Renderable(aura, getXCoordByPercent(0.5f), getYCoordByPercent(0.35f));

        Bitmap noiseScratch = BitmapFactory.decodeResource(getResources(), R.drawable.noise_scratch);
        Bitmap noiseReg = BitmapFactory.decodeResource(getResources(), R.drawable.noise);

        noiseScratchEffect = new NoiseEffect(noiseScratch, 100, 2f);
        renderables[2] = noiseScratchEffect;
        noise = new NoiseEffect(noiseReg, 30, 1.5f);
        renderables[3] = noise;
        setNoiseIntensity(0.5f);
        setWaveHeight(50);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destroyResources();
    }

    private void destroyResources() {
        for (Renderable renderable : renderables)
            renderable.destroy();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (renderables == null && getWidth() != 0)
            init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float deltaTime = FrameRateCounter.timeStep();

        for (Renderable renderable : renderables) {
            renderable.draw(canvas);
            renderable.update(deltaTime, 0);
        }
        if (!pasuse)
            invalidate();
    }

    public void setPause(boolean pause) {
//        this.pasuse = pause;
//        if (!pause) {
//            FrameRateCounter.timeStep();
//            invalidate();
//            for (Renderable renderable : renderables)
//                renderable.resume();
//        } else {
//            for (Renderable renderable : renderables)
//                renderable.pause();
//        }
    }

    private boolean pasuse = false;

    private float getYCoordByPercent(float percent) {
        return getHeight() * percent;
    }

    private float getXCoordByPercent(float percent) {
        return getWidth() * percent;
    }

    public void setWaveHeight(float height) {
        water.setWaveHeight(height);
    }

    public void setNoiseIntensity(float noiseAmount) {
        noiseScratchEffect.setNoiseIntensity(noiseAmount);
        noise.setNoiseIntensity(noiseAmount);
    }
}
