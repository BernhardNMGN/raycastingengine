package renderers.raycasting;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import raycasting.RayResult;
import renderers.anglecalculators.AngleCalculatorFirstPerson;
import renderers.interfaces.CursorRenderer;
import renderers.interfaces.FloorAndCeilingRenderer;
import renderers.interfaces.HudRenderer;
import renderers.utilities.RenderType;
import renderers.anglecalculators.AngleCalculator;
import renderers.interfaces.GameRenderer;
import resources.Player;
import resources.map.GameMap;

import java.util.Set;

public class GameRendererRayCasting implements GameRenderer {

    private FloorAndCeilingRenderer floorAndCeilingRenderer;
    private WallRendererRayCasting wallRenderer;
    private PlayerRendererRayCasting playerRenderer;
    private CursorRenderer cursorRenderer;
    private HudRenderer hudRenderer;
    private AngleCalculator angleCalculator;

    public GameRendererRayCasting(FloorAndCeilingRendererRayCasting floorAndCeilingRenderer, WallRendererRayCasting wallRenderer, PlayerRendererRayCasting playerRenderer, CursorRenderer cursorRenderer, HudRenderer hudRenderer, AngleCalculatorFirstPerson angleCalculator) {
        this.floorAndCeilingRenderer = floorAndCeilingRenderer;
        this.wallRenderer = wallRenderer;
        this.playerRenderer = playerRenderer;
        this.cursorRenderer = cursorRenderer;
        this.hudRenderer = hudRenderer;
        this.angleCalculator = angleCalculator;
    }

    @Override
    public void drawAll(RayResult[] rayResults, int[] pixels, Set<KeyCode> keysPressed, Player player, GameMap map, Point2D cursor) {
        wallRenderer.draw(rayResults, pixels);
        playerRenderer.draw(player);
        cursorRenderer.draw(cursor);
        hudRenderer.draw(player, map, cursor);
    }

    @Override
    public void drawAll(RayResult[] rayResults, int[] pixels, Image image, Set<KeyCode> keysPressed, Player player, GameMap map, Point2D cursor) {
//        floorAndCeilingRenderer.draw(rayResults, pixels);
        wallRenderer.draw(rayResults, pixels);
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
}
