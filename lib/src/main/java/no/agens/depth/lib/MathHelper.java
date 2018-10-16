package no.agens.depth.lib;

import java.util.Random;

public class MathHelper {
    public static final Random rand = new Random();
    public static float randomRange(float min, float max) {


        return rand.nextInt(((int) max - (int) min) + 1) + (int) min;
    }
}
