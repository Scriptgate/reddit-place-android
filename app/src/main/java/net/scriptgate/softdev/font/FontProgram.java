package net.scriptgate.softdev.font;

import net.scriptgate.softdev.android.program.AttributeVariable;
import net.scriptgate.softdev.android.program.UniformVariable;

import static android.opengl.GLES30.*;

public class FontProgram {

    private int programHandle;

    public FontProgram(int programHandle) {
        this.programHandle = programHandle;
    }

    public int getProgramHandle() {
        return programHandle;
    }

    public int getHandle(UniformVariable uniformVariable) {
        return glGetUniformLocation(programHandle, uniformVariable.getName());
    }

    public int getHandle(AttributeVariable attributeVariable) {
        return glGetAttribLocation(programHandle, attributeVariable.getName());
    }
}
