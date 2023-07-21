package resources.segments;

import resources.Direction;

public abstract class Wall extends Segment {

    protected String wallTextureId;

    protected Wall(double segmentSize, double x, double y) {
        super(segmentSize, x, y);
    }

    protected Wall(double segmentSize) {
        super(segmentSize);
    }

    public String getWallTextureId() {
        return this.wallTextureId;
    }
}
