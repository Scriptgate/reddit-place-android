package net.scriptgate.softdev.android;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import net.scriptgate.softdev.android.common.Point2D;

public class MainGLView extends GLSurfaceView {

    private IsometricRenderer renderer;
    private ZoomController zoomController;

    public void setZoomController(ZoomController zoomController) {
        this.zoomController = zoomController;
    }

    private enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    private Mode mode = Mode.NONE;

    float oldDistance = 0.0f;

    public MainGLView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = Mode.DRAG;
                queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        renderer.onDown(x, y);
                    }
                });
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDistance = getDistanceBetweenMultiTouchEvents(event);
                if (oldDistance > 10f) {
                    mode = Mode.ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if(mode == Mode.ZOOM) {
                    queueEvent(new Runnable() {
                        @Override
                        public void run() {zoomController.setZoom();}
                    });
                } else {
                    queueEvent(new Runnable() {
                        @Override
                        public void run() {
                            renderer.onUp(x, y);
                        }
                    });
                }
                mode = Mode.NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == Mode.DRAG) {
                    queueEvent(new Runnable() {
                        @Override
                        public void run() {
                            renderer.onMove(x, y);
                        }
                    });
                } else if (mode == Mode.ZOOM) {
                    float newDistance = getDistanceBetweenMultiTouchEvents(event);
                    if (newDistance > 10f) {
                        final float zoom = newDistance / oldDistance;
                        final Point2D zoomCenter = getCenterBetweenMultiTouchEvents(event);
                        queueEvent(new Runnable() {
                            @Override
                            public void run() {zoomController.updateZoom(zoom, zoomCenter);}
                        });
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private Point2D getCenterBetweenMultiTouchEvents(MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        return new Point2D(x/2, y/2);
    }

    private float getDistanceBetweenMultiTouchEvents(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }


    // Hides superclass method.
    public void setRenderer(IsometricRenderer renderer) {
        this.renderer = renderer;
        super.setRenderer(renderer);
    }
}