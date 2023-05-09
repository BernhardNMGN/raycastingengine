package renderers.interfaces;

import javafx.scene.canvas.GraphicsContext;
import renderers.utilities.GraphicRendererContainer;
import renderers.utilities.RenderType;

public interface MapRenderer{

    void draw();
    RenderType getRenderType();
}
