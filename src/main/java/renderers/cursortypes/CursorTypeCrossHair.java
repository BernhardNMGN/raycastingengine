package renderers.cursortypes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import renderers.interfaces.CursorType;

public class CursorTypeCrossHair implements CursorType {

    @Override
    public void draw(GraphicsContext gc, double size, Color color, double x, double y) {
        double diff = size/2.;
        gc.setStroke(color);
        gc.setLineWidth(4.);
        gc.strokeOval(x - diff/2., y - diff/2., diff, diff);
        gc.strokeLine(x - diff, y, x + diff, y);
        gc.strokeLine(x, y - diff, x, y + diff);

    }
}
