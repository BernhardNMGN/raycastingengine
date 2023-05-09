package resources;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.image.PixelFormat;
import javafx.scene.robot.Robot;
import raycasting.RayCaster;
import renderers.anglecalculators.AngleCalculatorFirstPerson;
import renderers.cursortypes.CursorTypeCrossHair;
import renderers.interfaces.*;
import renderers.raycasting.*;
import renderers.topdown.*;
import resources.map.GameMap;
import settings.Settings;

import java.nio.IntBuffer;

public class ResourceGenerator {

    public static PixelBuffer<IntBuffer> generatePixelBuffer() {
        IntBuffer intBuffer = IntBuffer.allocate(Settings.HORIZONTAL_RESOLUTION * Settings.VERTICAL_RESOLUTION);
        PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
        PixelBuffer<IntBuffer> pixelBuffer = new PixelBuffer<>(Settings.HORIZONTAL_RESOLUTION, Settings.VERTICAL_RESOLUTION, intBuffer, pixelFormat);
        return pixelBuffer;
    }

    public static GameRendererTopDown generate2dRendererTopDown(Canvas canvas, GameMap map) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        CursorType cursorType = createDefaultCursorType();
        CursorRenderer cursorRenderer = new CursorRendererTopDown(cursorType);
        HudRenderer hudRenderer = new HudRendererGeneral(gc);
        return new GameRendererTopDown(new MapRendererTopDown(gc, map), new PlayerRendererTopDown(gc), cursorRenderer, hudRenderer, canvas);
    }

    private static CursorType createDefaultCursorType() {
        return new CursorTypeCrossHair();
    }

    public static GameRenderer generate3dRenderer(ImageView imageView, PixelBuffer<IntBuffer> pixelBuffer, GameMap map) {
        RayCaster rayCaster = new RayCaster(map);
        Robot mouseMover = new Robot();
        AngleCalculatorFirstPerson angleCalculator = new AngleCalculatorFirstPerson(mouseMover);
        return new GameRendererRayCasting(new MapRendererRayCasting(map, pixelBuffer, imageView, rayCaster), new PlayerRendererRayCasting(), new CursorRendererRayCasting(), new HudRendererRayCasting(), angleCalculator);
    }

    /**
     * This is more dynamic (because we can declare default class in Settings), but method looks very
     * ugly..
     *
    public static CursorType createDefaultCursorType() {
        Class<CursorType> defaultClass = Settings.DEFAULT_CURSOR_TYPE;
        CursorType type = null;
        try {
            Constructor<CursorType> constructor = defaultClass.getConstructor();
            type = constructor.newInstance();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return type;
    }
     **/
}
