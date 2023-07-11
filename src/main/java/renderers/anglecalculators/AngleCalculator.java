package renderers.anglecalculators;

import javafx.geometry.Point2D;
import org.apache.commons.geometry.euclidean.twod.Vector2D;

public interface AngleCalculator{

    double calculateAngle(double currentAngle, Point2D cursor);
    Vector2D calculateAngleVector(Vector2D currentAngle, Point2D cursor);
}
