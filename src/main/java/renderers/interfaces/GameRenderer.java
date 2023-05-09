package renderers.interfaces;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import renderers.utilities.RenderType;
import renderers.anglecalculators.AngleCalculator;
import resources.Player;
import resources.map.GameMap;
import java.util.Set;

public interface GameRenderer {

    void drawAll(Set<KeyCode> keysPressed, Player player, GameMap map, Point2D cursor);
    RenderType getRenderType();
    AngleCalculator getAngleCalculator();
}
