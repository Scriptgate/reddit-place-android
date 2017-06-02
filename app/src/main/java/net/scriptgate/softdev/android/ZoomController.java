package net.scriptgate.softdev.android;


import net.scriptgate.softdev.android.common.Point2D;
import net.scriptgate.softdev.android.common.Point3D;

import static net.scriptgate.softdev.android.common.Point3D.scalarProduct;

public class ZoomController {

    private static final float MAX_ZOOM = 7;
    private static final float MIN_ZOOM = 0.01f;

    private float zoom = 1.0f;
    private float lastZoom = 1.0f;

    private World world;

    public ZoomController(World world) {
        this.world = world;
    }

    public void setZoom() {
        zoom = zoom * lastZoom;
        lastZoom = 1.0f;
    }

    public void updateZoom(float newZoom, Point2D zoomCenter) {
        if (this.zoom * newZoom < MIN_ZOOM || this.zoom * newZoom > MAX_ZOOM) {
            return;
        }

        Point3D zoomCenterWorld = world.getPosition(zoomCenter);

        world.translateView(zoomCenterWorld);
        {
            world.scaleView(newZoom / lastZoom);
            lastZoom = newZoom;
        }
        world.translateView(scalarProduct(-1, zoomCenterWorld));
    }

    public void release() {
        this.zoom = 1.0f;
    }
}
