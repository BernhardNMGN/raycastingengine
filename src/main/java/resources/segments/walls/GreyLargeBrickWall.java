package resources.segments.walls;

import resources.segments.Segment;
import resources.segments.Wall;

public class GreyLargeBrickWall extends Segment implements Wall {

    public GreyLargeBrickWall(double segmentSize, double x, double y) {
        super(segmentSize, x, y);
        this.textureId = "bricks_grey";
    }

    public GreyLargeBrickWall(double segmentSize) {
        super(segmentSize);
        this.textureId = "bricks_grey";
    }

    @Override
    protected void setDefaultColors() {

    }
}
