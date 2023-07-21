package resources.textures;
import exceptions.TextureLoadingException;
import renderers.utilities.ImageLoader;
import settings.Settings;
import java.awt.image.BufferedImage;

import static renderers.utilities.ColorUtilities.colorToARGB;

public class Texture {

    private final String id;

    private final String path;

    private boolean imageLoaded;

    private BufferedImage image;
    private int[][] colors;
    private int size;

    public Texture(String id, String path){
        this.id = id;
        this.path = path;
        this.imageLoaded = false;
        this.size = Settings.TEXTURE_SIZE;
        colors = new int[size][size];
    }

    public boolean loadImage() {
        try {
            this.image = ImageLoader.loadImage(path);
            this.image = ImageLoader.resizeImage(image, size, size);
            fillColorMap();
            this.imageLoaded = true;
        } catch (Exception e) {
            System.out.println("Error loading image on path: " + path);
            e.printStackTrace();
            fillColorMapDefaultColor();
            this.imageLoaded = false;
            throw new TextureLoadingException(e);
        } finally {
            return this.imageLoaded;
        }
    }

    private void fillColorMapDefaultColor() {
        int defaultColor = colorToARGB(Settings.DEFAULT_TEXTURE_COLOR);
        for(int x = 0; x < size; x++)
            for(int y = 0; y < size; y++)
                colors[y][x] = defaultColor;
    }

    private void fillColorMap() {
        for(int x = 0; x < size; x++)
            for(int y = 0; y < size; y++)
                colors[x][y] = image.getRGB(x, y);
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public int[][] getColors() {
        return colors;
    }

    public int getSize() {
        return size;
    }

    public int getColor(int x, int y) {
        return colors[y][x];
    }
}
