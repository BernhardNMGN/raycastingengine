package resources.segments;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import settings.Settings;

public abstract class Segment {


    private Color lineColor;
    private Color fillColor;
    private double lineThickness;
    private Point2D startCoords;
    private double segmentSize;
    private BoundingBox outerBoundingBox;
    private BoundingBox innerBoundingBox;

    private ImagePattern texture; // todo: implement

    protected int[][] textureArray;
    protected Color[] textureArrayColors;

    protected Segment(double segmentSize, double x, double y) {
        this.startCoords = new Point2D(x, y);
        this.segmentSize = segmentSize;
        this.outerBoundingBox = new BoundingBox(x, y, segmentSize, segmentSize);
        this.innerBoundingBox = new BoundingBox(x+lineThickness, y+lineThickness, segmentSize - (2 * lineThickness), segmentSize - (2 * lineThickness));
        setDefaultColors();
        this.lineThickness = segmentSize/20.;
        initializeTextureArray();
    }

    protected Segment(double segmentSize) {
        this.segmentSize = segmentSize;
    }

    protected abstract void setDefaultColors();

    protected abstract void initializeTextureArray();

    public void draw(GraphicsContext gc, double x, double y) {
        gc.setStroke(lineColor);
        gc.setFill(fillColor);
        gc.fillRect(x, y ,segmentSize, segmentSize);
        gc.fillRect(x, y, segmentSize, segmentSize);
    }

    public void draw(GraphicsContext gc) {
        draw(gc, startCoords.getX(), startCoords.getY());
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public double getSegmentSize() {
        return segmentSize;
    }

    public void setSegmentSize(double segmentSize) {
        this.segmentSize = segmentSize;
    }

    public Point2D getStartCoords() {
        return startCoords;
    }

    public BoundingBox getOuterBoundingBox() {
        return outerBoundingBox;
    }

    public Color getColorByIndex(Point2D entrancePoint) {
        if(texture == null) {
            if(innerBoundingBox.contains(entrancePoint))
                return fillColor;
            else return lineColor;
        }
        else return fillColor; //todo: implement getting argb from texture;
    }

    public int[][] getTextureArray() {
        return textureArray;
    }

    public Color[] getTextureArrayColors() {
        return textureArrayColors;
    }
}
