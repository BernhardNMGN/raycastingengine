package resources.segments;

import javafx.scene.paint.Color;

public class DefaultFloor extends Segment{


    public DefaultFloor(double segmentSize, double x, double y) {
        super(segmentSize, x, y, "default_floor");
    }

    public DefaultFloor(double segmentSize) {
        super(segmentSize);
    }

    @Override
    protected void setDefaultColors() {
        setFillColor(Color.LIGHTGRAY);
        setLineColor(Color.DARKGRAY);
    }
}
