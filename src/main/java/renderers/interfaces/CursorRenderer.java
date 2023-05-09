package renderers.interfaces;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import renderers.utilities.GraphicRendererContainer;

public interface CursorRenderer {

    void draw(Point2D cursor);
}
