package renderers.topdown;

import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import raycasting.RayResult;
import renderers.utilities.RenderType;
import renderers.interfaces.WallRenderer;
import resources.map.GameMap;
import resources.segments.Segment;
import settings.Settings;

import java.util.List;

public class WallRendererTopDown implements WallRenderer {

    private final GameMap map;
    private double segmentSize;

    private double horzOffset;
    private double vertOffset;

    private int maxIndexX;
    private int maxIndexY;

    private GraphicsContext gc;


    public WallRendererTopDown(GraphicsContext gc, GameMap map) {
        this.gc = gc;
        this.map = map;
        this.segmentSize = map.getSegmentSize();
        this.horzOffset = Settings.HORIZONTAL_RESOLUTION/2.;
        this.vertOffset = Settings.VERTICAL_RESOLUTION/2.;
        this.maxIndexX = map.getMap().get(0).size() - 1;
        this.maxIndexY = map.getMap().size() - 1;
    }

    @Override
    public void draw() {
        double xDiff = map.getCurrentPlayerCoords().getX() - horzOffset;
        double yDiff = map.getCurrentPlayerCoords().getY() - vertOffset;

        List<List<Segment>> visibleSegments = computeVisibleSegments(xDiff, yDiff);
        visibleSegments.forEach(line ->
                line.stream()
                        .forEach(seg -> {
                            double x = seg.getStartCoords().getX() - xDiff;
                            double y = seg.getStartCoords().getY() - yDiff;
                            seg.draw(gc, x, y);})
        );
    }

    @Override
    public void draw(int[] pixels) {

    }

    @Override
    public void draw(RayResult[] rayResults, int[] pixels) {

    }

    @Override
    public RenderType getRenderType() {
        return RenderType.TOP_DOWN;
    }

    private List<List<Segment>> computeVisibleSegments(double xDiff, double yDiff) {
        int xMin = Math.max((int) (xDiff/segmentSize), 0);
        int yMin = Math.max((int) (yDiff/segmentSize), 0);
        int xMax = Math.min((int) ((xDiff+Settings.HORIZONTAL_RESOLUTION)/segmentSize), maxIndexX);
        int yMax = Math.min((int) ((yDiff+Settings.VERTICAL_RESOLUTION)/segmentSize), maxIndexY);
        return map.getMap().subList(yMin, yMax+1).stream()
                .map(list -> list.subList(xMin, xMax+1))
                .toList();

    }

    private List<List<Segment>> computeVisibleSegments() {
        double xMin = map.getCurrentPlayerCoords().getX() - Settings.HORIZONTAL_RESOLUTION/2.;
        double yMin = map.getCurrentPlayerCoords().getY() - Settings.VERTICAL_RESOLUTION/2.;
        BoundingBox screenBox = new BoundingBox(xMin, yMin, Settings.HORIZONTAL_RESOLUTION, Settings.VERTICAL_RESOLUTION);
        return map.getMap().stream()
                .map(
                        line -> line.stream()
                                .filter(seg -> isVisible(seg, screenBox))
                                .toList())
                .toList();
    }

    private boolean isVisible(Segment seg, BoundingBox screenBox) {
        BoundingBox segBox = seg.getOuterBoundingBox();
        return screenBox.intersects(segBox) || screenBox.contains(segBox);
    }
}
