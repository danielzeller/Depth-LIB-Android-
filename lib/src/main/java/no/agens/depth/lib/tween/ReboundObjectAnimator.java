package no.agens.depth.lib.tween;

import android.animation.ValueAnimator;
import android.util.Log;
import android.util.Property;
import android.view.View;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by danielzeller on 06.10.14.
 */
public class ReboundObjectAnimator extends ValueAnimator {

    private Method propertyMethod;
    Property<View, Float> property;
    private float from;
    private float to;
    private Spring spring;
    private Object target;

    private double tension = 150;
    private double friction = 6;
    ArrayList<AnimatorUpdateListener> updateListeners = null;
    float currentAnimatedValue;
    float fraction;

    public static ReboundObjectAnimator ofFloat(Object target, String propertyName, float from, float to) {
        ReboundObjectAnimator animator = new ReboundObjectAnimator(target, propertyName, from, to);

        return animator;
    }
    public static ReboundObjectAnimator ofFloat(Object target, Property propertyName, float from, float to) {
        ReboundObjectAnimator animator = new ReboundObjectAnimator(target, propertyName, from, to);

        return animator;
    }
    public ReboundObjectAnimator(Object target, String setterName, float from, float to) {
        propertyMethod = getPropertyMethod(target, "set" + setterName);
        this.from = from;
        this.to = to;
        this.target = target;
    }

    public ReboundObjectAnimator(Object target, Property property, float from, float to) {
        this.property = property;
        this.from = from;
        this.to = to;
        this.target = target;
    }

    public void start() {
        spring = SpringSystem.create().createSpring();

        spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(tension, friction));
        final float moveAmount = to - from;
        spring.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringUpdate(Spring spring) {
                  fraction = (float) spring.getCurrentValue();

                  currentAnimatedValue = from + fraction * moveAmount;
                if (propertyMethod != null)
                    try {
                        propertyMethod.invoke(target, currentAnimatedValue);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                else if(property!=null)
                    property.set((View)target, currentAnimatedValue);

                if(updateListeners!=null)
                for (AnimatorUpdateListener updateListener:updateListeners)
                    updateListener.onAnimationUpdate(ReboundObjectAnimator.this);
            }

            @Override
            public void onSpringAtRest(Spring spring) {
                super.onSpringAtRest(spring);
                spring.destroy();
                if (getListeners() != null)
                    for (AnimatorListener listener : getListeners())
                        listener.onAnimationEnd(null);
                removeAllListeners();
            }

        });
        spring.setEndValue(1);
    }

    public double getTension() {
        return tension;
    }

    public ReboundObjectAnimator setTension(double tension) {
        this.tension = tension;
        return this;
    }

    public double getFriction() {
        return friction;
    }

    public ReboundObjectAnimator setFriction(double friction) {
        this.friction = friction;
        return this;
    }

    public void cancel() {
        if (spring != null)
            spring.destroy();
        if (getListeners() != null)
            for (AnimatorListener listener : getListeners())
                listener.onAnimationCancel(null);
        removeAllListeners();
    }

    @Override
    public long getStartDelay() {
        return 0;
    }

    @Override
    public void setStartDelay(long startDelay) {

    }

    @Override
    public Object getAnimatedValue() {
        return currentAnimatedValue;
    }

    @Override
    public float getAnimatedFraction() {
        return fraction;
    }

    public static Method getPropertyMethod(Object target, String fieldName) {
        Class<?> clazz = target.getClass();
        try {
            Method method = findMethod(clazz, fieldName);
            return method;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.e("REBOUND_OBJECT_ANIM", "Could not find property: " + fieldName);
        }
        return null;
    }

    public static Method findMethod(Class<?> clazz, String methodName) throws NoSuchMethodException {
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        throw new NoSuchMethodException();
    }

    public boolean isRunning() {
        return spring != null && !spring.isAtRest();
    }

    public void addUpdateListener(AnimatorUpdateListener listener) {
        if (updateListeners == null) {
            updateListeners = new ArrayList<AnimatorUpdateListener>();
        }
        updateListeners.add(listener);
    }

    /**
     * Removes all listeners from the set listening to frame updates for this animation.
     */
    public void removeAllUpdateListeners() {
        if (updateListeners == null) {
            return;
        }
        updateListeners.clear();
        updateListeners = null;
    }

    /**
     * Removes a listener from the set listening to frame updates for this animation.
     *
     * @param listener the listener to be removed from the current set of update listeners
     * for this animation.
     */
    public void removeUpdateListener(AnimatorUpdateListener listener) {
        if (updateListeners == null) {
            return;
        }
        updateListeners.remove(listener);
        if (updateListeners.size() == 0) {
            updateListeners = null;
        }
    }

}
