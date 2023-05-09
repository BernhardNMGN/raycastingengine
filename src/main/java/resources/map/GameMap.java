package resources.map;

import javafx.geometry.Point2D;
import resources.segments.Segment;
import java.util.List;

public class GameMap {

    protected List<List<Character>> mapArray;
    private double segmentSize;
    private List<List<Segment>> map;
    private Point2D playerStartCoords;
    private Point2D currentPlayerCoords;
    private double currentPlayerAngle;

    private MapAssets assets;

    public GameMap(List<List<Character>> mapArray, double segmentSize, List<List<Segment>> map, Point2D playerStartCoords) {
        this.mapArray = mapArray;
        this.segmentSize = segmentSize;
        this.map = map;
        this.playerStartCoords = playerStartCoords;
        this.assets = new MapAssets();
    }

//    protected void initPlayerStartCoords(Point2D indexPoint) {
//        int horSize = mapArray.get(0).size();
//        int index = horSize * (int) indexPoint.getY() + (int) indexPoint.getX();
//        List<Segment> startingSegment = map.get(index);
//        playerStartCoords = startingSegment.getStartCoords().add(segmentSize/2., segmentSize/2.);
//    }

    public List<List<Character>> getMapArray() {
        return mapArray;
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

    public double getCurrentPlayerAngle() {
        return currentPlayerAngle;
    }

    public void setCurrentPlayerAngle(double currentPlayerAngle) {
        this.currentPlayerAngle = currentPlayerAngle;
    }

    public MapAssets getAssets() {
        return assets;
    }
}
