package no.agens.depth;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import no.agens.depth.lib.DepthLayout;
import no.agens.depth.lib.MaterialMenuDrawable;

public class PlayGroundActivity extends AppCompatActivity {

    private DepthLayout depthView;
    private static final float MAX_ROTATION_X = 90;
    private static final float MAX_ROTATION_Y = 90;
    private static final float MAX_ROTATION_Z = 360;

    private static final float MAX_ELEVATION = 50;
    private static final float MAX_DEPTH = 20;
    private static final float CAMERA_DISTANCE = 6000f;
    private int seekbarColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_playground);
        seekbarColor = getResources().getColor(R.color.fab);
        depthView = (DepthLayout) findViewById(R.id.depth_view);
        depthView.setCameraDistance((CAMERA_DISTANCE * getResources().getDisplayMetrics().density));
        setupSeekBars();
        makeAppFullscreen();
        setupMenuButton();
    }
    private void setupMenuButton() {
        ImageView menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });
        MaterialMenuDrawable menuIcon = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN, WaterFragment.TRANSFORM_DURATION);
        menu.setImageDrawable(menuIcon);
        menuIcon.setIconState(MaterialMenuDrawable.IconState.ARROW);
    }
    private void setupSeekBars() {
        setupRotationXSeekbar();
        setupRotationYSeekbar();
        setupRotationZSeekbar();
        setupElevationSeekbar();
        setupDepthSeekbar();

    }

    private void makeAppFullscreen() {
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void setupDepthSeekbar() {
        SeekBar depth = (SeekBar) findViewById(R.id.depth_seekbar);
        WindFragment.setProgressBarColor(depth, seekbarColor);
        depth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                depthView.setDepth(MAX_DEPTH * getResources().getDisplayMetrics().density * ((float) progress / (float) seekBar.getMax()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        depth.setProgress((int) (depth.getMax() * 0.1f));
    }

    private SeekBar setupRotationXSeekbar() {
        SeekBar rotationX = (SeekBar) findViewById(R.id.rotation_x_seekbar);
        WindFragment.setProgressBarColor(rotationX, seekbarColor);
        rotationX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                depthView.setRotationX(-MAX_ROTATION_X + (MAX_ROTATION_X * 2f) * ((float) progress / (float) seekBar.getMax()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        rotationX.setProgress((int) (rotationX.getMax() * 0.1f));
        return rotationX;

    }

    private void setupRotationYSeekbar() {
        SeekBar rotationY = (SeekBar) findViewById(R.id.rotation_y_seekbar);
        rotationY.setProgress((int) (rotationY.getMax() * 0.5f));
        WindFragment.setProgressBarColor(rotationY, seekbarColor);
        rotationY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                depthView.setRotationY(-MAX_ROTATION_Y + (MAX_ROTATION_Y * 2f) * ((float) progress / (float) seekBar.getMax()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setupRotationZSeekbar() {
        SeekBar rotation = (SeekBar) findViewById(R.id.rotation_z_seekbar);
        rotation.setProgress(0);
        WindFragment.setProgressBarColor(rotation, seekbarColor);
        rotation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                depthView.setRotation(-MAX_ROTATION_Z * ((float) progress / (float) seekBar.getMax()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void setupElevationSeekbar() {
        SeekBar elevation = (SeekBar) findViewById(R.id.elevation_seekbar);
        elevation.setProgress(0);
        WindFragment.setProgressBarColor(elevation, seekbarColor);
        elevation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                depthView.setCustomShadowElevation((MAX_ELEVATION * ((float) progress / (float) seekBar.getMax())) * getResources().getDisplayMetrics().density);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        elevation.setProgress((int) (elevation.getMax() * 0.5f));
    }
}
