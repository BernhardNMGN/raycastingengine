package renderers.anglecalculators;

import javafx.geometry.Point2D;
import javafx.scene.robot.Robot;
import settings.Settings;

import static renderers.utilities.GeometryCalculator.incrementAngle;

public class AngleCalculatorFirstPerson implements AngleCalculator{

    private final double maxTurningSpeed;
    private final double centerX;
    private final double centerY;
    private Robot mouseMover;

    public AngleCalculatorFirstPerson(Robot robot) {
        this.maxTurningSpeed = Settings.MAX_TURNING_SPEED;
        this.centerX = Settings.HORIZONTAL_RESOLUTION/2.;
        this.centerY = Settings.VERTICAL_RESOLUTION/2.;
        this.mouseMover = robot;
    }

    @Override
    public double calculateAngle(double currentAngle, Point2D cursor) {
        if(!isOnScreen(cursor))
            return currentAngle;
        double cursorX = cursor.getX();
        if(cursorX == centerX)
            return currentAngle;
        double angleDelta = maxTurningSpeed * (cursorX - centerX);
        if(angleDelta > 1000.)
            System.out.println("debug");
        double newAngle = incrementAngle(currentAngle, angleDelta);
        reCenterCursor(cursorX, cursor.getY()); //todo: uncomment!!!!!!!!!
        return newAngle;

    }

    private boolean isOnScreen(Point2D cursor) {
        return cursor.getX() >= 0. && cursor.getX() <= Settings.HORIZONTAL_RESOLUTION && cursor.getY() >= 0. && cursor.getY() <= Settings.VERTICAL_RESOLUTION;
    }

    private void reCenterCursor(double x, double y) {
        mouseMover.mouseMove(centerX, centerY);
    }
}
