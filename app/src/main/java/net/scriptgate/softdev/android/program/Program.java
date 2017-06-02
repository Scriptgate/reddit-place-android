package net.scriptgate.softdev.android.program;

import java.util.List;

import static android.opengl.GLES30.*;
import static net.scriptgate.softdev.android.helper.ShaderHelper.createAndLinkProgram;
import static net.scriptgate.softdev.android.helper.ShaderHelper.loadShader;
import static net.scriptgate.softdev.android.program.AttributeVariable.toStringArray;

public class Program {

    private int handle;

    private Program(int programHandle) {
        this.handle = programHandle;
    }

    public static Program createProgram(String vertexShaderResource, String fragmentShaderResource, List<AttributeVariable> attributes) {

        int vertexShaderHandle = loadShader(GL_VERTEX_SHADER, vertexShaderResource);
        int fragmentShaderHandle = loadShader(GL_FRAGMENT_SHADER, fragmentShaderResource);

        // Create a program object and store the handle to it.
        int programHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, toStringArray(attributes));
        return new Program(programHandle);
    }

    public int getHandle(AttributeVariable attribute) {
        return glGetAttribLocation(handle, attribute.getName());
    }

    public int getHandle(UniformVariable uniform) {
        return glGetUniformLocation(handle, uniform.getName());
    }

    public void useForRendering() {
        // Tell OpenGL to use this program when rendering.
        glUseProgram(handle);
    }

    @Deprecated
    public int getHandle() {
        return handle;
    }
}
