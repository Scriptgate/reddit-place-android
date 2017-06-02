package net.scriptgate.softdev.android.common;

public class ColorPoint3DFace extends Face<ColorPoint3D> {

    public ColorPoint3DFace(ColorPoint3D p1, ColorPoint3D p2, ColorPoint3D p3, ColorPoint3D p4) {
        super(p1, p2, p3, p4);
    }

    @Override
    public int getNumberOfFloatsPerElement() {
        return 7;
    }

    @Override
    public void addToArray(ColorPoint3D element, float[] data, int offset) {
        data[offset] = element.point.x;
        data[offset + 1] = element.point.y;
        data[offset + 2] = element.point.z;
        data[offset + 3] = element.color.red;
        data[offset + 4] = element.color.green;
        data[offset + 5] = element.color.blue;
        data[offset + 6] = element.color.alpha;
    }
}
