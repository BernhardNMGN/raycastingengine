package renderers.utilities;

import javafx.geometry.Point2D;
import org.apache.commons.geometry.euclidean.twod.Vector2D;
import raycasting.RayResult;
import resources.Direction;
import resources.segments.DefaultFloor;
import resources.segments.DefaultWall;
import resources.segments.PlayerStartFloor;
import resources.segments.Segment;

import java.util.Vector;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;

public class GeometryCalculator {


    public static Point2D calculatePointOnCircle(Point2D origin, double angle, double radius) {
        double x = radius * cos(toRadians(angle)) + origin.getX();
        double y = radius * sin(toRadians(angle)) + origin.getY();
        return new Point2D(x, y);
    }

    public static Point2D calculatePointOnCircle(Point2D origin, Vector2D angle, double radius) {
        angle = angle.multiply(radius);
        Point2D dir = new Point2D(angle.getX(), angle.getY());
        return origin.add(dir);
    }

    public static double calculateAngle(double originalAngle, double difference) {
        double newAngle = originalAngle + difference;
        return newAngle < 0. ? newAngle + 360. : (newAngle > 360. ? newAngle - 360. : newAngle);
    }

    public static double incrementAngle(double angle, double increment) {

        double result = angle + increment;
        while(result >= 360.)
            result -= 360.;
        while(result < 0.)
            result += 360.;
        return result;
    }

    public static Direction calculateSideOfImpact(RayResult rayResult) {
        double entX = rayResult.getEntrancePoint().getX();
        double entY = rayResult.getEntrancePoint().getY();
        double segX = rayResult.getSegment().getStartCoords().getX();
        double segY = rayResult.getSegment().getStartCoords().getY();
        if(entX == segX) {
            return Direction.LEFT;
        }
        else if(entX == segX + rayResult.getSegment().getSegmentSize()) {
            return Direction.RIGHT;
        }
        else if(entY == segY) {
            return Direction.UP;
        }
        else return Direction.DOWN;
    }

    public static Point2D angleToVector(Point2D source, double angle) {
        return null;
    }

    public static double length(Vector2D vector) {
        double x = vector.getX();
        double y = vector.getY();
        return Math.sqrt(x*x + y*y);
    }
    public static Vector2D rotateVector(Vector2D vector, double angle) {
        double rx = (vector.getX() * Math.cos(angle)) - (vector.getY() * Math.sin(angle));
        double ry = (vector.getX() * Math.sin(angle)) + (vector.getY() * Math.cos(angle));
        return Vector2D.of(rx, ry);
    }
}
