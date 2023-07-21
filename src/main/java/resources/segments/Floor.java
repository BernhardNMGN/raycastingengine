package resources.segments;

public abstract class Floor extends Segment{

    protected String floorTextureId;
    protected String ceilingTextureId;

    protected Floor(double segmentSize, double x, double y) {
        super(segmentSize, x, y);
    }

    protected Floor(double segmentSize) {
        super(segmentSize);
    }

    public String getFloorTextureId() {
        return this.floorTextureId;
    }

    public String getCeilingTextureId() {
        return this.ceilingTextureId;
    }
}
