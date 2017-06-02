package net.scriptgate.softdev.android.helper;

import junit.framework.TestCase;

import java.nio.IntBuffer;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class FloatBufferHelperTest extends TestCase {

    public void testAllocateIntBuffer() throws Exception {
        for (int i = 0; i < 10; i++) {
            IntBuffer buffer = FloatBufferHelper.allocateIntBuffer(i);
            assertThat(buffer.capacity()).isEqualTo(i);
        }
    }
}