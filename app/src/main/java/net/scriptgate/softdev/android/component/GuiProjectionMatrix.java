package net.scriptgate.softdev.android.component;


import android.opengl.Matrix;

import static android.opengl.GLES30.glViewport;

public class GuiProjectionMatrix extends ProjectionMatrix {

    public GuiProjectionMatrix(float far) {
        super(far);
    }

    @Override
    protected void initializeProjectionMatrix(int width, int height) {
        // Set the OpenGL viewport to the same size as the surface.
        glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = 0;
        final float right = width;
        final float bottom = height;
        final float top = 0.0f;
        final float near = -100.0f;

        Matrix.orthoM(projectionMatrix, 0, left, right, bottom, top, near, far);
    }
}