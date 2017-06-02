package net.scriptgate.softdev.android.component;

import android.opengl.GLU;
import android.opengl.Matrix;

import net.scriptgate.softdev.android.common.Point3D;

import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES30.glViewport;

public class ProjectionMatrix {

    protected final float far;

    private int width;
    private int height;

    public ProjectionMatrix(float far) {
        this.far = far;
    }

    public static ProjectionMatrix createProjectionMatrix() {
        return new ProjectionMatrix(10.0f);
    }

    public static ProjectionMatrix createProjectMatrix(float far) {
        return new ProjectionMatrix(far);
    }

    /**
     * Store the projection matrix. This is used to project the scene onto a 2D viewport.
     */
    protected float[] projectionMatrix = new float[16];

    public void onSurfaceChanged(int width, int height) {
        this.width = width;
        this.height = height;
        initializeProjectionMatrix(width, height);
    }

    protected void initializeProjectionMatrix(int width, int height) {
        // Set the OpenGL viewport to the same size as the surface.
        glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;

        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);
    }

    public void multiplyWithMatrixAndStore(float[] matrix) {
        Matrix.multiplyMM(matrix, 0, projectionMatrix, 0, matrix, 0);
    }

    public void multiplyWithMatrixAndStore(float[] matrix, float[] resultMatrix) {
        Matrix.multiplyMM(resultMatrix, 0, projectionMatrix, 0, matrix, 0);
    }

    public boolean unProject(Point3D touch, ModelViewProjectionMatrix mvMatrix, float[] output) {
        Point3D window = new Point3D(touch.x, (float) height - touch.y, touch.z);
        return GLU.gluUnProject(window.x, window.y, window.z, mvMatrix.getMatrix(), 0, projectionMatrix, 0, new int[]{0, 0, width, height}, 0, output, 0) == GL10.GL_TRUE;
    }
}
