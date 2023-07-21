package game;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import raycasting.RayCaster;
import raycasting.RayResult;
import renderers.anglecalculators.AngleCalculator;
import renderers.interfaces.GameRenderer;
import renderers.raycasting.GameRendererRayCasting;
import renderers.topdown.GameRendererTopDown;
import resources.Player;
import resources.map.GameMap;
import settings.Settings;

import java.awt.*;
import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Set;

public class GameLoop extends AnimationTimer {
    /**
     * Game Objects
     */
    private GameMap map;
    private Player player;
    private Point2D cursor;

    private RayCaster rayCaster;
    private Set<KeyCode> currentKeysPressed = new HashSet<>();

    private RayResult[] rayResults;
    /**
     * Loop Objects
     */
    private Integer frameRate;
    private EventHandler<ActionEvent> eventHandler;
    private KeyFrame keyFrame;
    private Timeline gameLoop;

    /**
     * Available & current renderers
     */
    private Scene scene;
    private GameRenderer currentGameRenderer;
    private GameRendererRayCasting gameRendererRayCasting;
    private GameRendererTopDown gameRendererTopDown;
    private PixelBuffer<IntBuffer> pixelBuffer;
    private WritableImage image;
    private ImageView imageView;

    private long lastFrameCall;
    private long frameTime;

    public GameLoop(RayCaster rayCaster, PixelBuffer<IntBuffer> pixelBuffer, ImageView imageView, GameMap map, Scene scene, GameRendererRayCasting gameRendererRayCasting, GameRendererTopDown gameRendererTopDown) {
        this.scene = scene;
        this.rayCaster = rayCaster;
        this.pixelBuffer = pixelBuffer;
        this.image = new WritableImage(pixelBuffer);
        this.imageView = imageView;
        this.map = map;

        setupInputListeners();
        setupRenderers(gameRendererRayCasting, gameRendererTopDown);
        initializeCursorAndPlayer();
    }
    @Override
    public void handle(long now) {
        executeGameLoop();
        frameTime = System.nanoTime() - now;
        System.out.println("Frame rendered in " + ((long) (frameTime * .000001)) + " ms");
        lastFrameCall = now;
    }

    private void executeGameLoop() {
        int[] pixels = pixelBuffer.getBuffer().array();
        map.setCurrentPlayerDirAndUpdateCameraPlane(player.updateAngleVector(cursor));
        map.setCurrentPlayerCoords(player.updateCoordinates(currentKeysPressed));
        rayResults = rayCaster.run();
        drawUsingRayCastRenderer(pixels);
        pixelBuffer.updateBuffer(b -> null);
        imageView.setImage(image);
    }

    private void setupRenderers(GameRendererRayCasting gameRendererRayCasting, GameRendererTopDown gameRendererTopDown) {
        this.gameRendererRayCasting = gameRendererRayCasting;
        this.gameRendererTopDown = gameRendererTopDown;
        if(gameRendererRayCasting != null)
            this.currentGameRenderer = gameRendererRayCasting;
        else this.currentGameRenderer = gameRendererTopDown;
    }

    private void setupInputListeners() {
        scene.setOnKeyTyped(event -> {
            if(event.getCode() == KeyCode.ESCAPE)
                System.exit(0);
        });
        scene.setOnKeyPressed(event -> currentKeysPressed.add(event.getCode()));
        scene.setOnKeyReleased(event -> currentKeysPressed.remove(event.getCode()));
        scene.setOnMouseMoved(event -> cursor = new Point2D(event.getX(), event.getY()));
    }

    private void initializeCursorAndPlayer() {
        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        cursor = new Point2D(mousePoint.getX(), mousePoint.getY());
        player = new Player(map.getPlayerStartCoords(), map);
        AngleCalculator angleCalculator = currentGameRenderer.getAngleCalculator();
        player.setAngleCalculator(angleCalculator);
        scene.setCursor(Cursor.NONE);
    }

    private void drawUsingRayCastRenderer(int[] pixels) {
        this.gameRendererRayCasting.drawAll(rayResults, pixels, currentKeysPressed, player, map, cursor);
    }
}
