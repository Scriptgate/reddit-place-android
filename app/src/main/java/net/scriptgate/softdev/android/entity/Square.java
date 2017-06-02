package net.scriptgate.softdev.android.entity;

import net.scriptgate.softdev.android.UserInterface;
import net.scriptgate.softdev.android.common.Color;
import net.scriptgate.softdev.android.common.Point3D;
import net.scriptgate.softdev.android.component.ModelMatrix;
import net.scriptgate.softdev.android.component.ModelViewProjectionMatrix;
import net.scriptgate.softdev.android.component.ProjectionMatrix;
import net.scriptgate.softdev.android.component.ViewMatrix;
import net.scriptgate.softdev.android.program.AttributeVariable;
import net.scriptgate.softdev.android.program.Program;

import java.nio.FloatBuffer;

import static android.opengl.GLES30.*;
import static net.scriptgate.softdev.android.common.Face.ELEMENTS_PER_FACE;
import static net.scriptgate.softdev.android.entity.SquareDataFactory.generateXYPositionData;
import static net.scriptgate.softdev.android.helper.FloatBufferHelper.allocateBuffer;
import static net.scriptgate.softdev.android.program.UniformVariable.MVP_MATRIX;
import static net.scriptgate.softdev.android.program.UniformVariable.TEXTURE;

public class Square {

    private FloatBuffer verticesBuffer;
    private FloatBuffer textureCoordinateBuffer;
    private FloatBuffer selectedTextureCoordinateBuffer;

    private boolean selected = false;

    private UserInterface.Mode mode;
    private Color color;
    private Point3D position;
    private float width;
    private float height;

    public static Square createXYSquare(UserInterface.Mode mode, Color color, Point3D position, float[] textureData, float[] selectedTextureData, float width, float height) {
        FloatBuffer verticesBuffer = allocateBuffer(generateXYPositionData(width, height));
        FloatBuffer textureCoordinateBuffer = allocateBuffer(textureData);
        FloatBuffer selectedTextureCoordinateBuffer = allocateBuffer(selectedTextureData);
        return new Square(mode, color, position, width, height, verticesBuffer, textureCoordinateBuffer, selectedTextureCoordinateBuffer);
    }

    private Square(UserInterface.Mode mode, Color color, Point3D position, float width, float height, FloatBuffer verticesBuffer, FloatBuffer textureCoordinateBuffer, FloatBuffer selectedTextureCoordinateBuffer) {
        this.mode = mode;
        this.color = color;
        this.position = position;
        this.width = width;
        this.height = height;
        this.verticesBuffer = verticesBuffer;
        this.textureCoordinateBuffer = textureCoordinateBuffer;
        this.selectedTextureCoordinateBuffer = selectedTextureCoordinateBuffer;
    }

    public void draw(Program program, ModelMatrix modelMatrix, ViewMatrix viewMatrix, ProjectionMatrix projectionMatrix, ModelViewProjectionMatrix mvpMatrix, int textureHandle) {
        modelMatrix.setIdentity();
        modelMatrix.translate(position);

        mvpMatrix.multiply(modelMatrix, viewMatrix, projectionMatrix);
        mvpMatrix.passTo(program.getHandle(MVP_MATRIX));

        setTexture(program, textureHandle);

        passPositionData(program);
        passColorData(program);
        passTextureData(program);

        glDrawArrays(GL_TRIANGLES, 0, ELEMENTS_PER_FACE);
    }

    private void setTexture(Program program, int textureHandle) {
        // Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, textureHandle);
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        glUniform1i(program.getHandle(TEXTURE), 0);
    }

    private void passPositionData(Program program) {
        int positionHandle = program.getHandle(AttributeVariable.POSITION);
        verticesBuffer.position(0);
        glVertexAttribPointer(positionHandle, 3, GL_FLOAT, false, 0, verticesBuffer);
        glEnableVertexAttribArray(positionHandle);
    }

    private void passColorData(Program program) {
        int colorHandle = program.getHandle(AttributeVariable.COLOR);
        glVertexAttrib4fv(colorHandle, color.toArray(), 0);
        glDisableVertexAttribArray(colorHandle);
    }

    private void passTextureData(Program program) {
        int textureCoordinateHandle = program.getHandle(AttributeVariable.TEXTURE_COORDINATE);
        if (selected) {
            selectedTextureCoordinateBuffer.position(0);
            glVertexAttribPointer(textureCoordinateHandle, 2, GL_FLOAT, false, 0, selectedTextureCoordinateBuffer);

        } else {
            textureCoordinateBuffer.position(0);
            glVertexAttribPointer(textureCoordinateHandle, 2, GL_FLOAT, false, 0, textureCoordinateBuffer);
        }
        glEnableVertexAttribArray(textureCoordinateHandle);
    }

    public boolean contains(int x, int y) {
        return x > position.x && x < position.x + width
                && y > position.y && y < position.y + height;
    }

    public float getHeight() {
        return height;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public UserInterface.Mode getMode() {
        return mode;
    }
}