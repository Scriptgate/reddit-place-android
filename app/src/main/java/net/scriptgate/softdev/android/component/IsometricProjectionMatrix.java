package net.scriptgate.softdev.android.component;

import android.opengl.Matrix;

import static android.opengl.GLES30.glViewport;

public class IsometricProjectionMatrix extends ProjectionMatrix {

    private float zoom;

    public IsometricProjectionMatrix(float far, float zoom) {
        super(far);
        this.zoom = zoom;
    }

    @Override
    protected void initializeProjectionMatrix(int width, int height) {
        // Set the OpenGL viewport to the same size as the surface.
        glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio * zoom;
        final float right = ratio * zoom;
        final float bottom = -1.0f * zoom;
        final float top = 1.0f * zoom;
        final float near = -100.0f;

        Matrix.orthoM(projectionMatrix, 0, left, right, bottom, top, near, far);
    }
}
