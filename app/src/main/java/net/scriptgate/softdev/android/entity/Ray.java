package net.scriptgate.softdev.android.entity;

import net.scriptgate.softdev.android.common.Point2D;
import net.scriptgate.softdev.android.common.Point3D;
import net.scriptgate.softdev.android.component.ModelMatrix;
import net.scriptgate.softdev.android.component.ModelViewProjectionMatrix;
import net.scriptgate.softdev.android.component.ProjectionMatrix;
import net.scriptgate.softdev.android.component.ViewMatrix;

public class Ray {

    private static final float NEAR = 1.0f;
    private static final float FAR = 0.0f;

    public final Point3D p1;
    public final Point3D p2;

    public Ray(ModelMatrix modelMatrix, ViewMatrix viewMatrix, ProjectionMatrix projectionMatrix, ModelViewProjectionMatrix mvpMatrix, Point2D touch) {
        mvpMatrix.multiply(modelMatrix, viewMatrix);
        this.p1 = screenCoordinatesToViewCoordinates(new Point3D(touch.x, touch.y, FAR), mvpMatrix, projectionMatrix);
        this.p2 = screenCoordinatesToViewCoordinates(new Point3D(touch.x, touch.y, NEAR), mvpMatrix, projectionMatrix);
    }

    private Point3D screenCoordinatesToViewCoordinates(Point3D touch, ModelViewProjectionMatrix mvMatrix, ProjectionMatrix projectionMatrix) {
        float[] far = new float[4];
        projectionMatrix.unProject(touch, mvMatrix, far);
        return new Point3D(far[0], far[1], far[2]);
    }

    public Point3D intersectXZ() {
        float x = -p1.y * ((p2.x - p1.x) / (p2.y - p1.y)) + p1.x;
        float z = -p1.y * ((p2.z - p1.z) / (p2.y - p1.y)) + p1.z;

        return new Point3D(x, 0.0f, z);
    }
}
