package no.agens.depth;

import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.PathMeasure;

import no.agens.depth.lib.MathHelper;
import no.agens.depth.lib.headers.PathBitmapMesh;

public class Foam extends PathBitmapMesh {
    private float verticalOffset;

    void update(float deltaTime) {

        for (int i = 0; i < foamCoords.length; i++) {
            easedFoamCoords[i] += ((foamCoords[i] - easedFoamCoords[i])) * deltaTime;
        }
    }

    float[] foamCoords;
    float[] easedFoamCoords;
    float minHeight, maxHeight;

    public Foam(int horizontalSlices, int verticalSlices, Bitmap bitmap, float minHeight, float maxHeight, int animDuration) {
        super(horizontalSlices, verticalSlices, bitmap,animDuration);
        setupFoam(horizontalSlices);
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;

    }


    private void setupFoam(int verts) {
        foamCoords = new float[verts];
        easedFoamCoords = new float[verts];
        for (int i = 0; i < verts; i++) {
            foamCoords[i] = 0;
            easedFoamCoords[i] = 0;
        }
    }

    void calcWave() {
        for (int i = 0; i < foamCoords.length; i++) {
            foamCoords[i] = MathHelper.randomRange(minHeight, maxHeight);
        }
    }



    public void matchVertsToPath(Path path, float extraOffset) {
        PathMeasure pm = new PathMeasure(path, false);
        int index = 0;
        for (int i = 0; i < staticVerts.length / 2; i++) {

            float yIndexValue = staticVerts[i * 2 + 1];
            float xIndexValue = staticVerts[i * 2];


            float percentOffsetX = (0.000001f + xIndexValue) / bitmap.getWidth();
            float percentOffsetX2 = (0.000001f + xIndexValue) / (bitmap.getWidth() + extraOffset);
            percentOffsetX2 += pathOffsetPercent;
            pm.getPosTan(pm.getLength() * (1f - percentOffsetX), coords, null);
            pm.getPosTan(pm.getLength() * (1f - percentOffsetX2), coords2, null);

            if (yIndexValue == 0) {
                setXY(drawingVerts, i, coords[0], coords2[1] + verticalOffset);
            } else {
                float desiredYCoord = Math.max(coords2[1], coords2[1] + easedFoamCoords[Math.min(easedFoamCoords.length - 1, index)]);
                setXY(drawingVerts, i, coords[0], desiredYCoord + verticalOffset);

                index += 1;

            }
        }
    }

    public void setVerticalOffset(float verticalOffset) {
        this.verticalOffset = verticalOffset;
    }


}
