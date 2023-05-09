package resources.segments;

import javafx.scene.paint.Color;
import settings.Settings;

public class DefaultWall extends Segment implements Wall{


//    public DefaultWall getInstance() {
//        if(instance == null)
//            instance = new DefaultWall(Settings.SEGMENT_SIZE);
//        return instance;
//    }

    public DefaultWall(double segmentSize, double x, double y) {
        super(segmentSize, x, y);
    }

    public DefaultWall(double segmentSize) {
        super(segmentSize);
    }



    @Override
    protected void setDefaultColors() {
        setFillColor(Color.DARKRED);
        setLineColor(Color.BLACK);
    }

    @Override
    protected void initializeTextureArray() {
        textureArray = new int[][]{};
        textureArrayColors = new Color[]{getFillColor(), getLineColor()};
    }
}
