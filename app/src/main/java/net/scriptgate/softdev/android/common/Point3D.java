package net.scriptgate.softdev.android.common;

import java.util.Arrays;

public class Point3D {

    public float x;
    public float y;
    public float z;

    public Point3D() {
        this(0, 0, 0);
    }

    public Point3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float[] toArray() {
        return new float[]{x, y, z};
    }

    public boolean isZero() {
        return Arrays.equals(toArray(), new float[]{0.0f, 0.0f, 0.0f});
    }

    @Override
    public String toString() {
        return "Point3D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public static Point3D minus(Point3D u, Point3D v) {
        return new Point3D(u.x - v.x, u.y - v.y, u.z - v.z);
    }

    public static Point3D crossProduct(Point3D u, Point3D v) {
        return new Point3D(
                (u.y * v.z) - (v.y * u.z),
                (v.x * u.z) - (u.x * v.z),
                (u.x * v.y) - (v.x * u.y)
        );
    }

    // dot product (3D) which allows vector operations in arguments
    public static float dot(Point3D u, Point3D v) {
        return ((u.x * v.x) + (u.y * v.y) + (u.z * v.z));
    }

    //scalar product
    public static Point3D scalarProduct(float r, Point3D u) {
        return new Point3D(u.x * r, u.y * r, u.z * r);
    }

    public static Point3D addition(Point3D u, Point3D v) {
        return new Point3D(u.x + v.x, u.y + v.y, u.z + v.z);
    }
}
