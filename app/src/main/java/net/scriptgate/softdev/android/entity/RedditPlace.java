package net.scriptgate.softdev.android.entity;


import android.content.Context;
import android.util.Log;

import net.scriptgate.softdev.android.common.Point3D;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

public class RedditPlace {

    /**
     * Used for debug logs. max 23 characters
     */
    private static final String TAG = "RedditPlace";

    private static final long TOTAL_NUMBER_OF_EVENTS = 16_559_897;

    private Context activityContext;

    public RedditPlace(Context context) {
        this.activityContext = context;
    }

    public List<Event> readEvents(long from, long limit) throws IOException {
        Log.d(TAG, "Loading binary event data...");

        long eventsRead = 0;

        List<Event> events = new ArrayList<>();

        try (InputStream eventStream = activityContext.getAssets().open("3d_diffs.bin")) {
            Log.d(TAG, "Skipping " + from + " events");
            eventStream.skip(from * 16);
            while (eventStream.available() != 0 && from + eventsRead < TOTAL_NUMBER_OF_EVENTS && eventsRead < limit) {

                float x = readUInt32AsFloat(eventStream);
                float y = readUInt32AsFloat(eventStream);
                float z = readUInt32AsFloat(eventStream);
                int color = readUInt32AsInt(eventStream);

                if(5*x > Short.MAX_VALUE || 5*z-5000 > Short.MAX_VALUE || 5*y > Short.MAX_VALUE) {
                    System.err.println("Skipping event "+x+", "+y+", "+z+ " because it was out of bounds.");
                    continue;
                }
                events.add(new Event(new Point3D(5*x, 5*z-5000, 5*y), color));
                eventsRead++;
            }
        }
//        System.out.println(eventsRead + " events loaded. Let's roll bitches.");

        return events;
    }

    private static float readUInt32AsFloat(InputStream is) throws IOException {
        byte[] data = new byte[4];
        is.read(data, 0, data.length);
        return ByteBuffer.wrap(data).order(LITTLE_ENDIAN).getFloat();
    }

    private static int readUInt32AsInt(InputStream is) throws IOException {
        byte[] data = new byte[4];
        is.read(data, 0, data.length);
        return ByteBuffer.wrap(data).order(LITTLE_ENDIAN).getInt();
    }
}
