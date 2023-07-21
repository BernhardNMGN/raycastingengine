package raycasting;

import javafx.geometry.Point2D;
import resources.segments.Segment;
import resources.segments.Wall;
import settings.Settings;

public class RayResult {

    private double distance;
    private Wall wallSegment;
    private Point2D entrancePoint;
    private double relativeXPos;
    private double angle;

    private int textureX;

    private double floorXWall;

    private double floorYWall;

    public RayResult(double distance, Wall wallSegment, Point2D entrancePoint, double relativeXPos, double angle) {
        this.distance = distance;
        this.wallSegment = wallSegment;
        this.entrancePoint = entrancePoint;
        this.relativeXPos = relativeXPos;
        this.angle = angle;

    }

    public RayResult(double distance, Wall wallSegment, int textureX, double[] floorSegmentStart) {
        this.distance = distance;
        this.wallSegment = wallSegment;
        this.textureX = textureX;
        this.floorXWall = floorSegmentStart[0];
        this.floorYWall = floorSegmentStart[1];
    }

    public double getDistance() {
        return distance;
    }

    public Wall getWallSegment() {
        return wallSegment;
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

    public double getFloorXWall() {
        return floorXWall;
    }

    public double getFloorYWall() {
        return floorYWall;
    }
}
