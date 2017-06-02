package net.scriptgate.softdev.android.entity.cube;


import net.scriptgate.softdev.android.entity.Event;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.List;

import java8.util.function.IntFunction;
import java8.util.function.Supplier;

import static net.scriptgate.softdev.android.entity.cube.VertexDataBufferFactory.createPositionData;
import static net.scriptgate.softdev.android.entity.cube.VertexDataBufferFactory.createTextureData;

public class IndexBufferObjectCreator {

    private final Supplier<FloatBuffer> positionDataBufferSupplier;
    private final Supplier<ShortBuffer> textureDataBufferSupplier;
    private final IntFunction<IntBuffer> indexBufferSupplier;
    public final int numberOfCubes;

    public IndexBufferObjectCreator(final List<Event> events) {
        positionDataBufferSupplier = new Supplier<FloatBuffer>() {
            @Override
            public FloatBuffer get() {return createPositionData(events);
            }
        };
        textureDataBufferSupplier = new Supplier<ShortBuffer>() {
            @Override
            public ShortBuffer get() {return createTextureData(events);
            }
        };
        indexBufferSupplier = new IntFunction<IntBuffer>() {
            @Override
            public IntBuffer apply(int offset) {return IndexDataBufferFactory.createIndexData(events, offset);
            }
        };
        this.numberOfCubes = events.size();
    }

    public IndexBufferObjectData createData(int indexOffset) {
        FloatBuffer positionDataBuffer = positionDataBufferSupplier.get();
        ShortBuffer textureDataBuffer = textureDataBufferSupplier.get();
        IntBuffer indexBuffer = indexBufferSupplier.apply(indexOffset);
        return new IndexBufferObjectData(positionDataBuffer, textureDataBuffer, indexBuffer, numberOfCubes);
    }
}

