package io.github.ramboxeu.techworks.client.util;

public class Color {
    private int alpha;
    private int red;
    private int green;
    private int blue;

    public Color(int red, int green, int blue, int alpha) {
        if (!validateColor(red, green, blue, alpha)) {
            throw new IllegalArgumentException("Invalid RGB color");
        }

        this.red = red;
        this.blue = blue;
        this.green = green;
        this.alpha = alpha;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getAlpha() {
        return alpha;
    }

    public static boolean validateColor(int red, int green, int blue, int alpha) {
        return !(red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255 || alpha < 0 || alpha > 255);
    }

    public static Color fromRGB(int rgb) {
        rgb = rgb | 0xff000000;

        int red   = (rgb >> 16) & 0xff;
        int green = (rgb >> 8)  & 0xff;
        int blue  = (rgb >> 0)  & 0xff;

        return new Color(red, green, blue, 255);
    }

    public static Color fromRGBA(int rgba) {
        int red   = (rgba >> 16) & 0xff;
        int green = (rgba >> 8)  & 0xff;
        int blue  = (rgba >> 0)  & 0xff;
        int alpha = (rgba >> 24) & 0xff;

        return new Color(red, green, blue, alpha);
    }

    public static int toRGBA(int red, int green, int blue, int alpha) {
        if (!validateColor(red, green, blue, alpha)) {
            throw new IllegalArgumentException("Invalid RGB color");
        }

        return  ((alpha & 0xFF) << 24) |
                ((red & 0xFF)   << 16) |
                ((green & 0xFF) << 8)  |
                ((blue & 0xFF)  << 0);
    }

    public static int HSVtoRGB(float hue, float saturation, float value) {
        int h = (int)(hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        float r = 0;
        float g = 0;
        float b = 0;

        switch (h) {
            case 0:
                r = value; g = t; b = p;
                break;
            case 1:
                r = q; g = value; b = p;
                break;
            case 2:
                r = p; g = value; b = t;
                break;
            case 3:
                r = p; g = q; b = value;
                break;
            case 4:
                r = t; g = p; b = value;
                break;
            case 5:
                r = value; g = p; b = value;
                break;
        }

        return 0xFF000000 | ((int)(r * 256) << 16) | ((int)(g * 256) << 8) | (int)(b * 256);
    }
}
