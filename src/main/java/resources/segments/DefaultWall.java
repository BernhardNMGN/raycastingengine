package resources.segments;

import javafx.scene.paint.Color;
import resources.Direction;
import settings.Settings;

public class DefaultWall extends Wall{

    public DefaultWall(double segmentSize, double x, double y) {
        super(segmentSize, x, y);
        this.wallTextureId = Settings.DEFAULT_WALL_TEXTURE_ID;
    }

    public DefaultWall(double segmentSize) {
        super(segmentSize);
        this.wallTextureId = Settings.DEFAULT_WALL_TEXTURE_ID;
    }

    @Override
    protected void setDefaultColors() {
        setFillColor(Color.DARKRED);
        setLineColor(Color.BLACK);
    }
}
