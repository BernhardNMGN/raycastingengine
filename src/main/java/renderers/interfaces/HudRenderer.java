package renderers.interfaces;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import resources.Player;
import resources.map.GameMap;

public interface HudRenderer {


    void draw(Player player, GameMap map, Point2D cursor);

    void setDefaultColors();
}
