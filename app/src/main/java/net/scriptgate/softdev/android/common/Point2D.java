package net.scriptgate.softdev.android.common;

public class Point2D {

    public float x;
    public float y;

    public Point2D() {
        this(0, 0);
    }

    public Point2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static float distance(Point2D u, Point2D v) {
        float x = u.x - v.x;
        float y = u.y - v.y;
        return (float) Math.sqrt(x*x + y*y);
    }
}
