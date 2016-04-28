package no.agens.depth;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import no.agens.depth.lib.MathHelper;
import no.agens.depth.lib.headers.AuraDrawable;
import no.agens.depth.lib.headers.NoiseEffect;
import no.agens.depth.lib.headers.ParticleSystem;
import no.agens.depth.lib.headers.Renderable;
import no.agens.depth.lib.tween.FrameRateCounter;

/**
 * Created by danielzeller on 01.10.14.
 */
public class BearSceneView extends View {


    public static final int WIND_RANDOMIZE_INTERVAL = 300;
    private Renderable[] renderables;

    public BearSceneView(Context context) {
        super(context);

    }

    private float wind = 10f;
    float windRanomizerTarget;
    float windRanomizerEased;

    ParticleSystem flames;
    ParticleSystem sparks;
    Smoke smoke;

    public BearSceneView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BearSceneView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        renderables = new Renderable[17];
        Bitmap threeB = BitmapFactory.decodeResource(getResources(), R.mipmap.tree);
        addThree(threeB,getMeasuredWidth() * 0.18f, getMeasuredHeight() * -0.65f, 0.28f, 0.46f);
        addThree(threeB,getMeasuredWidth() * 0.6f, getMeasuredHeight() * -0.65f, 0.33f, 0.46f);
        addThree(threeB,getMeasuredWidth() * 0.45f, getMeasuredHeight() * -0.45f, 0.5f, 0.8f);
        addThree(threeB,getMeasuredWidth() * 0.13f, getMeasuredHeight() * -0.65f, 0.3f, 0.46f);
        addThree(threeB,getMeasuredWidth() * 0.83f, getMeasuredHeight() * -0.2f, 0.5f, 1f);
        addThree(threeB,getMeasuredWidth() * 0.02f, getMeasuredHeight() * -0.1f, 0.8f, 1f);
        addThree(threeB,getMeasuredWidth() * 0.18f, getMeasuredHeight() * 0.15f, 0.8f, 1f);
        addThree(threeB,getMeasuredWidth() * 0.7f, getMeasuredHeight() * -0.1f, 0.8f, 1f);

