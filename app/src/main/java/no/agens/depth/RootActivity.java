package no.agens.depth;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import no.agens.depth.tween.interpolators.ExpoIn;
import no.agens.depth.tween.interpolators.QuintOut;


public class RootActivity extends Activity {
    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_root);
        makeAppFullscreen();
        if (savedInstanceState == null) {
            currentFragment = new WaterFragment();
            getFragmentManager().beginTransaction().add(R.id.fragment_container, currentFragment).commit();
        }
        setupMenu();
    }

    public void setCurretMenuIndex(int curretMenuIndex) {
        this.curretMenuIndex = curretMenuIndex;
    }

    int curretMenuIndex = 0;

    private void makeAppFullscreen() {
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    boolean isMenuVisible = false;
    ViewGroup menu;

    @Override
    public void onBackPressed() {

        if (isMenuVisible) {
            hideMenu();
            ((MenuAnimation) currentFragment).revertFromMenu();
        } else
            super.onBackPressed();
    }

    public void showMenu() {
        isMenuVisible = true;
        ObjectAnimator translationY = ObjectAnimator.ofFloat(menu, View.TRANSLATION_Y, menu.getHeight(), 0);
        translationY.setDuration(1000);
        translationY.setInterpolator(new QuintOut());
        translationY.setStartDelay(150);
        translationY.start();
        selectMenuItem(curretMenuIndex, ((TextView) menu.getChildAt(curretMenuIndex).findViewById(R.id.item_text)).getCurrentTextColor());
        ((MenuAnimation) currentFragment).animateTOMenu();
    }

    public void hideMenu() {
        isMenuVisible = false;
        ObjectAnimator translationY = ObjectAnimator.ofFloat(menu, View.TRANSLATION_Y, menu.getHeight());
        translationY.setDuration(750);
        translationY.setInterpolator(new ExpoIn());
        translationY.start();
    }


    private void setupMenu() {
        menu = (ViewGroup) findViewById(R.id.menu_container);
        int color = getResources().getColor(R.color.splash1);
        addMenuItem(menu, "Water And Noise", R.drawable.splash1, color, R.drawable.menu_btn, 0);
        addMenuItem(menu, "Two Bears", R.drawable.splash2, getResources().getColor(R.color.splash2), R.drawable.menu_btn2, 1);
        addMenuItem(menu, "Depth Playground", R.drawable.splash3, getResources().getColor(R.color.splash3), R.drawable.menu_btn3, 2);
        addMenuItem(menu, "About", R.drawable.splash4, getResources().getColor(R.color.splash4), R.drawable.menu_btn4, 3);
        selectMenuItem(0, color);
        menu.setTranslationY(20000);
    }


    private void addMenuItem(ViewGroup menu, String text, int drawableResource, int splashColor, int menu_btn, int menuIndex) {
        ViewGroup item = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.menu_item, menu, false);
        ((TextView) item.findViewById(R.id.item_text)).setText(text);
        CircularSplashView ic = (CircularSplashView) item.findViewById(R.id.circle);
        ic.setSplash(BitmapFactory.decodeResource(getResources(), drawableResource));
        ic.setSplashColor(splashColor);
        item.setOnClickListener(getMenuItemCLick(menuIndex, splashColor));
        if (menuIndex == 0) {
            int padding = (int) getResources().getDimension(R.dimen.menu_item_height_padding);
            menu.addView(item, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.menu_item_height) + padding));
            item.setPadding(0, padding, 0, 0);
        } else if (menuIndex == 3) {
            int padding = (int) getResources().getDimension(R.dimen.menu_item_height_padding);
            menu.addView(item, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.menu_item_height) + padding));
            item.setPadding(0, 0, 0, padding);
        } else
            menu.addView(item, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.menu_item_height)));
        item.setBackground(getResources().getDrawable(menu_btn, null));

    }

    private View.OnClickListener getMenuItemCLick(final int menuIndex, final int color) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuIndex == curretMenuIndex)
                    onBackPressed();
                else if (menuIndex == 0 && !(currentFragment instanceof WaterFragment)) {
                    ((MenuAnimation) currentFragment).exitFromMenu();
                    WaterFragment waterFragment = new WaterFragment();
                    waterFragment.setIntroAnimate(true);
                    goToFragment(waterFragment);
                    hideMenu();
                    selectMenuItem(menuIndex, color);
                } else if (menuIndex == 1 && !(currentFragment instanceof WindFragment)) {
                    ((MenuAnimation) currentFragment).exitFromMenu();
                    WindFragment windFragment = new WindFragment();
                    windFragment.setIntroAnimate(true);
                    goToFragment(windFragment);
                    hideMenu();
                    selectMenuItem(menuIndex, color);
                } else if (menuIndex == 2) {
                    startActivity(new Intent(RootActivity.this, PlayGroundActivity.class));
                   onBackPressed();
                }
            }
        };
    }

    private void selectMenuItem(int menuIndex, int color) {
        for (int i = 0; i < menu.getChildCount(); i++) {
            View menuItem = menu.getChildAt(i);
            if (i == menuIndex)
                select(menuItem, color);
            else
                unSelect(menuItem);
        }
        curretMenuIndex = menuIndex;
    }

    private void unSelect(View menuItem) {
        final View circle = menuItem.findViewById(R.id.circle);
        circle.animate().scaleX(0).scaleY(0).setDuration(150).withEndAction(new Runnable() {
            @Override
            public void run() {
                circle.setVisibility(View.INVISIBLE);
            }
        }).start();
        fadeColoTo(Color.BLACK, (TextView) menuItem.findViewById(R.id.item_text));
    }

    private void fadeColoTo(int newColor, TextView view) {

        ObjectAnimator color = ObjectAnimator.ofObject(view, "TextColor", new ArgbEvaluator(), view.getCurrentTextColor(), newColor);
        color.setDuration(200);
        color.start();
    }

    private void select(View menuItem, int color) {
        final CircularSplashView circle = (CircularSplashView) menuItem.findViewById(R.id.circle);
        circle.setScaleX(1f);
        circle.setScaleY(1f);
        circle.setVisibility(View.VISIBLE);
        circle.introAnimate();
        fadeColoTo(color, (TextView) menuItem.findViewById(R.id.item_text));
    }

    public void goToFragment(final Fragment newFragment) {
        getFragmentManager().beginTransaction().add(R.id.fragment_container, newFragment).commit();
        final Fragment removeFragment = currentFragment;
        currentFragment = newFragment;
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                getFragmentManager().beginTransaction().remove(removeFragment).commit();
            }
        }, 2000);
    }
}
