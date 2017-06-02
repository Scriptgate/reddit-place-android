package net.scriptgate.softdev.android;


import android.content.Context;

import net.scriptgate.softdev.android.common.Color;
import net.scriptgate.softdev.android.common.Point2D;
import net.scriptgate.softdev.android.common.Point3D;
import net.scriptgate.softdev.android.component.GuiProjectionMatrix;
import net.scriptgate.softdev.android.component.ModelMatrix;
import net.scriptgate.softdev.android.component.ModelViewProjectionMatrix;
import net.scriptgate.softdev.android.component.ProjectionMatrix;
import net.scriptgate.softdev.android.component.ViewMatrix;
import net.scriptgate.softdev.android.entity.Square;
import net.scriptgate.softdev.android.program.Program;
import net.scriptgate.softdev.font.Font;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static net.scriptgate.softdev.android.common.Color.WHITE;
import static net.scriptgate.softdev.android.component.ViewMatrix.createViewBehindOrigin;
import static net.scriptgate.softdev.android.entity.Square.createXYSquare;
import static net.scriptgate.softdev.android.entity.SquareDataFactory.generateTextureData;
import static net.scriptgate.softdev.android.entity.cube.IndexBufferObject.MAX_EVENTS;
import static net.scriptgate.softdev.android.helper.TextureHelper.loadTexture;
import static net.scriptgate.softdev.android.program.AttributeVariable.COLOR;
import static net.scriptgate.softdev.android.program.AttributeVariable.MVP_MATRIX;
import static net.scriptgate.softdev.android.program.AttributeVariable.POSITION;
import static net.scriptgate.softdev.android.program.AttributeVariable.TEXTURE_COORDINATE;
import static net.scriptgate.softdev.android.program.Program.createProgram;
import static net.scriptgate.softdev.font.FontBuilder.createFont;

public class UserInterface {

    private ModelMatrix modelMatrix;
    private ViewMatrix viewMatrix;
    private ProjectionMatrix projectionMatrix;
    private ModelViewProjectionMatrix mvpMatrix;

    private Program textureProgram;

    private int squareTextureHandle;
    private List<Square> squares = new ArrayList<>();

    private Font font;

    private float[] fontViewProjectionMatrix = new float[16];

    public enum Mode {
        ADD_CUBE,
        MOVE_LIGHT,
        MOVE_VIEW
    }

    public UserInterface() {
        modelMatrix = new ModelMatrix();
        viewMatrix = createViewBehindOrigin();
        projectionMatrix = new GuiProjectionMatrix(10.0f);
        mvpMatrix = new ModelViewProjectionMatrix();

        Point2D offset = new Point2D(30, 30);
        Point2D size = new Point2D(128, 128);

        float padding = 15;

        Square placeCubeMenuItem = createXYSquare(Mode.ADD_CUBE, WHITE,
                new Point3D(offset.x, offset.y, 0.0f),
                generateTextureData(0.5f, 0.25f, new Point2D()),
                generateTextureData(0.5f, 0.25f, new Point2D(0.5f, 0)),
                size.x, size.y);
        Square moveLightMenuItem = createXYSquare(Mode.MOVE_LIGHT, WHITE,
                new Point3D(offset.x, offset.y + size.y + padding, 0.0f),
                generateTextureData(0.5f, 0.25f, new Point2D(0, 0.25f)),
                generateTextureData(0.5f, 0.25f, new Point2D(0.5f, 0.25f)),
                size.x, size.y);
        Square moveViewMenuItem = createXYSquare(Mode.MOVE_VIEW, WHITE,
                new Point3D(offset.x, offset.y + (size.y + padding) * 2, 0.0f),
                generateTextureData(0.5f, 0.25f, new Point2D(0, 0.5f)),
                generateTextureData(0.5f, 0.25f, new Point2D(0.5f, 0.5f)),
                size.x, size.y);
        moveViewMenuItem.setSelected(true);


        squares.add(placeCubeMenuItem);
        squares.add(moveLightMenuItem);
        squares.add(moveViewMenuItem);
    }

    public void onSurfaceCreated(Context activityContext) {
        viewMatrix.onSurfaceCreated();
        squareTextureHandle = loadTexture(activityContext, R.drawable.menu);
        textureProgram = createProgram("texture_vertex_shader", "texture_fragment_shader", asList(POSITION, COLOR));

        Program fontProgram = createProgram("batch_vertex_shader", "batch_fragment_shader", asList(POSITION, TEXTURE_COORDINATE, MVP_MATRIX));
        font = createFont()
                .program(fontProgram.getHandle())
                .assets(activityContext.getAssets())
                .font("Roboto-Regular.ttf")
                .size(40)
                .build();
    }

    public void onSurfaceChanged(int width, int height) {
        projectionMatrix.onSurfaceChanged(width, height);
    }

    public void draw(int fps, int numberOfEvents) {
        textureProgram.useForRendering();

        for (Square square : squares) {
            square.draw(textureProgram, modelMatrix, viewMatrix, projectionMatrix, mvpMatrix, squareTextureHandle);
        }

        projectionMatrix.multiplyWithMatrixAndStore(viewMatrix.getMatrix(), fontViewProjectionMatrix);

        Color fontColor = Color.BLACK;
        font.begin(fontColor.red, fontColor.green, fontColor.blue, fontColor.alpha, fontViewProjectionMatrix);
        {
            font.startDrawing("FPS: " + fps).at(188, 30).draw();
            font.startDrawing("Events: " + numberOfEvents).at(188, 30 + font.getScaledCharHeight()).draw();
            final int bufferCapacityPercentage = (int) Math.floor((numberOfEvents * 1.0f / MAX_EVENTS) * 100.0f);
            font.startDrawing("Buffer: " + bufferCapacityPercentage + "%").at(188, 30 + font.getScaledCharHeight() * 2).draw();
        }
        font.end();
    }

    public UserInterface.Mode getSelectedMode() {
        for (Square square : squares) {
            if (square.isSelected()) {
                return square.getMode();
            }
        }
        throw new IllegalStateException("No mode selected");
    }

    public void onDown(int x, int y) {
        for (Square square : squares) {
            square.setSelected(square.contains(x, y));
        }

    }

    public boolean contains(int x, int y) {
        for (Square square : squares) {
            if (square.contains(x, y)) {
                return true;
            }
        }
        return false;
    }
}
