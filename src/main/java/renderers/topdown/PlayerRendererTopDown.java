package renderers.topdown;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import renderers.utilities.GraphicRendererContainer;
import renderers.utilities.RenderType;
import renderers.interfaces.PlayerRenderer;
import resources.Player;
import settings.Settings;

import static renderers.utilities.GeometryCalculator.*;

public class PlayerRendererTopDown implements PlayerRenderer{

    private Point2D playerCoords;
    private GraphicsContext gc;

    public PlayerRendererTopDown(GraphicsContext gc) {
        this.gc = gc;
        this.playerCoords = new Point2D(Settings.HORIZONTAL_RESOLUTION/2., Settings.VERTICAL_RESOLUTION/2.);
    }

    @Override
    public void draw(Player player) {
        double playerAngle = player.getAngle();
        double playerSize = player.getPlayerSize();
        Color primaryPlayerColor = player.getPrimaryPlayerColor();
        double radius = playerSize/2.;

        double leftAngle = calculateAngle(playerAngle, -90.);
        Point2D leftPoint = calculatePointOnCircle(playerCoords, leftAngle, radius);
        double rightAngle = calculateAngle(playerAngle, 90.);
        Point2D rightPoint = calculatePointOnCircle(playerCoords, rightAngle, radius);

        Point2D endPoint = calculatePointOnCircle(playerCoords, playerAngle, playerSize);

        gc.setFill(primaryPlayerColor);
        gc.setStroke(primaryPlayerColor);
        gc.fillOval(playerCoords.getX() - radius, playerCoords.getY() - radius, playerSize, playerSize);
        gc.strokeLine(leftPoint.getX(), leftPoint.getY(), endPoint.getX(), endPoint.getY());
        gc.strokeLine(rightPoint.getX(), rightPoint.getY(), endPoint.getX(), endPoint.getY());
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.TOP_DOWN;
    }
}
