package net.scriptgate.softdev.android.entity.cube;

import net.scriptgate.softdev.android.entity.Event;

import java.nio.IntBuffer;
import java.util.List;

import static java.util.Arrays.asList;
import static net.scriptgate.softdev.android.entity.cube.IndexBufferObject.allocateIndexBuffer;

public class IndexDataBufferFactory {

    static IntBuffer createIndexData(List<Event> events, int indexOffset) {
        int numberOfCubes = events.size();
        return createIndexData(numberOfCubes, indexOffset);
    }

    static IntBuffer createIndexData(int numberOfCubes, int indexOffset) {

        IntBuffer indexBuffer = allocateIndexBuffer(numberOfCubes);

        int index = indexOffset;

        for (int i = 0; i < numberOfCubes; i++) {
            final int frontA = index++;
            final int frontB = index++;
            final int frontC = index++;
            final int frontD = index++;
            final int backA = index++;
            final int backB = index++;
            final int backD = index++;

            int[] frontFace = new int[]{frontA, frontB, frontC, frontD};
            int[] rightFace = new int[]{frontD, frontB, backD, backB};
            int[] topFace = new int[]{backB, frontB, backA, frontA};

            for (int[] face : asList(frontFace, rightFace, topFace)) {
                indexBuffer.put(face[0]);
                indexBuffer.put(face[2]);
                indexBuffer.put(face[1]);
                indexBuffer.put(face[2]);
                indexBuffer.put(face[3]);
                indexBuffer.put(face[1]);
            }
        }
        indexBuffer.position(0);
        return indexBuffer;
    }

}
