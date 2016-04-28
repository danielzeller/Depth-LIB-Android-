package no.agens.depth;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import no.agens.depth.lib.headers.PathBitmapMesh;
import no.agens.depth.lib.headers.Renderable;

public class Water extends Renderable {

    public static final int VERTS = 6;
    private float height, width;

    Foam[] foams = new Foam[4];
    int numberOfWaves;
    PathBitmapMesh water;
    private float waveHeight;

    public Water(Bitmap bitmap, Bitmap foam, float y, float height, float width, int waves) {
        super(bitmap, 0, y);
        this.height = height;
        this.width = width;
        numberOfWaves = waves;

        debugPaint.setColor(Color.RED);
        debugPaint.setStyle(Paint.Style.STROKE);
        lastEmit = System.currentTimeMillis();
        water = new PathBitmapMesh(VERTS, 1, bitmap, 1500);
        foams[0] = new Foam(VERTS, 1, foam, 0, height / 12, 1500);
        foams[1] = new Foam(VERTS, 1, foam, -height / 5, height / 5, 1500);
        foams[1].setAlpha(100);
        foams[2] = new Foam(VERTS, 1, foam, -height / 12, height / 12, 1450);
        foams[2].setVerticalOffset(height / 7);
        foams[3] = new Foam(VERTS, 1, foam, -height / 12, height / 12, 1400);
        foams[3].setVerticalOffset(height / 4);
        waveHeight = height / 10;
        createPath();
    }

    @Override
    public void pause() {
        super.pause();
        water.pause();
        for (Foam foam : foams)
            foam.pause();
    }

    @Override
    public void resume() {
        super.resume();
        water.resume();
        for (Foam foam : foams)
            foam.resume();
    }

    @Override
    public void destroy() {
        super.destroy();
        for (Foam foam : foams)
            foam.destroy();
    }

    Paint debugPaint = new Paint();

    @Override
    public void draw(Canvas canvas) {

        water.draw(canvas);
        for (Foam foam : foams) {
            foam.draw(canvas);
        }
    }

    @Override
    public void update(float deltaTime, float wind) {
        super.update(deltaTime, wind);
        for (Foam foam : foams) {
            foam.update(deltaTime);
        }
        water.matchVertsToPath(waterPath, height, ((bitmap.getWidth() / numberOfWaves) * 4f));
        for (Foam foam : foams) {
            foam.matchVertsToPath(waterPath, foam.getBitmap().getWidth() / numberOfWaves * 4f);
        }
        if (lastEmit + emitInterWall < System.currentTimeMillis()) {
            for (Foam foam : foams) {
                foam.calcWave();
            }
            lastEmit = System.currentTimeMillis();
        }

    }

    private Path waterPath = new Path();

    private void createPath() {
        waterPath.reset();
        waterPath.moveTo(0, y);

        int step = (int) (width / numberOfWaves);
        boolean goLeft = true;
        for (int i = 0; i < numberOfWaves; i++) {
            if (goLeft)
                waterPath.cubicTo(x + step * i, y, x + step * i + step / 2f, y + waveHeight, x + step * i + step, y);
            else
                waterPath.cubicTo(x + step * i, y, x + step * i + step / 2f, y - waveHeight, x + step * i + step, y);

            goLeft = !goLeft;
        }

    }


    long lastEmit;
    private int emitInterWall = 1000;


    public void setWaveHeight(float waveHeight) {
        this.waveHeight = waveHeight;
        createPath();
    }
}
