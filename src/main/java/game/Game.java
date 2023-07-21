package game;

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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
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

import static renderers.utilities.RenderType.*;

public class Game {
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

    private long startFrame;
    private long frameTime;

    public Game(RayCaster rayCaster, PixelBuffer<IntBuffer> pixelBuffer, ImageView imageView, GameMap map, Scene scene, GameRendererRayCasting gameRendererRayCasting, GameRendererTopDown gameRendererTopDown){
        this.frameRate = Settings.FRAMERATE;
        this.scene = scene;
        this.rayCaster = rayCaster;
        this.pixelBuffer = pixelBuffer;
        this.image = new WritableImage(pixelBuffer);
        this.imageView = imageView;
        setupInputListeners();
        this.map = map;
        setupRenderers(gameRendererRayCasting, gameRendererTopDown);
        initializeCursorAndPlayer();
        setupGameLoop(frameRate);
        setupInputListeners();
        this.startFrame = System.currentTimeMillis();
        this.frameTime = System.currentTimeMillis() - startFrame;
    }

    public Game(PixelBuffer<IntBuffer> pixelBuffer, ImageView imageView, GameMap map, Scene scene, GameRendererTopDown gameRendererTopDown) {
        this(null, pixelBuffer, imageView, map, scene, null, gameRendererTopDown);
    }

    public Game(RayCaster rayCaster, PixelBuffer pixelBuffer, ImageView imageView, GameMap map, Scene scene, GameRendererRayCasting gameRendererRayCasting) {
        this(rayCaster, pixelBuffer, imageView, map, scene, gameRendererRayCasting, null);
    }

    private void setupRenderers(GameRendererRayCasting gameRendererRayCasting, GameRendererTopDown gameRendererTopDown) {
        this.gameRendererRayCasting = gameRendererRayCasting;
        this.gameRendererTopDown = gameRendererTopDown;
        if(gameRendererRayCasting != null)
            this.currentGameRenderer = gameRendererRayCasting;
        else this.currentGameRenderer = gameRendererTopDown;
    }

    private void initializeCursorAndPlayer() {
        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        cursor = new Point2D(mousePoint.getX(), mousePoint.getY());
        player = new Player(map.getPlayerStartCoords(), map);
        AngleCalculator angleCalculator = currentGameRenderer.getAngleCalculator();
        player.setAngleCalculator(angleCalculator);
        scene.setCursor(Cursor.NONE);
    }

    private void setupGameLoop(Integer frameRate) {
        if(currentGameRenderer instanceof GameRendererRayCasting) {
            this.eventHandler = actionEvent -> {
                long start = System.currentTimeMillis();
                int[] pixels = pixelBuffer.getBuffer().array();
                map.setCurrentPlayerDirAndUpdateCameraPlane(player.updateAngleVector(cursor));
                map.setCurrentPlayerCoords(player.updateCoordinates(currentKeysPressed));
                rayResults = rayCaster.run();
                drawUsingRayCastRenderer(pixels);
                pixelBuffer.updateBuffer(b -> null);
                imageView.setImage(image);
//                long frameTime = System.currentTimeMillis() - start;
                System.out.println("Frame rendered in " + (System.currentTimeMillis() - start) + " ms");
            };
        }
        else {
            this.eventHandler = actionEvent -> {
                double startTime = System.currentTimeMillis();
//            map.setCurrentPlayerAngle(player.updateAngle(cursor));
                map.setCurrentPlayerDirAndUpdateCameraPlane(player.updateAngleVector(cursor));
                map.setCurrentPlayerCoords(player.updateCoordinates(currentKeysPressed));
                drawUsingTopDownRenderer();
            };
        }
        this.keyFrame = new KeyFrame(Duration.millis(1000/ frameRate), eventHandler);
        gameLoop = new Timeline(keyFrame);
        gameLoop.setCycleCount(Timeline.INDEFINITE);
    }

    private void drawUsingRayCastRenderer(int[] pixels) {
        this.gameRendererRayCasting.drawAll(rayResults, pixels, currentKeysPressed, player, map, cursor);
    }

    private void drawUsingTopDownRenderer() {
        this.gameRendererTopDown.drawAll(null, null, currentKeysPressed, player, map, cursor);
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

    public void run() {
        gameLoop.play();
    }

    public void runWithoutTimeLine(Stage stage) {
        while (true) {
            long start = System.currentTimeMillis();
            int[] pixels = pixelBuffer.getBuffer().array();
            map.setCurrentPlayerDirAndUpdateCameraPlane(player.updateAngleVector(cursor));
            map.setCurrentPlayerCoords(player.updateCoordinates(currentKeysPressed));
            rayResults = rayCaster.run();
            drawUsingRayCastRenderer(pixels);
            pixelBuffer.updateBuffer(b -> null);
            imageView.setImage(image);
            stage.setScene(scene);
            stage.show();
            System.out.println("Frame rendered in " + (System.currentTimeMillis() - start) + " ms");
        }
    }

    private void switchRenderer(int index) {
        //todo: implement
    }
}
