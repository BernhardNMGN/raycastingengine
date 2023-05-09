package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import renderers.anglecalculators.AngleCalculator;
import renderers.interfaces.GameRenderer;
import resources.Player;
import resources.map.GameMap;
import settings.Settings;

import java.awt.*;
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
    private Set<KeyCode> currentKeysPressed = new HashSet<>();
    /**
     * Loop Objects
     */
    private Integer frameRate;
    private EventHandler<ActionEvent> eventHandler;
    private KeyFrame keyFrame;
    private Timeline gameLoop;

    /**
     * Available & current renderers
//     */
//    private GraphicRendererContainer grContainer;
    private Scene scene;
    private int currentRendererIndex;
    private GameRenderer[] availableRenderers = new GameRenderer[values().length];



    public Game(GameMap map, Scene scene, GameRenderer... renderers){
        this.frameRate = Settings.FRAMERATE;
//        this.grContainer = grContainer;
        this.scene = scene;
        setupInputListeners();
        setupRenderers(renderers);
        setupGameLoop(frameRate);
        this.map = map;
        initializeCursorAndPlayer();
    }

    private void setupRenderers(GameRenderer[] renderers) {
        for(GameRenderer renderer : renderers) {
            availableRenderers[renderer.getRenderType().ordinal()] = renderer;
        }
        int index = 0;
        while (availableRenderers[index] == null)
            index++;
        this.currentRendererIndex = index;
    }

    private void initializeCursorAndPlayer() {
        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        cursor = new Point2D(mousePoint.getX(), mousePoint.getY());
        player = new Player(map.getPlayerStartCoords(), map);
        AngleCalculator angleCalculator = getCurrentRenderer().getAngleCalculator();
        player.setAngleCalculator(angleCalculator);
        scene.setCursor(Cursor.NONE);
    }

    private void setupGameLoop(Integer frameRate) {
        this.eventHandler = actionEvent -> {
            long start = System.currentTimeMillis();
            map.setCurrentPlayerAngle(player.updateAngle(cursor));
            long next = System.currentTimeMillis();
            long angleCalcTime = (next - start);
            start = next;
            map.setCurrentPlayerCoords(player.updateCoordinates(currentKeysPressed));
            next = System.currentTimeMillis();
            long playerMoveTime = (next - start);
            start = next;
            drawUsingCurrentRenderer();
            next = System.currentTimeMillis();
            long renderTime = (next - start);
            start = next;
            System.out.println("angle calc time: " + angleCalcTime + ", player move time: " + playerMoveTime + ", render time: " + renderTime + ", total frame rendered in : " + (angleCalcTime + playerMoveTime + renderTime) + " milliseconds");
        };
        this.keyFrame = new KeyFrame(Duration.millis(1000/ frameRate), eventHandler);
        gameLoop = new Timeline(keyFrame);
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        setupInputListeners();
    }

    private GameRenderer getCurrentRenderer() {
        return availableRenderers[currentRendererIndex];
    }

    private void drawUsingCurrentRenderer() {
        getCurrentRenderer().drawAll(currentKeysPressed, player, map, cursor);
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

    private void switchRenderer(int index) {
        //todo: implement
    }
}
