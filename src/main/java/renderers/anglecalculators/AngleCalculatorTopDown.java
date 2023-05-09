package renderers.anglecalculators;

import javafx.geometry.Point2D;
import settings.Settings;

import static java.lang.Math.atan2;
import static java.lang.Math.toDegrees;

public class AngleCalculatorTopDown implements AngleCalculator {

    private double centerX;
    private double centerY;

    public AngleCalculatorTopDown() {
        this.centerX = Settings.HORIZONTAL_RESOLUTION/2.;
        this.centerY = Settings.VERTICAL_RESOLUTION/2.;
    }

    @Override
    public double calculateAngle(double currentAngle, Point2D cursor) {
        double x2 = cursor.getX();
        double y2 = cursor.getY();
        double angle;
        if(centerX == x2) {
            angle = centerY > y2 ? 270. : 90.;
        }
        else if(centerY == y2)
            angle = centerX > x2 ? 180. : 0.;
        else angle = toDegrees(atan2(y2 - centerY, x2 - centerX));
        if(angle < 0)
            angle += 360.;
        return angle;
    }
}
