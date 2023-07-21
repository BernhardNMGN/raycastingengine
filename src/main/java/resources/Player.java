package resources;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.apache.commons.geometry.euclidean.twod.Vector2D;
import raycasting.RayCaster;
import renderers.anglecalculators.AngleCalculator;
import resources.map.GameMap;
import resources.segments.Segment;
import resources.segments.Wall;
import settings.KeyBindings;
import settings.Settings;
import java.util.List;
import java.util.Set;

public class Player {

    private Point2D playerCoords;
    private Vector2D playerDir;
    private double speed;

    private double playerSize;
    private Color primaryPlayerColor;

    private GameMap map;
    private double segmentSize;

    private final double screenCenterX;
    private final double screenCenterY;

    private int[][] wallArray;

    private AngleCalculator angleCalculator;
    private RayCaster rayCaster;

    public Player(double playerX, double playerY, GameMap map) {
        this(new Point2D(playerX, playerY), map);
    }

    public Player(Point2D startingCoords, GameMap map) {
        this.playerCoords = startingCoords;
        this.primaryPlayerColor = Settings.PRIMARY_PLAYER_COLOR;
        this.playerDir = Vector2D.of(0., -1.);
        this.map = map;
        this.segmentSize = map.getSegmentSize();
        this.playerSize = segmentSize/2.;
        this.speed = Settings.PLAYER_MOVEMENT_SPEED_IN_ENGINE;
        this.screenCenterX = Settings.HORIZONTAL_RESOLUTION/2.;
        this.screenCenterY = Settings.VERTICAL_RESOLUTION/2.;
        this.rayCaster = new RayCaster(map);
        initializeWallArray();
    }

    private void initializeWallArray() {
        int sizeX = map.getMap().get(0).size();
        int sizeY = map.getMap().size();
        this.wallArray = new int[sizeX][sizeY];
        for(int x = 0; x < sizeX; x++)
            for (int y = 0; y < sizeY; y++) {
                Segment seg = map.getMap().get(y).get(x);
                wallArray[x][y] = seg instanceof Wall ? 1 : 0;
            }
    }

    private void movePlayer(double dX, double dY) {
        double pX = playerCoords.getX();
        double pY = playerCoords.getY();
        if(dX != pX) {
            if(!detectCollision(pX + dX, pY))
                playerCoords = playerCoords.add(dX, 0.);
        }
        if(dY != pY) {
           if(!detectCollision(pX, pY + dY))
               playerCoords = playerCoords.add(0., dY);
        }
    }

    private boolean detectCollision(double x, double y) {
        Circle playerRange = new Circle(x, y, playerSize/2.);
        for(int i = (int) x - 1; i <= (int) x + 1; i++)
            for(int j = (int) y - 1; j <= (int) y + 1; j++) {
                if (wallArray[i][j] > 0 && playerRange.intersects(new BoundingBox(i,j,segmentSize,segmentSize)))
                    return true;
            }
        return false;
    }

    public Vector2D updateAngleVector(Point2D cursor) {
        this.playerDir = angleCalculator.calculateAngleVector(this.playerDir, cursor);
        return playerDir;
    }

    public Point2D updateCoordinates(Set<KeyCode> currentKeysPressed) {
        double currentSpeed = currentKeysPressed.contains(KeyBindings.RUN) ? speed * Settings.PLAYER_RUN_FACTOR : speed;
        double cos = currentSpeed * playerDir.getX();
        double sin = currentSpeed * playerDir.getY();
        if(currentKeysPressed.contains(PlayerMovement.FORWARD.getKeyCode()))
            movePlayer(cos, sin);
        if(currentKeysPressed.contains(PlayerMovement.BACKWARD.getKeyCode()))
            movePlayer(-cos, -sin);
        if(currentKeysPressed.contains(PlayerMovement.LEFT.getKeyCode()))
            movePlayer(sin, -cos);
        if(currentKeysPressed.contains(PlayerMovement.RIGHT.getKeyCode()))
            movePlayer(-sin, cos);
        return playerCoords;
    }

    public void setAngleCalculator(AngleCalculator angleCalculator) {
        this.angleCalculator = angleCalculator;
    }

    public enum PlayerMovement {
        FORWARD(KeyBindings.FORWARD),
        BACKWARD(KeyBindings.BACKWARD),
        LEFT(KeyBindings.LEFT),
        RIGHT(KeyBindings.RIGHT);

        private KeyCode keyCode;

        PlayerMovement(KeyCode keyCode) {
            this.keyCode = keyCode;
        }

        public KeyCode getKeyCode() {
            return keyCode;
        }
    }

    public Point2D getPlayerCoords() {
        return playerCoords;
    }

    public Vector2D getPlayerDir() {
        return playerDir;
    }

    public double getPlayerSize() {
        return playerSize;
    }

    public Color getPrimaryPlayerColor() {
        return primaryPlayerColor;
    }
}
