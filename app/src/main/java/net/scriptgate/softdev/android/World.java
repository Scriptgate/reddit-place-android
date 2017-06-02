package net.scriptgate.softdev.android;

import android.opengl.GLSurfaceView;

import net.scriptgate.softdev.android.common.Point2D;
import net.scriptgate.softdev.android.common.Point3D;
import net.scriptgate.softdev.android.component.IsometricProjectionMatrix;
import net.scriptgate.softdev.android.component.ModelMatrix;
import net.scriptgate.softdev.android.component.ModelViewProjectionMatrix;
import net.scriptgate.softdev.android.component.ProjectionMatrix;
import net.scriptgate.softdev.android.component.ViewMatrix;
import net.scriptgate.softdev.android.entity.Event;
import net.scriptgate.softdev.android.entity.Ray;
import net.scriptgate.softdev.android.entity.cube.IndexBufferObject;
import net.scriptgate.softdev.android.entity.cube.IndexBufferObjectCreator;
import net.scriptgate.softdev.android.entity.cube.IndexBufferObjectCubeRenderer;
import net.scriptgate.softdev.android.entity.cube.IndexBufferObjects;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import static net.scriptgate.softdev.android.helper.TextureHelper.loadTexture;

public class World {

    private ModelMatrix modelMatrix;
    private ViewMatrix viewMatrix;
    private ProjectionMatrix projectionMatrix;
    private ModelViewProjectionMatrix mvpMatrix;

    private IndexBufferObjects indexBufferObjects;

    private IndexBufferObjectCubeRenderer cubeRenderer;

    private final ExecutorService singleThreadedExecutor = Executors.newSingleThreadExecutor();

    private GLSurfaceView glSurfaceView;

    public World(GLSurfaceView glSurfaceView) {
        this.glSurfaceView = glSurfaceView;
        modelMatrix = new ModelMatrix();

        float dist = 5f;
        Point3D eye = new Point3D(dist, dist, dist);
        Point3D look = new Point3D(0.0f, 0.0f, 0.0f);
        Point3D up = new Point3D(0.0f, 1.0f, 0.0f);
        viewMatrix = new ViewMatrix(eye, look, up);

        float zoom = 15.0f;
        projectionMatrix = new IsometricProjectionMatrix(10000.0f, zoom);

        mvpMatrix = new ModelViewProjectionMatrix();
    }

    public Point3D getPosition(Point2D screenPosition) {
        Ray ray = new Ray(modelMatrix, viewMatrix, projectionMatrix, mvpMatrix, screenPosition);
        return ray.intersectXZ();
    }

    public void translateView(Point3D point) {
        viewMatrix.translate(point);
    }

    public void scaleView(float scale) {
        viewMatrix.scale(new Point3D(scale, scale, scale));
    }

    public void draw() {
        cubeRenderer.render(indexBufferObjects);
    }

    public void onSurfaceChanged(int width, int height) {
        projectionMatrix.onSurfaceChanged(width, height);
    }

    public void onSurfaceCreated() {
        viewMatrix.onSurfaceCreated();
        //Instead of moving the cubeRenderer up (centering the origin), we're simply manipulating the viewMatrix
        viewMatrix.translate(new Point3D(-7500.0f, 0, -7500.0f));

        int textureHandle = loadTexture(glSurfaceView.getContext(), R.drawable.colormap);
        cubeRenderer = new IndexBufferObjectCubeRenderer(modelMatrix, viewMatrix, projectionMatrix, mvpMatrix);

        indexBufferObjects = new IndexBufferObjects(textureHandle);
    }

    public void release() {
        if (indexBufferObjects != null) {
            indexBufferObjects.release();
            indexBufferObjects = null;
        }
    }

    public void generateCubes(List<Event> events) {
        singleThreadedExecutor.submit(new GenDataRunnable(glSurfaceView, events));
    }

    public void allocateBuffer(final int numberOfEvents) {
        glSurfaceView.queueEvent(new Runnable() {
            @Override
            public void run() {
                indexBufferObjects.allocate(numberOfEvents);
            }
        });
    }

    public boolean hasRoom(int size) {
        return indexBufferObjects.hasRoom(size);
    }

    private class GenDataRunnable implements Runnable {
        private GLSurfaceView glSurfaceView;


        private List<Event> events;

        public GenDataRunnable(GLSurfaceView glSurfaceView, List<Event> events) {
            this.glSurfaceView = glSurfaceView;
            this.events = events;
        }

        @Override
        public void run() {

            if (events.size() > 0) {
                final IndexBufferObjectCreator data = new IndexBufferObjectCreator(events);

                // Run on the GL thread -- the same thread the other members of the renderer run in.
                glSurfaceView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        IndexBufferObject availableBuffer = indexBufferObjects.getBufferWithRoom(events.size());
                        availableBuffer.addData(data);
                    }
                });
            }
        }

    }
}
