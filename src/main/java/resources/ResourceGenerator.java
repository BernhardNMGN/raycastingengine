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
import resources.segments.*;
import resources.segments.walls.GreyLargeBrickWall;
import resources.textures.TextureIdBuffer;
import resources.textures.TextureMap;
import settings.Settings;

import java.nio.IntBuffer;
import java.util.List;
import java.util.Optional;

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
        return new GameRendererTopDown(new WallRendererTopDown(gc, map), new PlayerRendererTopDown(gc), cursorRenderer, hudRenderer, canvas);
    }

    private static CursorType createDefaultCursorType() {
        return new CursorTypeCrossHair();
    }

    public static GameRendererRayCasting generate3dRenderer(GameMap map) {
        RayCaster rayCaster = new RayCaster(map);
        Robot mouseMover = new Robot();
        AngleCalculatorFirstPerson angleCalculator = new AngleCalculatorFirstPerson(mouseMover);
        TextureMap textureMap = generateTextureMap();
        TextureIdBuffer textureIdBuffer = generateTextureIdBuffer(map);
        return new GameRendererRayCasting(new FloorAndCeilingRendererRayCasting(map, textureMap, textureIdBuffer), //
                new WallRendererRayCasting(map,textureMap, textureIdBuffer), //
                new PlayerRendererRayCasting(), //
                new CursorRendererRayCasting(), //
                new HudRendererRayCasting(), //
                angleCalculator);
    }

    private static TextureMap generateTextureMap() {
        TextureMap map = new TextureMap(Settings.FILE_PATH_FOR_TEXTURES);
        boolean loaded = map.loadTextures();
        System.out.println("All textures loaded: " + loaded);
        return map;
    }

    private static TextureIdBuffer generateTextureIdBuffer(GameMap map) {
        List<List<Segment>> segments = map.getMap();
        int horizontalMapSize = segments.get(0).size();
        int verticalMapSize = segments.size();
        String[][][] textureIds = new String[horizontalMapSize][verticalMapSize][3];
        map.streamSegments().forEach(seg -> {
            int x = (int) seg.getStartCoords().getX();
            int y = (int) seg.getStartCoords().getY();
            textureIds[x][y] = switch (seg) {
                case Wall wall -> new String[]{wall.getWallTextureId(), null, null};
                case Floor floor -> new String[]{null, floor.getFloorTextureId(), floor.getCeilingTextureId()};
                default -> new String[]{null, null, null};
            };
        });
        return new TextureIdBuffer(textureIds);
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
