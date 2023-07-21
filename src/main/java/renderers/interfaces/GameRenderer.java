package renderers.interfaces;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import raycasting.RayResult;
import renderers.utilities.RenderType;
import renderers.anglecalculators.AngleCalculator;
import resources.Player;
import resources.map.GameMap;
import java.util.Set;

public interface GameRenderer {

    void drawAll(RayResult[] rayResults, int[] pixels, Set<KeyCode> keysPressed, Player player, GameMap map, Point2D cursor);

    void drawAll(RayResult[] rayResults, int[] pixels, Image image, Set<KeyCode> keysPressed, Player player, GameMap map, Point2D cursor);

    RenderType getRenderType();
    AngleCalculator getAngleCalculator();
}
