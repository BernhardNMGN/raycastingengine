package raycasting;

import javafx.geometry.Point2D;
import resources.Axis;
import resources.map.GameMap;
import resources.segments.Segment;
import resources.segments.Wall;
import settings.Settings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.*;
import static renderers.utilities.GeometryCalculator.incrementAngle;

public class RayCaster {

    private GameMap map;

    private double leftAngleBound;
    private double rightAngleBound;

    private double segmentSize;
    private final double offset;

    private double maxDistance;
    
    private List<List<Segment>> segments;
    private Point2D zeroPoint;

    public RayCaster(GameMap map) {
        this.map = map;
        this.segmentSize = map.getSegmentSize();
        this.segments = map.getMap();
        this.maxDistance = Settings.MAX_DRAW_DISTANCE;
        this.offset = segmentSize/1000.;
        this.zeroPoint = new Point2D(0., 0.);
    }

    public RayResult[] run() {
        double playerAngle = map.getCurrentPlayerAngle();
        int fov = Settings.PLAYER_FOV;
        calculateAngleBounds(playerAngle, fov);
        int nrOfRays = Settings.HORIZONTAL_RESOLUTION;
        double angleDelta = (double) fov / (double) nrOfRays;
        RayResult[] rayResults = new RayResult[nrOfRays];
        Point2D playerCoords = map.getCurrentPlayerCoords();
        double angle = leftAngleBound;
        for (int i = 0; i < nrOfRays; i++) {
            rayResults[i] = castRay(playerCoords, angle, maxDistance);
            angle = incrementAngle(angle, angleDelta);
        }
        return rayResults;
    }

    public RayResult[] runAlt() {
        double playerAngle = map.getCurrentPlayerAngle();
        int fov = Settings.PLAYER_FOV;
        calculateAngleBounds(playerAngle, fov);
        int nrOfRays = Settings.HORIZONTAL_RESOLUTION;
        double angleDelta = (double) fov / (double) nrOfRays;
        Map<Double,RayResult> rayResults = new HashMap<>();
        Point2D playerCoords = map.getCurrentPlayerCoords();
        double angle = leftAngleBound;
        while (angle < rightAngleBound) {
            RayResult nextResult = castRay(playerCoords, angle, maxDistance);
            while(!(nextResult.getSegment() instanceof Wall)) {
                angle = incrementAngle(angle, angleDelta);
                nextResult = castRay(playerCoords, angle, maxDistance);
            }

        }
        return null;
    }

    public RayResult castRayAlt(Point2D player, double angle) {

        Segment playerSegment = getSegment(player.getX(), player.getY());
        double xSeg = playerSegment.getStartCoords().getX();
        double ySeg = playerSegment.getStartCoords().getY();

        double sinAngle = Math.sin(angle);
        double cosAngle = Math.cos(angle);

        double dirX = segmentSize;
        double xVert;
        // verticals
        if(cosAngle > 0) {
            xVert = xSeg + segmentSize;
        }
        else {
            dirX *= -1.;
            xVert = xSeg - offset;
        }
        double depthVert = (xVert - player.getX()) / cosAngle;
        double yVert = player.getY() + depthVert * sinAngle;
        double deltaDepth = dirX / cosAngle;
        double dirY = deltaDepth * sinAngle;
        return null;
    }

    public RayResult castRay(Point2D player, double angle, double maximumDistance) {
        double x = player.getX();
        double y = player.getY();
        int rayDirX = getDirX(angle);
        int rayDirY = getDirY(angle);
        Point2D horVector = findBoundary(x, angle, rayDirX, Axis.HORIZONTAL);
        Point2D vertVector = findBoundary(y, angle, rayDirY, Axis.VERTICAL);
        Point2D horPos = player.add(horVector);
        Point2D vertPos = player.add(vertVector);

        horVector = scaleVector(horVector, Axis.HORIZONTAL, rayDirX);
        double horVectorLength = zeroPoint.distance(horVector);
        vertVector = scaleVector(vertVector, Axis.VERTICAL, rayDirY);
        double vertVectorLength = zeroPoint.distance(vertVector);

        double horDistance = player.distance(horPos);
        double vertDistance = player.distance(vertPos);
        Point2D nextPoint = null;
        double distance = 0.;
        Segment nextSegment = getSegment(x, y, rayDirX, rayDirY);
        Axis currentAxis = null;
        while(!(nextSegment instanceof Wall) && distance <= maximumDistance) {
            if (horDistance < vertDistance) {
                nextPoint = horPos;
                horPos = horPos.add(horVector);
                distance = horDistance;
                horDistance += horVectorLength;
                currentAxis = Axis.HORIZONTAL;
            } else {
                nextPoint = vertPos;
                vertPos = vertPos.add(vertVector);
                distance = vertDistance;
                vertDistance += vertVectorLength;
                currentAxis = Axis.VERTICAL;
            }
            nextSegment = getSegment(nextPoint.getX(), nextPoint.getY(), rayDirX, rayDirY);
        }
        double relativeXPos = getRelativeXPos(rayDirX, rayDirY, currentAxis, nextPoint);
        return new RayResult(distance, nextSegment, nextPoint, relativeXPos, angle);
    }

