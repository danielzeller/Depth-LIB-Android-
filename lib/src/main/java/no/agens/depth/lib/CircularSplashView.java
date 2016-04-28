package no.agens.depth.lib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import no.agens.depth.lib.tween.interpolators.QuintOut;


/**
 * Created by danielzeller on 03.09.14.
 */
public class CircularSplashView extends View {

    private List<CircledDrawable> circles = new ArrayList<CircledDrawable>();

    public CircularSplashView(Context context) {
        super(context);
    }

    public CircularSplashView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularSplashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSplash(Bitmap splash) {
        this.splash = splash;
    }

    public void setSplashColor(int splashColor) {
        this.splashColor = splashColor;
    }

    private Bitmap splash;
    private int splashColor;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (circles.size() == 0) {
            introAnimate();
        }
    }

    public void introAnimate() {
        circles.clear();
        RectF expandedSizeFloat = new RectF(0, 0, getWidth(), getHeight());
        Rect expandedSizeInt = new Rect(0, 0, getWidth(), getHeight());
        RectF biggerRect = new RectF(-1, -1, getWidth() + 1, getHeight() + 1);
        circles.add(new CircleColorExpand(expandedSizeFloat, 0, 600, splashColor));
        circles.add(new CircleColorExpand(biggerRect, 70, 600, Color.WHITE));
        circles.add(new CircleBitmapExpand(expandedSizeInt, 130, 800, splash));
        for (CircledDrawable c : circles)
            c.startAnim();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (CircledDrawable circle : circles)
            circle.draw(canvas);
    }

    interface CircledDrawable {
        public void startAnim();

        public void draw(Canvas canvas);
    }

    public class CircleBitmapExpand implements CircledDrawable {
        Rect targetSize;
        Rect drawingRect;
        long startDelay;
        long animDuration;
        Bitmap bitmap;

        public CircleBitmapExpand(Rect targetSize, long startDelay, long animDuration, Bitmap inBitmap) {
            this.targetSize = targetSize;
            this.startDelay = startDelay;
            this.animDuration = animDuration;
            bitmap = inBitmap;

        }

        public Bitmap GetBitmapClippedCircle(Bitmap bitmap) {

            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();
            final Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            final Path path = new Path();
            path.addCircle(
                    (float) (width / 2)
                    , (float) (height / 2)
                    , (float) Math.min(width, (height / 2))
                    , Path.Direction.CCW);

            final Canvas canvas = new Canvas(outputBitmap);
            canvas.clipPath(path);
            canvas.drawBitmap(bitmap, 0, 0, null);
            bitmap.recycle();
            return outputBitmap;
        }

        public void startAnim() {
            Rect startRect = new Rect(targetSize.centerX(), targetSize.centerY(), targetSize.centerX(), targetSize.centerY());
            drawingRect = startRect;
            ValueAnimator rectSize = ValueAnimator.ofObject(new RectEvaluator(), startRect, targetSize);
            rectSize.setDuration(animDuration);
            rectSize.setInterpolator(new QuintOut());
            rectSize.setStartDelay(startDelay);
            rectSize.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    drawingRect = (Rect) animation.getAnimatedValue();
                    invalidate();
                }
            });
            rectSize.start();
        }

        public void draw(Canvas canvas) {
            if (drawingRect != null)
                canvas.drawBitmap(bitmap, null, drawingRect, null);
        }
    }

    public class CircleColorExpand implements CircledDrawable {
        private Paint paint = new Paint(Color.BLACK);
        RectF targetSize;
        RectF drawingRect;
        long startDelay;
        long animDuration;

        public CircleColorExpand(RectF targetSize, long startDelay, long animDuration, int paintColor) {
            this.targetSize = targetSize;
            this.startDelay = startDelay;
            this.animDuration = animDuration;
            paint.setColor(paintColor);
            paint.setAntiAlias(true);
            paint.setDither(true);
        }

        public void startAnim() {
            RectF startRect = new RectF(targetSize.centerX(), targetSize.centerY(), targetSize.centerX(), targetSize.centerY());
            ValueAnimator rectSize = ValueAnimator.ofObject(new RectFEvaluator(), startRect, targetSize);
            rectSize.setDuration(animDuration);
            rectSize.setInterpolator(new QuintOut());
            rectSize.setStartDelay(startDelay);
            rectSize.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    drawingRect = (RectF) animation.getAnimatedValue();
                    invalidate();
                }
            });
            rectSize.start();
        }

        public void draw(Canvas canvas) {
            if (drawingRect != null)
                canvas.drawOval(drawingRect, paint);
        }
    }
}
