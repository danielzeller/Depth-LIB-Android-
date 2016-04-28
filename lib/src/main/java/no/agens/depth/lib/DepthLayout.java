package no.agens.depth.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.RelativeLayout;

import no.agens.depth.lib.R;

public class DepthLayout extends RelativeLayout {


    public static final int DEFAULT_EDGE_COLOR = Color.WHITE;
    public static final int DEFAULT_THICKNESS = 2;
    Paint edgePaint = new Paint();

    public DepthLayout(Context context) {
        super(context);
        initView(null);

    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }

    public Paint getEdgePaint() {
        return edgePaint;
    }


    public void setEdgePaint(Paint edgePaint) {
        this.edgePaint = edgePaint;
    }

    private void initView(AttributeSet attrs) {

        edgePaint.setColor(DEFAULT_EDGE_COLOR);
        edgePaint.setAntiAlias(true);
        if (attrs != null) {
            TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.DepthView);
            edgePaint.setColor(arr.getInt(R.styleable.DepthView_edge_color, DEFAULT_EDGE_COLOR));
            setIsCircle(arr.getBoolean(R.styleable.DepthView_is_circle, false));
            depth = arr.getDimension(R.styleable.DepthView_depth, DEFAULT_THICKNESS * getResources().getDisplayMetrics().density);
            customShadowElevation = arr.getDimension(R.styleable.DepthView_custom_elevation, 0);
        } else {
            edgePaint.setColor(DEFAULT_EDGE_COLOR);
            depth = DEFAULT_THICKNESS * getResources().getDisplayMetrics().density;
        }
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {

            }
        });
    }


    public DepthLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);

    }

    public DepthLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }


    private float depth;


    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
        ((View)getParent()).invalidate();
    }

    public boolean isCircle() {
        return isCircle;
    }

    public void setIsCircle(boolean isCircle) {
        this.isCircle = isCircle;
    }

    private boolean isCircle = false;

    float[] prevSrc = new float[8];

    public boolean calculateBounds() {

        float[] src = new float[8];
        float[] dst = new float[]{0, 0, getWidth(), 0, 0, getHeight(), getWidth(), getHeight()};
        Matrix matrix = getMatrix();

        matrix.mapPoints(src, dst);
        topLeft.x = src[0] + getLeft();
        topLeft.y = src[1] + getTop();
        topRight.x = src[2] + getLeft();
        topRight.y = src[3] + getTop();

        bottomLeft.x = src[4] + getLeft();
        bottomLeft.y = src[5] + getTop();
        bottomRight.x = src[6] + getLeft();
        bottomRight.y = src[7] + getTop();
        boolean returnValue = hasMatrixChanged(src);
        prevSrc = src;
        float percentFrom90X = (getRotationX()) / 90f;
        float percentFrom90Y = (-getRotationY()) / 90f;


        matrix.postTranslate(percentFrom90Y * getDepth(), percentFrom90X * getDepth());
        src = new float[8];
        dst = new float[]{0, 0, getWidth(), 0, 0, getHeight(), getWidth(), getHeight()};
        matrix.mapPoints(src, dst);

        topLeftBack.x = src[0] + getLeft();
        topLeftBack.y = src[1] + getTop();
        topRightBack.x = src[2] + getLeft();
        topRightBack.y = src[3] + getTop();

        bottomLeftBack.x = src[4] + getLeft();
        bottomLeftBack.y = src[5] + getTop();
        bottomRightBack.x = src[6] + getLeft();
        bottomRightBack.y = src[7] + getTop();
        customShadow.calculateBounds(this);

        return returnValue;
    }

    boolean hasMatrixChanged(float[] newSrc) {
        for (int i = 0; i < 8; i++) {
            if (newSrc[i] != prevSrc[i])
                return true;
        }
        return false;
    }

    public PointF getTopLeft() {
        return topLeft;
    }

    public PointF getTopRight() {
        return topRight;
    }

    public PointF getBottomLeft() {
        return bottomLeft;
    }

    public PointF getBottomRight() {
        return bottomRight;
    }

    public PointF getTopLeftBack() {
        return topLeftBack;
    }

    public PointF getTopRightBack() {
        return topRightBack;
    }

    public PointF getBottomLeftBack() {
        return bottomLeftBack;
    }

    public PointF getBottomRightBack() {
        return bottomRightBack;
    }

    PointF topLeft = new PointF(0, 0);
    PointF topRight = new PointF(0, 0);
    PointF bottomLeft = new PointF(0, 0);
    PointF bottomRight = new PointF(0, 0);


    PointF topLeftBack = new PointF(0, 0);
    PointF topRightBack = new PointF(0, 0);
    PointF bottomLeftBack = new PointF(0, 0);
    PointF bottomRightBack = new PointF(0, 0);


    private CustomShadow customShadow = new CustomShadow();

    public CustomShadow getCustomShadow() {
        return customShadow;
    }

    public void setCustomShadowElevation(float customShadowElevation) {
        this.customShadowElevation = customShadowElevation;
        ((View)getParent()).invalidate();
    }

    public float getCustomShadowElevation() {
        return customShadowElevation;
    }

    float customShadowElevation;

    class CustomShadow {

        public static final float DEFAULT_SHADOW_PADDING = 10f;
        PointF topLeftBack = new PointF(0, 0);
        PointF topRightBack = new PointF(0, 0);
        PointF bottomLeftBack = new PointF(0, 0);
        PointF bottomRightBack = new PointF(0, 0);
        int padding;

        public boolean calculateBounds(DepthLayout target) {
            float[] src = new float[8];
            float density = getResources().getDisplayMetrics().density;
            float offsetY = customShadowElevation;
            float offsetX = customShadowElevation / 5;
            padding = (int) (customShadowElevation / 4f + DEFAULT_SHADOW_PADDING * density);

            float[] dst = new float[]{-padding, -padding, target.getWidth() + padding, -padding, -padding, target.getHeight() + padding, target.getWidth() + padding, target.getHeight() + padding};
            Matrix matrix = getMatrix();
            matrix.mapPoints(src, dst);

            topLeftBack.x = src[0] + target.getLeft() + offsetX;
            topLeftBack.y = src[1] + target.getTop() + offsetY;
            topRightBack.x = src[2] + target.getLeft() + offsetX;
            topRightBack.y = src[3] + target.getTop() + offsetY;

            bottomLeftBack.x = src[4] + target.getLeft() + offsetX;
            bottomLeftBack.y = src[5] + target.getTop() + offsetY;
            bottomRightBack.x = src[6] + target.getLeft() + offsetX;
            bottomRightBack.y = src[7] + target.getTop() + offsetY;

            return false;
        }

        Matrix matrix = new Matrix();

        public void drawShadow(Canvas canvas, DepthLayout dl, Drawable shadow) {

            shadow.setBounds(-padding, -padding, dl.getWidth() + padding, dl.getHeight() + padding);
            float[] src = new float[]{0, 0, dl.getWidth(), 0, dl.getWidth(), dl.getHeight(), 0, dl.getHeight()};
            float[] dst = new float[]{topLeftBack.x, topLeftBack.y, topRightBack.x, topRightBack.y, bottomRightBack.x, bottomRightBack.y, bottomLeftBack.x, bottomLeftBack.y};
            int count = canvas.save();
            matrix.setPolyToPoly(src, 0, dst, 0, src.length >> 1);
            canvas.concat(matrix);
            shadow.draw(canvas);
            canvas.restoreToCount(count);
        }
    }
}
