package raycasting;

import javafx.geometry.Point2D;
import resources.segments.Segment;

public class RayResult {

    private double distance;
    private Segment segment;
    private Point2D entrancePoint;
    private double relativeXPos;
    private double angle;

    private int textureX;

    public RayResult(double distance, Segment segment, Point2D entrancePoint, double relativeXPos, double angle) {
        this.distance = distance;
        this.segment = segment;
        this.entrancePoint = entrancePoint;
        this.relativeXPos = relativeXPos;
        this.angle = angle;
    }

    public RayResult(double distance, Segment segment, int textureX) {
        this.distance = distance;
        this.segment = segment;
        this.textureX = textureX;
    }

    public double getDistance() {
        return distance;
    }

    public Segment getSegment() {
        return segment;
    }

    public Point2D getEntrancePoint() {
        return entrancePoint;
    }

    public double getRelativeXPos() {
        return relativeXPos;
    }

    public int getTextureX() {
        return textureX;
    }
}