        Bitmap bear1 = BitmapFactory.decodeResource(getResources(), R.mipmap.bear_1);
        Bitmap bear2 = BitmapFactory.decodeResource(getResources(), R.mipmap.bear_2);
        Bitmap bear3 = BitmapFactory.decodeResource(getResources(), R.mipmap.bear_white);
        Bitmap stones = BitmapFactory.decodeResource(getResources(), R.drawable.stones);
        Bitmap smoke = BitmapFactory.decodeResource(getResources(), R.drawable.smoke);
        Bitmap grunge = BitmapFactory.decodeResource(getResources(), R.drawable.grunge);
        addFire(smoke, stones, getMeasuredWidth() * 0.61f, getMeasuredHeight() * 0.8f);
        addBear(getMeasuredWidth() * 0.636f, getMeasuredHeight() * 0.59f, bear1, bear2);
        addWhiteBear(getMeasuredWidth() * 0.44f, getMeasuredHeight() * 0.66f, bear3);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        addGrunge(grunge);
    }

    private void addGrunge(Bitmap grunge) {
        NoiseEffect noise = new NoiseEffect(grunge, 100, 2.5f);
        renderables[index] = noise;
        noise.setNoiseIntensity(0.25f);
        index += 1;
    }

    private void addFire(Bitmap smoke, Bitmap stones, float x, float y) {

        renderables[index] = new AuraDrawable(getResources().getDrawable(R.drawable.aura_gradient), new Rect((int) (getMeasuredWidth() * 0.44f), (int) (getMeasuredHeight() * 0.4f), (int) (getMeasuredWidth() * 0.8f), (int) (getMeasuredHeight() * 1.1f)));
        index += 1;
        renderables[index] = new AuraDrawable(getResources().getDrawable(R.drawable.aura_gradient_inner), new Rect((int) (getMeasuredWidth() * 0.5f), (int) (getMeasuredHeight() * 0.6f), (int) (getMeasuredWidth() * 0.72f), (int) (getMeasuredHeight() * 1f)));
        index += 1;

        float density = getResources().getDisplayMetrics().density;
        float randomXPlacement = 5f * density;
        flames = new ParticleSystem(x, y, 30, -30f * density, randomXPlacement);
        sparks = new ParticleSystem(x, y, 600, -30f * density, randomXPlacement);

        renderables[index] = flames;
        flames.setParticleSize((int) (8f * density));
        flames.setRandomMovementX(20f * density);
        flames.setRandomMovementY(1.5f * density);
        flames.setColors(getResources().getColor(R.color.fire_start_color), getResources().getColor(R.color.fire_end_color));
        index += 1;

        renderables[index] = sparks;
        sparks.setParticleSize((int) (1f * density));
        sparks.setRandomMovementX(25f * density);
        sparks.setRandomMovementY(2.5f * density);
        sparks.setRandomMovementChangeInterval(900);
        sparks.setColors(getResources().getColor(R.color.fire_start_color), getResources().getColor(R.color.fire_start_color));
        sparks.setMinYCoord(0);
        index += 1;

        renderables[index] = new Renderable(stones, x - randomXPlacement * 2f, y);
        index += 1;
        this.smoke = new Smoke(smoke, x, getMeasuredHeight() * 0.68f, 110 * density, 60 * density, 8, density);
        renderables[index] = this.smoke;
        index += 1;

    }

    private void addWhiteBear(float v, float v1, Bitmap bear3) {
        renderables[index] = new Renderable(bear3, v, v1);
        index += 1;
    }

    private void addBear(float v, float v1, Bitmap bear1, Bitmap bear2) {
        renderables[index] = new RenderableBear(new Bitmap[]{bear1, bear2}, v, v1);
        index += 1;
    }

    int index = 0;

    void addThree(Bitmap bitmap,float x, float y, float scale, float alpha) {

        renderables[index] = new RenderableThree(bitmap, x, y, alpha);
        renderables[index].setScale(scale, scale);
        index += 1;
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destroyResources();
    }

    private void destroyResources() {

        for (Renderable renderable: renderables)
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
        windRanomizerEased += ((windRanomizerTarget - windRanomizerEased) * 4f) * deltaTime;
        for (Renderable renderable : renderables) {
            renderable.draw(canvas);
            if (renderable instanceof Smoke || renderable instanceof ParticleSystem)
                renderable.update(deltaTime, wind);
            else
                renderable.update(deltaTime, wind + windRanomizerEased);
        }
        if (lastWindRandomChange + WIND_RANDOMIZE_INTERVAL < System.currentTimeMillis()) {
            lastWindRandomChange = System.currentTimeMillis();
            float randomSpeedInterval = Math.max(wind / 2, 1);
            windRanomizerTarget = (float) MathHelper.rand.nextInt((int) randomSpeedInterval) - randomSpeedInterval / 2f;
        }
        if (!pause)
            invalidate();
    }

    public void setPause(boolean pause) {
        this.pause = pause;
        if (!pause) {
            FrameRateCounter.timeStep();
            invalidate();
            for (Renderable renderable : renderables)
                renderable.resume();
        } else {
            for (Renderable renderable : renderables)
                renderable.pause();
        }
    }

    private boolean pause = false;

    long lastWindRandomChange;


    public void setWind(int wind) {
        this.wind = wind;
    }

    float LOWEST_FLAMES_COORD = 0.8f;
    float HIGHEST_FLAMES_COORD = 0.4f;
    float HIGHEST_SMOKE_COORD = 0.6f;

    public void setFlamesHeight(int progress) {
        float flamesHeight = getYCoordByPercent(LOWEST_FLAMES_COORD - ((LOWEST_FLAMES_COORD - HIGHEST_FLAMES_COORD) * ((float) progress / 100f)));
        flames.setMinYCoord(flamesHeight);
        float smokeYCoord = getYCoordByPercent(LOWEST_FLAMES_COORD - ((LOWEST_FLAMES_COORD - HIGHEST_SMOKE_COORD) * ((float) progress / 100f)));
        smoke.setY(smokeYCoord);
    }


    private float getYCoordByPercent(float percent) {
        return getHeight() * percent;
    }
}
