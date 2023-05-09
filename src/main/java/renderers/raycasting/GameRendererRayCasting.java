package renderers.raycasting;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import renderers.anglecalculators.AngleCalculatorFirstPerson;
import renderers.interfaces.CursorRenderer;
import renderers.interfaces.HudRenderer;
import renderers.utilities.RenderType;
import renderers.anglecalculators.AngleCalculator;
import renderers.interfaces.GameRenderer;
import resources.Player;
import resources.map.GameMap;

import java.util.Set;

public class GameRendererRayCasting implements GameRenderer {

    private MapRendererRayCasting mapRenderer;
    private PlayerRendererRayCasting playerRenderer;
    private CursorRenderer cursorRenderer;
    private HudRenderer hudRenderer;
    private AngleCalculator angleCalculator;

    public GameRendererRayCasting(MapRendererRayCasting mapRenderer, PlayerRendererRayCasting playerRenderer, CursorRenderer cursorRenderer, HudRenderer hudRenderer, AngleCalculatorFirstPerson angleCalculator) {
        this.mapRenderer = mapRenderer;
        this.playerRenderer = playerRenderer;
        this.cursorRenderer = cursorRenderer;
        this.hudRenderer = hudRenderer;
        this.angleCalculator = angleCalculator;
    }

    @Override
    public void drawAll(Set<KeyCode> keysPressed, Player player, GameMap map, Point2D cursor) {
//        clearBackGround(grContainer);
        mapRenderer.draw();
        playerRenderer.draw(player);
        cursorRenderer.draw(cursor);
        hudRenderer.draw(player, map, cursor);
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.RAY_CASTING;
    }

    @Override
    public AngleCalculator getAngleCalculator() {
        return angleCalculator;
    }

    private void clearBackGround() {
//        GraphicsContext gc = grContainer.getGraphicsContext();
//        gc.setFill(Color.BLACK);
//        gc.fillRect(0, 0, Settings.HORIZONTAL_RESOLUTION, Settings.VERTICAL_RESOLUTION);
    }
}
