package resources.segments;

import javafx.scene.paint.Color;
import settings.Settings;

public class DefaultFloor extends Floor{

    public DefaultFloor(double segmentSize, double x, double y) {
        super(segmentSize, x, y);
        this.floorTextureId = Settings.DEFAULT_FLOOR_TEXTURE_ID;
        this.ceilingTextureId = Settings.DEFAULT_CEILING_TEXTURE_ID;
    }

    public DefaultFloor(double segmentSize) {
        super(segmentSize);
        this.floorTextureId = Settings.DEFAULT_FLOOR_TEXTURE_ID;
        this.ceilingTextureId = Settings.DEFAULT_CEILING_TEXTURE_ID;
    }

    @Override
    protected void setDefaultColors() {
        setFillColor(Color.LIGHTGRAY);
        setLineColor(Color.DARKGRAY);
    }
}
