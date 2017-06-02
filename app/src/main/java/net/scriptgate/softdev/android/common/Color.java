package net.scriptgate.softdev.android.common;

public class Color {

    //@formatter:off
    public static final Color RED     = new Color(1.0f, 0.0f, 0.0f, 1.0f);
    public static final Color GREEN   = new Color(0.0f, 1.0f, 0.0f, 1.0f);
    public static final Color BLUE    = new Color(0.0f, 0.0f, 1.0f, 1.0f);
    public static final Color WHITE   = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    public static final Color GREY    = new Color(0.5f, 0.5f, 0.5f, 1.0f);
    public static final Color BLACK   = new Color(0.0f, 0.0f, 0.0f, 1.0f);
    public static final Color MAGENTA = new Color(1.0f, 0.0f, 1.0f, 1.0f);
    public static final Color YELLOW  = new Color(1.0f, 1.0f, 0.0f, 1.0f);
    public static final Color CYAN    = new Color(0.0f, 1.0f, 1.0f, 1.0f);
    //@formatter:on

    public final float red;
    public final float green;
    public final float blue;
    public final float alpha;


    public Color(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public float[] toArray() {
        return new float[]{red, green, blue, alpha};
    }

    public Color alpha(float alpha) {
        return new Color(red, green, blue, alpha);
    }

    public static Color fromHex(String hex) {
        return new Color(
                Integer.valueOf(hex.substring(1, 3), 16) / 255f,
                Integer.valueOf(hex.substring(3, 5), 16)/ 255f,
                Integer.valueOf(hex.substring(5, 7), 16)/ 255f,
                1.0f
        );
    }
}
