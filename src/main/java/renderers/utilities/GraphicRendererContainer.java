package renderers.utilities;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelBuffer;

import java.nio.IntBuffer;

public class GraphicRendererContainer {

    private Scene scene;
    private GraphicsContext gc;
    private PixelBuffer<IntBuffer> pixelBuffer;
    private ImageView imageView;

    public GraphicRendererContainer(Scene scene, GraphicsContext gc, PixelBuffer<IntBuffer> pixelBuffer, ImageView imageView) {
        this.scene = scene;
        this.gc = gc;
        this.pixelBuffer = pixelBuffer;
        this.imageView = imageView;
    }

    public GraphicRendererContainer(Scene scene, GraphicsContext gc) {
        this(scene, gc, null, null);
    }

    public GraphicRendererContainer(Scene scene, PixelBuffer<IntBuffer> pixelBuffer, ImageView imageView) {
        this(scene, null, pixelBuffer, imageView);
    }

    public Scene getScene() {
        return scene;
    }

    public GraphicsContext getGraphicsContext() {
        return gc;
    }

    public PixelBuffer<IntBuffer> getPixelBuffer() {
        return pixelBuffer;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