    private double getRelativeXPos(int rayDirX, int rayDirY, Axis axis, Point2D point) {
        double result;
        if(axis.equals(Axis.HORIZONTAL)) {
            result = (point.getY() % segmentSize) * segmentSize;
            if(rayDirX < 0)
                result = segmentSize - result;
        } else {
            result = (point.getX() % segmentSize) * segmentSize;
            if(rayDirY > 0)
                result = segmentSize - result;
        }
        return result;
    }

    private Point2D scaleVector(Point2D vector, Axis searchDir, int rayDir) {
        double ratio = (rayDir * segmentSize) / (searchDir.equals(Axis.HORIZONTAL) ? vector.getX() : vector.getY());
        return vector.multiply(ratio);
    }

    private Point2D findBoundary(double coordinate, double angle, int rayDir, Axis searchDir) {
        double distanceToBoundary = findDeltaFromBoundary(rayDir, coordinate);
        if(searchDir.equals(Axis.HORIZONTAL)) {
            double delta = distanceToBoundary * tan(toRadians(angle));
            return new Point2D(distanceToBoundary, delta);
        }
        else {
            double vertAngle = incrementAngle(360., -incrementAngle(90., -(360. - angle)));
            double delta = distanceToBoundary * tan(toRadians(vertAngle));
            return new Point2D(delta, distanceToBoundary);
        }
    }

    private double findDeltaFromBoundary(int direction, double pos) {
        double newPos = direction > 0 ? segmentSize - (pos % segmentSize) : -(pos % segmentSize);
        return newPos == 0 ? segmentSize : newPos;
    }

    private void printData (double x, double y, double angle, double rayDeltaX, double rayDeltaY, double rayDeltaYforHorizontalSearch, double rayDeltaXforVerticalSearch) {
        System.out.println("x: " + x + ", y: " + y + ", angle: " + angle);
        System.out.println("rayDeltaX : " + rayDeltaX + ", rayDeltaY: " + rayDeltaY);
        System.out.println("deltaYhorSearch : " + rayDeltaYforHorizontalSearch + ", deltaXvertSearch: " + rayDeltaXforVerticalSearch);
        System.out.println("-------------------------------------------------------------------------------------");
    }

    private Segment getSegment(double x, double y, int rayDirX, int rayDirY) {
        int xInd = (int) (x/segmentSize + offset*rayDirX);
        int yInd = (int) (y/segmentSize + offset*rayDirY);
        if(xInd < 0 || xInd >= segments.get(0).size() || yInd < 0 || yInd >= segments.size())
            System.out.println("debug: index for segment out of bounds");
        return segments.get(yInd).get(xInd);
    }

    private Segment getSegment(double x, double y) {
        int xInd = (int) (x/segmentSize);
        int yInd = (int) (y/segmentSize);
        return segments.get(yInd).get(xInd);
    }

    private int getDirX(double angle) {
        if(angle < 90. || angle > 270.)
            return 1;
        else if(angle > 90. && angle < 270.)
            return -1;
        else return 0;
    }

    private int getDirY(double angle) {
        if(angle > 0. && angle < 180.)
            return 1;
        else if(angle > 180.)
            return -1;
        else return 0;
    }

    private void calculateAngleBounds(double playerAngle, int fov) {
        double halfFov = ((double) fov)/2.;
        this.leftAngleBound = incrementAngle(playerAngle, -halfFov);
        this.rightAngleBound = incrementAngle(playerAngle, halfFov);
    }
}
