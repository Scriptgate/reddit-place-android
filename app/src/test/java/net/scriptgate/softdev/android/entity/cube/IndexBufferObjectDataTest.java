package net.scriptgate.softdev.android.entity.cube;

import junit.framework.TestCase;

import net.scriptgate.softdev.android.common.Point3D;
import net.scriptgate.softdev.android.entity.Event;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class IndexBufferObjectDataTest extends TestCase {

    public void testNoEventsCreatesEmptyDataObject() throws Exception {
        IndexBufferObjectCreator iboCreator = new IndexBufferObjectCreator(Collections.<Event>emptyList());
        IndexBufferObjectData data = iboCreator.createData((short) 0);
        assertThat(data.indexBuffer.capacity()).isZero();
        assertThat(data.positionDataBuffer.capacity()).isZero();
        assertThat(data.textureDataBuffer.capacity()).isZero();
    }

    public void testConstructor_noOffset_Indices() throws Exception {
        List<Event> events = singletonList(new Event(new Point3D(5, 3, 8), 7));
        IndexBufferObjectCreator iboCreator = new IndexBufferObjectCreator(events);
        IndexBufferObjectData data = iboCreator.createData((short) 0);
        int[] expectedIndexBuffer = new int[]{
                0, 2, 1, 2, 3, 1,
                3, 6, 1, 6, 5, 1,
                5, 4, 1, 4, 0, 1
        };
        int[] actualIndexBuffer = new int[18];
        data.indexBuffer.get(actualIndexBuffer);
        assertThat(actualIndexBuffer).containsExactly(expectedIndexBuffer);
    }

    public void testConstructor_withOffset_Indices() throws Exception {
        List<Event> events = singletonList(new Event(new Point3D(5, 3, 8), 7));
        IndexBufferObjectCreator iboCreator = new IndexBufferObjectCreator(events);
        IndexBufferObjectData data = iboCreator.createData((short) 7);
        int[] expectedIndexBuffer = new int[]{
                 7,  9, 8,  9, 10, 8,
                10, 13, 8, 13, 12, 8,
                12, 11, 8, 11,  7, 8
        };
        int[] actualIndexBuffer = new int[18];
        data.indexBuffer.get(actualIndexBuffer);
        assertThat(actualIndexBuffer).containsExactly(expectedIndexBuffer);
    }
}