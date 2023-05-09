package renderers.interfaces;

import javafx.scene.canvas.GraphicsContext;
import renderers.utilities.GraphicRendererContainer;
import renderers.utilities.RenderType;
import resources.Player;

public interface PlayerRenderer {

    void draw(Player player);
    RenderType getRenderType();
}
