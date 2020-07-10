package io.github.ramboxeu.techworks.common.util;

public class RenderUtils {

    public static int rgbColor(int red, int green, int blue) {
        return rgbaColor(red, green, blue, 255);
    }

    public static int rgbaColor(int red, int green, int blue, int alpha) {
        if (red > 256 || red < 0) {
            throw new IllegalArgumentException(red + " is outside of [0, 255] range");
        }

        if (green > 256 || green < 0) {
            throw new IllegalArgumentException(green + " is outside of [0, 255] range");
        }

        if (blue > 256 || blue < 0) {
            throw new IllegalArgumentException(blue + " is outside of [0, 255] range");
        }

        if (alpha > 256 || alpha < 0) {
            throw new IllegalArgumentException(alpha + " is outside of [0, 255] range");
        }

        return  ((alpha & 0xFF) << 24) |
                ((red & 0xFF)   << 16) |
                ((green & 0xFF) << 8)  |
                ((blue & 0xFF)  << 0);
    }

    public static final class FontColors {
        public static final int DEFAULT_SCREEN = 4210752;
    }
}
