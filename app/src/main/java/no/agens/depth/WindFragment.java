package no.agens.depth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.SeekBar;

import no.agens.depth.lib.MaterialMenuDrawable;


public class WindFragment extends Fragment implements MenuAnimation {


    public static final int FLAMES_INITIAL_HEIGHT = 50;
    private boolean introAnimate;

    public WindFragment() {
    }

    View root;
    MaterialMenuDrawable menuIcon;
    BearSceneView bearsScene;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_wind, container, false);
        bearsScene = (BearSceneView) root.findViewById(R.id.water_scene);

        doIntroAnimation();
        setupFabButton();
        setupMenuButton();
        ((RootActivity) getActivity()).setCurrentMenuIndex(RootActivity.WIND_SCREEN_MENU_INDEX);
        setupSliders();
        return root;
    }

    private void setupSliders() {
        SeekBar windSeekbar = (SeekBar) root.findViewById(R.id.wind_seekbar);
        final SeekBar flamesSeekbar = (SeekBar) root.findViewById(R.id.flames_seekbar);
        setProgressBarColor(windSeekbar, getResources().getColor(R.color.fab2));
        setProgressBarColor(flamesSeekbar, getResources().getColor(R.color.fab2));

        windSeekbar.setOnSeekBarChangeListener(new SeekBarProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bearsScene.setWind(progress);
            }
        });

        flamesSeekbar.setOnSeekBarChangeListener(new SeekBarProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bearsScene.setFlamesHeight(progress);
            }
        });
        bearsScene.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                bearsScene.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                flamesSeekbar.setProgress(FLAMES_INITIAL_HEIGHT);
            }
        });

    }

    private void doIntroAnimation() {
        if (introAnimate)
            root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    TransitionHelper.startIntroAnim(root, showShadowListener);
                    hideShadow();
                    bearsScene.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bearsScene.setPause(true);
                        }
                    }, 10);
                }
            });
    }

    private void setupFabButton() {
        root.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        TransitionHelper.startExitAnim(root);
                    }
                });
                WaterFragment waterFragment = new WaterFragment();
                waterFragment.setIntroAnimate(true);
                ((RootActivity) getActivity()).goToFragment(waterFragment);
                if (((RootActivity) getActivity()).isMenuVisible)
                    ((RootActivity) getActivity()).hideMenu();
                hideShadow();
                bearsScene.setPause(true);
            }
        });
    }

    private void setupMenuButton() {
        ImageView menu = (ImageView) root.findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!((RootActivity) getActivity()).isMenuVisible)
                    ((RootActivity) getActivity()).showMenu();
                else
                    getActivity().onBackPressed();
            }
        });
        menuIcon = new MaterialMenuDrawable(getActivity(), Color.WHITE, MaterialMenuDrawable.Stroke.THIN, WaterFragment.TRANSFORM_DURATION);
        menu.setImageDrawable(menuIcon);
    }

    public static void setProgressBarColor(SeekBar progressBar, int newColor) {
        if (progressBar.getProgressDrawable() instanceof StateListDrawable) {
            StateListDrawable ld = (StateListDrawable) progressBar.getProgressDrawable();
            ld.setColorFilter(newColor, PorterDuff.Mode.SRC_IN);
            progressBar.getThumb().setColorFilter(newColor, PorterDuff.Mode.SRC_IN);
        } else if (progressBar.getProgressDrawable() instanceof LayerDrawable) {
            LayerDrawable ld = (LayerDrawable) progressBar.getProgressDrawable();
            for (int i = 0; i < ld.getNumberOfLayers(); i++) {
                Drawable d1 = ld.getDrawable(i);
                d1.setColorFilter(newColor, PorterDuff.Mode.SRC_IN);
            }
            progressBar.getThumb().setColorFilter(newColor, PorterDuff.Mode.SRC_IN);
        }

    }

    public void setIntroAnimate(boolean introAnimate) {
        this.introAnimate = introAnimate;
    }

    @Override
    public void animateTOMenu() {
        TransitionHelper.animateToMenuState(root, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                bearsScene.setPause(false);
            }
        });
        menuIcon.animateIconState(MaterialMenuDrawable.IconState.ARROW);
        hideShadow();
        bearsScene.setPause(true);
    }

    private void hideShadow() {
        View actionbarShadow = root.findViewById(R.id.actionbar_shadow);
        actionbarShadow.setVisibility(View.GONE);
    }

    @Override
    public void revertFromMenu() {
        TransitionHelper.startRevertFromMenu(root, showShadowListener);
        menuIcon.animateIconState(MaterialMenuDrawable.IconState.BURGER);
        bearsScene.setPause(true);
    }

    AnimatorListenerAdapter showShadowListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            showShadow();
            bearsScene.setPause(false);
        }
    };

    private void showShadow() {
        View actionbarShadow = root.findViewById(R.id.actionbar_shadow);
        actionbarShadow.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(actionbarShadow, View.ALPHA, 0, 0.8f).setDuration(400).start();
    }

    @Override
    public void exitFromMenu() {
        TransitionHelper.animateMenuOut(root);
        bearsScene.setPause(true);
    }
}
