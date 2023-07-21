package resources.map;

import javafx.geometry.Point2D;
import org.apache.commons.geometry.euclidean.twod.Vector2D;
import resources.segments.Segment;
import settings.Settings;

import java.util.List;
import java.util.stream.Stream;

public class GameMap {

    protected List<List<Character>> mapArray;
    private double segmentSize;
    private List<List<Segment>> map;
    private Point2D playerStartCoords;
    private Point2D currentPlayerCoords;
    private Vector2D currentPlayerDir;

    private Vector2D cameraPlane;

    private MapAssets assets;

    public GameMap(List<List<Character>> mapArray, double segmentSize, List<List<Segment>> map, Point2D playerStartCoords) {
        this.mapArray = mapArray;
        this.segmentSize = segmentSize;
        this.map = map;
        this.playerStartCoords = playerStartCoords;
        this.assets = new MapAssets();

    }

    public List<List<Segment>> getMap() {
        return map;
    }

    public double getSegmentSize() {
        return segmentSize;
    }

    public Point2D getPlayerStartCoords() {
        return playerStartCoords;
    }

    public Point2D getCurrentPlayerCoords() {
        return currentPlayerCoords;
    }

    public void setCurrentPlayerCoords(Point2D currentPlayerCoords) {
        this.currentPlayerCoords = currentPlayerCoords;
    }

    public Vector2D getCurrentPlayerDir() {
        return currentPlayerDir;
    }

    public void setCurrentPlayerDirAndUpdateCameraPlane(Vector2D currentPlayerDir) {
        this.currentPlayerDir = currentPlayerDir;
        this.cameraPlane = currentPlayerDir.orthogonal().multiply(Math.tan(Settings.PLAYER_FOV/2.));
    }

    public Stream<Segment> streamSegments() {
        return map.stream().flatMap(l -> l.stream());
    }

    public Vector2D getCameraPlane() {
        return this.cameraPlane;
    }
}
