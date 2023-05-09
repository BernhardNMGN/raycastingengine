package resources.map;

import javafx.scene.paint.Color;

public class MapAssets {

    private Color floorColor;
    private Color ceilingColor;

    public MapAssets(Color floorColor, Color ceilingColor) {
        this.floorColor = floorColor;
        this.ceilingColor = ceilingColor;
    }

    public MapAssets() {
        this(Color.LIGHTGREEN, Color.SKYBLUE);
    }

    public Color getFloorColor() {
        return floorColor;
    }

    public Color getCeilingColor() {
        return ceilingColor;
    }
}
