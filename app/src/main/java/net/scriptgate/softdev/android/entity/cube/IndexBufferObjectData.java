package net.scriptgate.softdev.android.entity.cube;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class IndexBufferObjectData {
    final ShortBuffer positionDataBuffer;
    final ShortBuffer textureDataBuffer;
    final IntBuffer indexBuffer;
    final int numberOfCubes;

    public IndexBufferObjectData(ShortBuffer positionDataBuffer, ShortBuffer textureDataBuffer, IntBuffer indexBuffer, int numberOfCubes) {
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

