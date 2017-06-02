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

import android.content.Context;
import android.opengl.GLSurfaceView;

import net.scriptgate.softdev.android.common.Color;
import net.scriptgate.softdev.android.common.Point2D;
import net.scriptgate.softdev.android.common.Point3D;
import net.scriptgate.softdev.android.entity.Event;
import net.scriptgate.softdev.android.entity.RedditPlace;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES30.*;
import static net.scriptgate.softdev.android.common.Color.*;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 * <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 * <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 * <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class IsometricRenderer implements GLSurfaceView.Renderer {

    private static final Color BACKGROUND_COLOR = WHITE;

    private final Context activityContext;

    private World world;
    private UserInterface userInterface;

    private RedditPlace redditPlace;

    private long lastTime = System.currentTimeMillis();
    private boolean spawnCubes = false;

    public IsometricRenderer(final Context activityContext, World world) {
        this.activityContext = activityContext;

        userInterface = new UserInterface();
        this.redditPlace = new RedditPlace(activityContext);

        this.world = world;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        world.generateCubes(Collections.<Event>emptyList());

        glClearColor(BACKGROUND_COLOR.red, BACKGROUND_COLOR.green, BACKGROUND_COLOR.blue, BACKGROUND_COLOR.alpha);

        world.onSurfaceCreated();
        userInterface.onSurfaceCreated(activityContext);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        world.onSurfaceChanged(width, height);
        userInterface.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        long now = System.currentTimeMillis();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glDisable(GL_BLEND);


        final long EVENTS_PER_BATCH = 100;
        if(this.spawnCubes) {
            try {
//                long start = System.currentTimeMillis();
                //TODO: replay more events
                List<Event> events = redditPlace.readEvents(numberOfReplayedEvents, EVENTS_PER_BATCH);
//                long elapsedTimeMillis = System.currentTimeMillis() - start;
//                System.out.println("Loading events took " + elapsedTimeMillis + " ms.");
                if (world.hasRoom(events.size())) {
                    world.generateCubes(events);
                    numberOfReplayedEvents += EVENTS_PER_BATCH;
                } else {
//                    System.err.println("No available buffer for data with " + events.size() + " events!");
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        world.draw();

        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        long elapsedTimeInFrame = now - lastTime;
        int fps = (int) (1000 / elapsedTimeInFrame);
        userInterface.draw(fps, numberOfReplayedEvents);

        lastTime = now;
    }

    private Point3D movePosition = null;

    public void onMove(int x, int y) {
        switch (userInterface.getSelectedMode()) {
            case MOVE_VIEW:
                if (movePosition != null) {
                    Point3D newMovePosition = world.getPosition(new Point2D(x, y));

                    Point3D offset = Point3D.minus(newMovePosition, movePosition);
                    world.translateView(offset);
                }
                break;
        }
    }

    public void onDown(int x, int y) {
        if (userInterface.contains(x, y)) {
            userInterface.onDown(x, y);
        } else {
            switch (userInterface.getSelectedMode()) {
                case ADD_CUBE:
                   toggleSpawningCubes();
                    break;
                case MOVE_LIGHT:
                    world.allocateBuffer(4_000_000);
                case MOVE_VIEW:
                    movePosition = world.getPosition(new Point2D(x, y));
                    break;
            }
        }
    }

    private void toggleSpawningCubes() {
        this.spawnCubes = !this.spawnCubes;
    }

    public void onUp(int x, int y) {
        movePosition = null;
    }

    private static int numberOfReplayedEvents = 0;


    public void clear() {
        this.world.release();
    }

}