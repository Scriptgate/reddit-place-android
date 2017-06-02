package net.scriptgate.softdev.android.entity.cube;


import net.scriptgate.softdev.android.common.Point2D;
import net.scriptgate.softdev.android.common.Point3D;
import net.scriptgate.softdev.android.entity.Event;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.List;

import java8.util.function.Function;
import java8.util.stream.Stream;

import static java.util.Arrays.asList;
import static java8.util.stream.StreamSupport.stream;
import static net.scriptgate.softdev.android.entity.Event.toColorIndex;
import static net.scriptgate.softdev.android.entity.Event.toPosition;
import static net.scriptgate.softdev.android.entity.cube.IndexBufferObject.allocatePositionDataBuffer;
import static net.scriptgate.softdev.android.entity.cube.IndexBufferObject.allocateTextureDataBuffer;
import static net.scriptgate.softdev.android.entity.cube.TextureTriangle.toTextureTriangle;
import static net.scriptgate.softdev.android.helper.FloatBufferHelper.putPoint2DIn;
import static net.scriptgate.softdev.android.helper.FloatBufferHelper.putPoint3DIn;

public class VertexDataBufferFactory {

    static ShortBuffer createPositionData(List<Event> events) {
        ShortBuffer positionDataBuffer = allocatePositionDataBuffer(events.size());

        stream(events)
                .map(toPosition())
                .flatMap(positionToVertices())
                .forEach(putPoint3DIn(positionDataBuffer));

        positionDataBuffer.position(0);
        return positionDataBuffer;
    }

    private static Function<Point3D, Stream<Point3D>> positionToVertices() {
        final float width = 5;
        final float height = 1;
        final float depth = 5;

        return new Function<Point3D, Stream<Point3D>>() {
            @Override
            public Stream<Point3D> apply(Point3D position) {
                //@formatter:off
                final Point3D frontA = new Point3D(position.x,         position.y + height, position.z + depth);
                final Point3D frontB = new Point3D(position.x + width, position.y + height, position.z + depth);
                final Point3D frontC = new Point3D(position.x,         position.y,          position.z + depth);
                final Point3D frontD = new Point3D(position.x + width, position.y,          position.z + depth);
                final Point3D backA  = new Point3D(position.x,         position.y + height, position.z);
                final Point3D backB  = new Point3D(position.x + width, position.y + height, position.z);
                final Point3D backD  = new Point3D(position.x + width, position.y,          position.z);
                //@formatter:on
                return stream(asList(frontA, frontB, frontC, frontD, backA, backB, backD));
            }
        };
    }

    static ShortBuffer createTextureData(List<Event> events) {
        final ShortBuffer textureDataBuffer = allocateTextureDataBuffer(events.size());

        stream(events)
                .mapToInt(toColorIndex())
                .mapToObj(toTextureTriangle())
                .flatMap(textureToVertices())
                .forEach(putPoint2DIn(textureDataBuffer));

        textureDataBuffer.position(0);
        return textureDataBuffer;
    }

    private static Function<TextureTriangle, Stream<Point2D>> textureToVertices() {
        return new Function<TextureTriangle, Stream<Point2D>>() {
            @Override
            public Stream<Point2D> apply(TextureTriangle texture) {
                final Point2D frontA = texture.p1;
                final Point2D frontB = texture.p2;
                final Point2D frontC = texture.p3;
                final Point2D frontD = texture.p1;
                final Point2D backA = texture.p3;
                final Point2D backB = texture.p1;
                final Point2D backD = texture.p3;

                return stream(asList(frontA, frontB, frontC, frontD, backA, backB, backD));
            }
        };
    }

}
