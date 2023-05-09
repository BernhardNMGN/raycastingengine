package renderers.topdown;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import renderers.interfaces.CursorRenderer;
import renderers.interfaces.CursorType;
import settings.Settings;

public class CursorRendererTopDown implements CursorRenderer {

    private Scene scene;
    private CursorType type;
    private double size;
    private Color color;

    public CursorRendererTopDown(Scene scene, CursorType type) {
        this.scene = scene;
        this.type = type;
        this.size = Settings.DEFAULT_CURSOR_SIZE;
        this.color = Settings.DEFAULT_CURSOR_COLOR;
    }

    public CursorRendererTopDown(CursorType type) {
        this.type = type;
    }

    @Override
    public void draw(Point2D cursor) {
        scene.setCursor(Cursor.CROSSHAIR); //todo: fix
    }
}
