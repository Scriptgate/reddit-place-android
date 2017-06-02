package net.scriptgate.softdev.android.entity.cube;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class IndexBufferObjectData {
    final FloatBuffer positionDataBuffer;
    final FloatBuffer textureDataBuffer;
    final IntBuffer indexBuffer;
    final int numberOfCubes;

    public IndexBufferObjectData(FloatBuffer positionDataBuffer, FloatBuffer textureDataBuffer, IntBuffer indexBuffer, int numberOfCubes) {

        this.positionDataBuffer = positionDataBuffer;
        this.textureDataBuffer = textureDataBuffer;
        this.indexBuffer = indexBuffer;
        this.numberOfCubes = numberOfCubes;
    }

    public void release() {
        positionDataBuffer.limit(0);
        textureDataBuffer.limit(0);
        indexBuffer.limit(0);
    }
}

