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
    private List<BoundingBox> walls;
    private List<List<Boolean>> wallList;
    private double segmentSize;

    private final double screenCenterX;
    private final double screenCenterY;

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
        extractWallSegments();
    }

    private void extractWallSegments() {
        walls = map.streamSegments()
                    .filter(seg -> seg instanceof Wall)
                    .map(wall -> {
                        double x = wall.getStartCoords().getX();
                        double y = wall.getStartCoords().getY();
                        return new BoundingBox(x, y, segmentSize, segmentSize);
                    }).toList();

        wallList = map.getMap().stream()
                .map(l ->
                    l.stream()
                    .map(seg -> seg instanceof Wall)
                    .toList())
                .toList();
    }

    private void movePlayer(double dX, double dY) {
        Point2D newCoords = playerCoords.add(dX, dY);
//        if(findCollisionPoint(newCoords, dX, dY) != null)
        if(detectCollision(newCoords))
            bumpPlayer(dX, dY);
        else playerCoords = newCoords;
    }

    private void bumpPlayer(double dX, double dY) {
        movePlayer(-1 * dX, -1 * dY);
    }

    private boolean detectCollision(Point2D coords) {
        Circle playerBox = new Circle(coords.getX(), coords.getY(), playerSize/2.);
        return walls.stream()
                .anyMatch(rect -> playerCollidesWithSegment(rect, playerBox));
    }

    private Point2D findCollisionPoint(Point2D coords, double dX, double dY) {
        Circle playerBox = new Circle(coords.getX(), coords.getY(), playerSize/2.);
        int playerIndexX = (int) (playerCoords.getX() / segmentSize);
        int playerIndexY = (int) (playerCoords.getY() / segmentSize);
        int newPlayerIndexX = (int) (coords.getX() / segmentSize);
        int newPlayerIndexY = (int) (coords.getY() / segmentSize);
        Segment playerSeg = map.getMap().get(playerIndexY).get(playerIndexX);

        for(int i = -1; i <= 1; i++)
            for(int j = -1; j <= 1; j++) {
                if(wallList.get(playerIndexY + j).get(playerIndexX + i)) {

                }
            }
        return null;
    }

    private boolean playerCollidesWithSegment(BoundingBox wall, Circle playerBox) {
        return playerBox.intersects(wall);
    }

    public Vector2D updateAngleVector(Point2D cursor) {
        this.playerDir = angleCalculator.calculateAngleVector(this.playerDir, cursor);
        return playerDir;
    }

    public Point2D updateCoordinates(Set<KeyCode> currentKeysPressed) {
        double currentSpeed = currentKeysPressed.contains(KeyBindings.RUN) ? speed * Settings.PLAYER_RUN_FACTOR : speed;
//        double cos = currentSpeed * cos(toRadians(angle));
//        double sin = currentSpeed * sin(toRadians(angle));
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
