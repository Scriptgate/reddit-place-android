package net.scriptgate.softdev.android.entity;

import net.scriptgate.softdev.android.common.Point2D;
import net.scriptgate.softdev.android.common.Point2DFace;
import net.scriptgate.softdev.android.common.Point3D;
import net.scriptgate.softdev.android.common.Point3DFace;

import static net.scriptgate.softdev.android.common.Face.ELEMENTS_PER_FACE;

public class SquareDataFactory {

    public static float[] generateXZPositionData() {
        Point3DFace face = new Point3DFace(
                new Point3D(0.0f, 0.0f, 0.0f),
                new Point3D(1.0f, 0.0f, 0.0f),
                new Point3D(0.0f, 0.0f, 1.0f),
                new Point3D(1.0f, 0.0f, 1.0f)
        );
        float[] vertices = new float[ELEMENTS_PER_FACE * face.getNumberOfFloatsPerElement()];
        face.addFaceToArray(vertices, 0);
        return vertices;
    }

    public static float[] generateXYPositionData(float width, float height) {
        Point3DFace face = new Point3DFace(
                new Point3D(0.0f, 0.0f, 0.0f),
                new Point3D(width, 0.0f, 0.0f),
                new Point3D(0.0f, height, 0.0f),
                new Point3D(width, height, 0.0f)
        );
        float[] vertices = new float[ELEMENTS_PER_FACE * face.getNumberOfFloatsPerElement()];
        face.addFaceToArray(vertices, 0);
        return vertices;
    }

    // S, T (or X, Y)
    // Texture coordinate data.
    // Because images have a Y axis pointing downward (values increase as you move down the image) while
    // OpenGL has a Y axis pointing upward, we adjust for that here by flipping the Y axis.
    // What's more is that the texture coordinates are the same for every face.
    public static float[] generateTextureData(float width, float height) {
        Point2DFace face = new Point2DFace(
                new Point2D(0.0f, 0.0f),
                new Point2D(width, 0.0f),
                new Point2D(0.0f, height),
                new Point2D(width, height)
        );
        float[] textureData = new float[ELEMENTS_PER_FACE * face.getNumberOfFloatsPerElement()];
        face.addFaceToArray(textureData, 0);
        return textureData;
    }

    // S, T (or X, Y)
    // Texture coordinate data.
    // Because images have a Y axis pointing downward (values increase as you move down the image) while
    // OpenGL has a Y axis pointing upward, we adjust for that here by flipping the Y axis.
    // What's more is that the texture coordinates are the same for every face.
    public static float[] generateTextureData(float width, float height, Point2D offset) {
        Point2DFace face = new Point2DFace(
                new Point2D(offset.x, offset.y),
                new Point2D(offset.x + width, offset.y),
                new Point2D(offset.x, offset.y + height),
                new Point2D(offset.x + width, offset.y + height)
        );
        float[] textureData = new float[ELEMENTS_PER_FACE * face.getNumberOfFloatsPerElement()];
        face.addFaceToArray(textureData, 0);
        return textureData;
    }
}
