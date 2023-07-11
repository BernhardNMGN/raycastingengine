package resources.segments;

import javafx.scene.paint.Color;
import resources.Direction;
import settings.Settings;

public class DefaultWall extends Segment implements Wall{

    public DefaultWall(double segmentSize, double x, double y) {
        super(segmentSize, x, y);
        this.textureId = Settings.DEFAULT_WALL_TEXTURE_ID;
    }

    public DefaultWall(double segmentSize) {
        super(segmentSize);
    }

    @Override
    protected void setDefaultColors() {
        setFillColor(Color.DARKRED);
        setLineColor(Color.BLACK);
    }
}
