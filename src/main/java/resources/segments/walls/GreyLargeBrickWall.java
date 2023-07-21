package resources.segments.walls;

import resources.segments.Segment;
import resources.segments.Wall;

public class GreyLargeBrickWall extends Wall {

    public GreyLargeBrickWall(double segmentSize, double x, double y) {
        super(segmentSize, x, y);
        this.wallTextureId = "bricks_grey";
    }

    public GreyLargeBrickWall(double segmentSize) {
        super(segmentSize);
        this.wallTextureId = "bricks_grey";
    }

    @Override
    protected void setDefaultColors() {
    }
}
