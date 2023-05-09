package renderers.interfaces;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public interface CursorType {

    void draw(GraphicsContext gc, double size, Color color, double x, double y);
}
