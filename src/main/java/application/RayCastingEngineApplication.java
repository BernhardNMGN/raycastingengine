package application;

import game.Game;
import game.GameLoop;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import raycasting.RayCaster;
import renderers.interfaces.*;
import renderers.raycasting.GameRendererRayCasting;
import renderers.topdown.GameRendererTopDown;
import resources.ResourceGenerator;
import resources.map.GameMap;
import resources.map.MapBuilder;
import settings.Settings;

import java.io.IOException;
import java.nio.IntBuffer;

public class RayCastingEngineApplication extends Application {

    private final String name = "Ray-casting Demo";
    private final ImageView imageView;
    private final StackPane root;

    private boolean testMode = false;

    public RayCastingEngineApplication() {
        imageView = new ImageView();
        root = new StackPane(imageView);
    }

    @Override
    public void start(Stage stage) throws IOException {

//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), Settings.HORIZONTAL_RESOLUTION, Settings.VERTICAL_RESOLUTION, Color.BLACK);

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        Settings.HORIZONTAL_RESOLUTION = (int)screenBounds.getWidth();
        Settings.VERTICAL_RESOLUTION = (int)screenBounds.getHeight();
        System.out.println("Height: " + screenBounds.getHeight() + " Width: " + screenBounds.getWidth());

        stage.setTitle(name);
//        stage.setFullScreen(true);

        Scene scene = new Scene(root);
        stage.setScene(scene);

        Canvas canvas = new Canvas(Settings.HORIZONTAL_RESOLUTION, Settings.VERTICAL_RESOLUTION);
        PixelBuffer<IntBuffer> pixelBuffer = ResourceGenerator.generatePixelBuffer();
        root.getChildren().add(canvas);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.ESCAPE) {
                    System.out.println("Key Pressed: " + ke.getCode());
                    stage.close();
                }
            }
        });

        MapBuilder mapBuilder = new MapBuilder();
        GameMap map = mapBuilder.buildFromFile("defaultMap");

        GameRendererTopDown rendererTopDown = ResourceGenerator.generate2dRendererTopDown(canvas, map);
        GameRendererRayCasting renderer3D = ResourceGenerator.generate3dRenderer(map);
        RayCaster rayCaster = new RayCaster(map);

        if(testMode) {
//            testMethod(pixelBuffer);
        }
        else {
//            Game game = new Game(rayCaster, pixelBuffer, imageView, map, scene, renderer3D);
            GameLoop gameLoop = new GameLoop(rayCaster, pixelBuffer, imageView, map, scene, renderer3D, rendererTopDown);
            stage.show();
            gameLoop.start();
//            game.run();
//            game.runWithoutTimeLine(stage); //not working
        }

    }

    private void testSmallPicture() {
        Color color1 = Color.SKYBLUE;
        int backGround = colorToARGB(color1);
        Color color2 = Color.DARKRED;
        int fill = colorToARGB(color2);
    }

    public void testMethod(PixelBuffer<IntBuffer> pixelBuffer) {

        int width = Settings.HORIZONTAL_RESOLUTION;
        int height = Settings.VERTICAL_RESOLUTION;
        Color color1 = Color.SKYBLUE;
        int testColor1 = colorToARGB(color1);
        Color color2 = Color.LIGHTGRAY;
        int testColor2 = colorToARGB(color2);

        int[] pixels = pixelBuffer.getBuffer().array();
        WritableImage image = new WritableImage(pixelBuffer);


        for(int i = 0; i < pixels.length; i++)
            pixels[i] = testColor2;
        pixelBuffer.updateBuffer(b -> null);
        imageView.setImage(image);

        /*

        double x = 350.;
        double y = 250.;
        Point2D playerCoords = new Point2D(x, y);
        double angle = 300.;

        MapBuilder mapBuilder = new MapBuilder();
        Map map = mapBuilder.buildFromFile("defaultMap");
        Player player = new Player(playerCoords, map);
        RayCaster rayCaster = new RayCaster(map);
        rayCaster.run();
//        rayCaster.castRay(playerCoords, angle);

         */
    }

    private int colorToARGB(Color color) {
        int alpha = toInt(color.getOpacity());
        int red = toInt(color.getRed());
        int green = toInt(color.getGreen());
        int blue = toInt(color.getBlue());
        int result = (alpha << 24) + (red << 16) + (green << 8) + blue;
        return result;
    }

    private int toInt(double value) {
        return (int)(value * 255.);
    }

    public static void main(String[] args) {
        launch();
    }
}