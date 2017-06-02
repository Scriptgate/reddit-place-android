package net.scriptgate.softdev.android.entity;


import net.scriptgate.softdev.android.common.Point3D;

import java8.util.function.Function;
import java8.util.function.ToIntFunction;

public class Event {

    public final Point3D position;
    public final int colorIndex;

    public Event(Point3D position, int colorIndex) {
        this.position = position;
        this.colorIndex = colorIndex;
    }

    public static ToIntFunction<Event> toColorIndex() {
        return new ToIntFunction<Event>() {
            @Override
            public int applyAsInt(Event event) {
                return event.colorIndex;
            }
        };
    }

    public static Function<Event, Point3D> toPosition() {
        return new Function<Event, Point3D>() {
            @Override
            public Point3D apply(Event event) {
                return event.position;
            }
        };
    }
}
