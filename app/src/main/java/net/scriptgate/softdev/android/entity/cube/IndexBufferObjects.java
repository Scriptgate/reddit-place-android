package net.scriptgate.softdev.android.entity.cube;


import net.scriptgate.softdev.android.program.Program;

import java.util.ArrayList;
import java.util.List;

import java8.util.function.Predicate;

import static android.opengl.GLES20.*;
import static java8.util.stream.StreamSupport.stream;
import static net.scriptgate.softdev.android.program.UniformVariable.TEXTURE;

public class IndexBufferObjects {

    /**
     * Used for debug logs. max 23 characters
     */
    private static final String TAG = "IndexBufferObjects";

    private List<IndexBufferObject> buffers;
    private int textureHandle;

    public IndexBufferObjects(int textureHandle) {
        this.textureHandle = textureHandle;
        buffers = new ArrayList<>();
    }

    void render(Program program) {
        setTexture(program);
        for (IndexBufferObject buffer : buffers) {
            buffer.render(program);
        }
    }

    private void setTexture(Program program) {
        // Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, textureHandle);
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        glUniform1i(program.getHandle(TEXTURE), 0);
    }

    public void release() {
        for (IndexBufferObject buffer : buffers) {
            buffer.release();
        }
    }

    public IndexBufferObject getBufferWithRoom(int size) {
        for (IndexBufferObject buffer : buffers) {
            if (buffer.hasRoom(size)) {
                return buffer;
            }
        }
        return null;
    }

    public void allocate(int numberOfEvents) {
        buffers.add(IndexBufferObject.allocate(numberOfEvents));
    }

    public boolean hasRoom(final int size) {
        return stream(buffers).anyMatch(new Predicate<IndexBufferObject>() {
            @Override
            public boolean test(IndexBufferObject indexBufferObject) {return indexBufferObject.hasRoom(size);}
        });
    }

}
