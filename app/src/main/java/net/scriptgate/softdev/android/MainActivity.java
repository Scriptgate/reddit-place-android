/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.scriptgate.softdev.android;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;

public class MainActivity extends Activity {

    private MainGLView view;
    private IsometricRenderer renderer;
    private ZoomController zoomController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        view = (MainGLView) findViewById(R.id.glView);

        // Check if the system supports OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2) {
            // Request an OpenGL ES 2.0 compatible context.
            view.setEGLContextClientVersion(2);

            // Set the renderer to our demo renderer, defined below.
            World world = new World(view);
            zoomController = new ZoomController(world);
            renderer = new IsometricRenderer(this, world);
            view.setRenderer(renderer);
            view.setZoomController(zoomController);
        } else {
            // This is where you could create an OpenGL ES 1.x compatible
            // renderer if you wanted to support both ES 1, ES 2 and ES 3.
            throw new UnsupportedOperationException("OpenGL version: "+configurationInfo.getGlEsVersion()+", not supported: "+configurationInfo.reqGlEsVersion);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        deallocateMemory();
        view.onPause();
    }

    private void deallocateMemory() {
        view.queueEvent(new Runnable() {
            @Override
            public void run() {
                renderer.clear();
                zoomController.release();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        allocateMemory();
        view.onResume();
    }

    private void allocateMemory() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}