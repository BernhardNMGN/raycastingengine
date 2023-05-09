package resources.segments;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PlayerStartFloor extends Segment{

    private Point2D playerStartCoords;

    public PlayerStartFloor(double segmentSize, double x, double y) {
        super(segmentSize, x, y);
        this.playerStartCoords = new Point2D(x + segmentSize/2., y + segmentSize/2.);
    }

    public PlayerStartFloor(double segmentSize) {
        super(segmentSize);
    }

    @Override
    protected void setDefaultColors() {
        setFillColor(Color.LIGHTGRAY);
        setLineColor(Color.DARKGRAY);
    }

    @Override
    protected void initializeTextureArray() {

    }

    public Point2D getPlayerStartCoords() {
        return playerStartCoords;
    }
}
