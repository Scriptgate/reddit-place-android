package net.scriptgate.softdev.android.helper;

import android.util.Log;

import static android.opengl.GLES30.*;
import static net.scriptgate.softdev.android.helper.RawResourceHelper.readShaderFileFromResource;

public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    public static int loadShader(int shaderType, String resource) {
        final String shaderSource = readShaderFileFromResource(resource);
        return compileShader(shaderType, shaderSource);
    }

    /**
     * Helper function to compile a shader.
     *
     * @param shaderType   The shader type.
     * @param shaderSource The shader source code.
     * @return An OpenGL handle to the shader.
     */
    public static int compileShader(final int shaderType, final String shaderSource) {
        int shaderHandle = glCreateShader(shaderType);

        if (shaderHandle != 0) {
            // Pass in the shader source.
            glShaderSource(shaderHandle, shaderSource);

            // Compile the shader.
            glCompileShader(shaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            glGetShaderiv(shaderHandle, GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                Log.e(TAG, "Error compiling shader: " + glGetShaderInfoLog(shaderHandle));
                glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }

        if (shaderHandle == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shaderHandle;
    }

    /**
     * Helper function to compile and link a program.
     *
     * @param vertexShaderHandle   An OpenGL handle to an already-compiled vertex shader.
     * @param fragmentShaderHandle An OpenGL handle to an already-compiled fragment shader.
     * @param attributes           Attributes that need to be bound to the program.
     * @return An OpenGL handle to the program.
     */
    public static int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle, final String[] attributes) {
        int programHandle = glCreateProgram();

        if (programHandle != 0) {
            // Bind the vertex shader to the program.
            glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            glAttachShader(programHandle, fragmentShaderHandle);

            // Bind attributes
            if (attributes != null) {
                final int size = attributes.length;
                for (int i = 0; i < size; i++) {
                    glBindAttribLocation(programHandle, i, attributes[i]);
                }
            }

            // Link the two shaders together into a program.
            glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            glGetProgramiv(programHandle, GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                Log.e(TAG, "Error compiling program: " + glGetProgramInfoLog(programHandle));
                glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0) {
            throw new RuntimeException("Error creating program.");
        }

        return programHandle;
    }
}
