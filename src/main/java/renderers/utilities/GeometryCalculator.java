package renderers.utilities;

import javafx.geometry.Point2D;
import raycasting.RayResult;
import resources.Direction;
import resources.segments.DefaultFloor;
import resources.segments.DefaultWall;
import resources.segments.PlayerStartFloor;
import resources.segments.Segment;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;

public class GeometryCalculator {


    public static Point2D calculatePointOnCircle(Point2D origin, double angle, double radius) {
        double x = radius * cos(toRadians(angle)) + origin.getX();
        double y = radius * sin(toRadians(angle)) + origin.getY();
        return new Point2D(x, y);
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
}
