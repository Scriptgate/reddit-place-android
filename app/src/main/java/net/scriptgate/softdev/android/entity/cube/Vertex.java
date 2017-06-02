package net.scriptgate.softdev.android.entity.cube;

import net.scriptgate.softdev.android.common.Point2D;
import net.scriptgate.softdev.android.common.Point3D;

import java.nio.FloatBuffer;

class Vertex {

    private Point3D position;
    private Point2D textureCoordinate;

    Vertex(Point3D position, Point2D textureCoordinate) {
        this.position = position;
        this.textureCoordinate = textureCoordinate;
    }

    public void putIn(FloatBuffer buffer) {
        buffer.put(position.x);
        buffer.put(position.y);
        buffer.put(position.z);
        buffer.put(textureCoordinate.x);
        buffer.put(textureCoordinate.y);
    }
}