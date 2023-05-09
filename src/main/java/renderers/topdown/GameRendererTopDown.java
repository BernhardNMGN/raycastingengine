package renderers.topdown;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import renderers.utilities.GraphicRendererContainer;
import renderers.utilities.RenderType;
import renderers.anglecalculators.AngleCalculator;
import renderers.anglecalculators.AngleCalculatorTopDown;
import renderers.interfaces.*;
import resources.Player;
import resources.map.GameMap;

import java.util.Set;

public class GameRendererTopDown implements GameRenderer {

    private MapRendererTopDown mapRenderer;
    private PlayerRendererTopDown playerRenderer;
    private CursorRenderer cursorRenderer;
    private HudRenderer hudRenderer;
    private AngleCalculator angleCalculator;
    private Canvas canvas;

    public GameRendererTopDown(MapRendererTopDown mapRenderer, PlayerRendererTopDown playerRenderer, CursorRenderer cursorRenderer, HudRenderer hudRenderer, Canvas canvas) {
        this.mapRenderer = mapRenderer;
        this.playerRenderer = playerRenderer;
        this.cursorRenderer = cursorRenderer;
        this.hudRenderer = hudRenderer;
        this.angleCalculator = new AngleCalculatorTopDown();
        this.canvas = canvas;
    }

    @Override
    public void drawAll(Set<KeyCode> keysPressed, Player player, GameMap map, Point2D cursor) {
//        clearBackGround();
        mapRenderer.draw();
        playerRenderer.draw(player);
        hudRenderer.draw(player, map, cursor);
        cursorRenderer.draw(cursor);
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.TOP_DOWN;
    }

    @Override
    public AngleCalculator getAngleCalculator() {
        return angleCalculator;
    }

    private void clearBackGround(GraphicRendererContainer grContainer) {
//        GraphicsContext gc = grContainer.getGraphicsContext();
//        gc.setFill(Color.BLACK);
//        gc.fillRect(0, 0, Settings.HORIZONTAL_RESOLUTION, Settings.VERTICAL_RESOLUTION);
    }
}
