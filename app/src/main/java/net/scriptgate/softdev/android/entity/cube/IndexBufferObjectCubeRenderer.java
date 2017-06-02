package net.scriptgate.softdev.android.entity.cube;

import net.scriptgate.softdev.android.component.ModelMatrix;
import net.scriptgate.softdev.android.component.ModelViewProjectionMatrix;
import net.scriptgate.softdev.android.component.ProjectionMatrix;
import net.scriptgate.softdev.android.component.ViewMatrix;
import net.scriptgate.softdev.android.program.Program;

import static android.opengl.GLES30.*;
import static java.util.Arrays.asList;
import static net.scriptgate.softdev.android.program.AttributeVariable.POSITION;
import static net.scriptgate.softdev.android.program.AttributeVariable.TEXTURE_COORDINATE;
import static net.scriptgate.softdev.android.program.UniformVariable.MVP_MATRIX;
import static net.scriptgate.softdev.android.program.UniformVariable.TEXTURE;

public class IndexBufferObjectCubeRenderer {


    private final Program program;
    private final ModelMatrix modelMatrix;
    private final ViewMatrix viewMatrix;
    private final ProjectionMatrix projectionMatrix;
    private final ModelViewProjectionMatrix mvpMatrix;

    public IndexBufferObjectCubeRenderer(ModelMatrix modelMatrix, ViewMatrix viewMatrix, ProjectionMatrix projectionMatrix, ModelViewProjectionMatrix mvpMatrix) {
        this.program = Program.createProgram("vbo_vertex_shader", "vbo_fragment_shader", asList(POSITION, TEXTURE_COORDINATE));
        this.modelMatrix = modelMatrix;
        this.viewMatrix = viewMatrix;
        this.projectionMatrix = projectionMatrix;
        this.mvpMatrix = mvpMatrix;
    }

    public void render(IndexBufferObjects cubes) {
        program.useForRendering();

        modelMatrix.setIdentity();

        mvpMatrix.multiply(modelMatrix, viewMatrix, projectionMatrix);
        mvpMatrix.passTo(program.getHandle(MVP_MATRIX));

        if (cubes != null) {
            cubes.render(program);
        }
    }
}
