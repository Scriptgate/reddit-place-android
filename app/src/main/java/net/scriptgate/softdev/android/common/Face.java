package net.scriptgate.softdev.android.common;

public abstract class Face<T> {

    public static final int ELEMENTS_PER_FACE = 6;

    private final T p1;
    private final T p2;
    private final T p3;
    private final T p4;

    public Face(T p1, T p2, T p3, T p4) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
    }

    public void addFaceToArray(float[] data, int offset) {
        // In OpenGL counter-clockwise winding is default. This means that when we look at a triangle,
        // if the points are counter-clockwise we are looking at the "front". If not we are looking at
        // the back. OpenGL has an optimization where all back-facing triangles are culled, since they
        // usually represent the backside of an object and aren't visible anyways.

        int numberOfFloatsPerElement = getNumberOfFloatsPerElement();

        // Build the triangles
        //  1---3,6
        //  | / |
        // 2,4--5
        addToArray(p1, data, offset);
        addToArray(p3, data, offset + numberOfFloatsPerElement);
        addToArray(p2, data, offset + numberOfFloatsPerElement * 2);
        addToArray(p3, data, offset + numberOfFloatsPerElement * 3);
        addToArray(p4, data, offset + numberOfFloatsPerElement * 4);
        addToArray(p2, data, offset + numberOfFloatsPerElement * 5);
    }

    public abstract int getNumberOfFloatsPerElement();

    public abstract void addToArray(T element, float[] data, int offset);
}




