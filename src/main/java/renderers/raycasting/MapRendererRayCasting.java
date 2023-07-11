package renderers.raycasting;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import raycasting.RayCaster;
import raycasting.RayResult;
import renderers.interfaces.MapRenderer;
import renderers.utilities.ImageLoader;
import renderers.utilities.RenderType;
import resources.map.GameMap;
import resources.textures.TextureMap;
import settings.Settings;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.stream.IntStream;

public class MapRendererRayCasting implements MapRenderer {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private GameMap map;
    private RayCaster rayCaster;
    private PixelBuffer<IntBuffer> pixelBuffer;
    private WritableImage image;
    private ImageView imageView;


    private ImageLoader imageLoader;

    private int ceilingColor;
    private int floorColor;
    private Double maxDist;
    private Double minDist;

    private int[] noSegmentArray;
    private HashMap<Color, Integer> colorMap;

    private TextureMap textureMap;

    private int width;
    private int height;

    private int screenDistance;

    public MapRendererRayCasting(GameMap map, PixelBuffer pixelBuffer, ImageView imageView, RayCaster rayCaster, TextureMap textureMap) {
        this.map = map;
        this.rayCaster = rayCaster;
        this.pixelBuffer = pixelBuffer;
        this.image = new WritableImage(pixelBuffer);
        this.imageView = imageView;

        this.colorMap = new HashMap<>();
        this.ceilingColor = colorToARGB(map.getAssets().getCeilingColor(), false);
        this.floorColor = colorToARGB(map.getAssets().getFloorColor(), false);
        this.maxDist = Settings.MAX_DRAW_DISTANCE;
        this.minDist = Settings.MIN_DRAW_DISTANCE;

        this.height = pixelBuffer.getHeight();
        this.width = pixelBuffer.getWidth();
        this.noSegmentArray = generateNoSegmentArray();
        this.screenDistance = Settings.SCREEN_DISTANCE.intValue();

        this.textureMap = textureMap;
    }

    private int colorToARGB(Color color, boolean applyShading) {
        return colorToARGB(color, applyShading, 0.);
    }

    private int colorToARGB(Color color, boolean applyShading, double rayDist) {
        if(applyShading) {
            int alpha = toInt(color.getOpacity());
            double shadingFactor = 1. + Math.pow(rayDist, 2.) * .01;
            int red = toInt(color.getRed()/shadingFactor);
            int green = toInt(color.getGreen()/shadingFactor);
            int blue = toInt(color.getBlue()/shadingFactor);
            int result = (alpha << 24) + (red << 16) + (green << 8) + blue;
            return result;
        }
        else if (colorMap.containsKey(color))
            return colorMap.get(color);
        else {
            int alpha = toInt(color.getOpacity());
            int red = toInt(color.getRed());
            int green = toInt(color.getGreen());
            int blue = toInt(color.getBlue());
            int result = (alpha << 24) + (red << 16) + (green << 8) + blue;
            colorMap.put(color, result);
            return result;
        }
    }

    private int toInt(double value) {
        return (int)(value * 255.);
    }

    private int[] generateNoSegmentArray() {
        return IntStream.range(0, height)
                .map(i -> i < height/2 ? ceilingColor : floorColor)
                .toArray();
    }

    @Override
    public void draw() {
        RayResult[] rayResults = rayCaster.run();
        IntBuffer intBuffer = pixelBuffer.getBuffer();
        int[] pixels = intBuffer.array();
        IntStream.range(0, Settings.HORIZONTAL_RESOLUTION)
                .forEach(i -> {
                    int index = (int) (i * Settings.IMAGE_QUALITY.getScalingFactor());
//                    fillBufferPerColumn(pixels, rayResults[index], i);
                    RayResult rayResult = rayResults[index];
                    int[] colors = textureMap.getColorColumn(rayResult.getSegment().getTextureId(), rayResult.getTextureX());
                    fillBufferPerColumnTextured(pixels, rayResult.getDistance(), colors, i);
                });
        pixelBuffer.updateBuffer(b -> null);
        imageView.setImage(image);
    }

    private long computeNewLogEntry(String action, long previousTime) {
        long timeElapsed = System.currentTimeMillis() - previousTime;
        log.info("Action: " + action + " executed in " + timeElapsed + " ms.");
        return System.currentTimeMillis();
    }

    private void fillBufferPerColumn(int[] pixels, RayResult rayResult, int column) {
        double rayDist = rayResult.getDistance();
        if(rayDist >= maxDist)
            fillBufferPerColumnFromArray(pixels, 0 , column, this.noSegmentArray);
        else {
            int segmentColor = colorToARGB(rayResult.getSegment().getFillColor(), true, rayDist);
            int virtualSegHeight = (int) (screenDistance/(rayDist + .00001));
            int segHeight = rayDist <= minDist ? screenDistance : Math.min(height,virtualSegHeight);
            if(segHeight % 2 != 0)
                segHeight++;
            int ceilingHeight = (height - segHeight)/2;

            int segStart = (height - segHeight)/2;
            int segEnd = segStart + segHeight;

            if(segHeight > height || ceilingHeight > height)
                System.out.println("debug: height incorrect");
            partiallyFillBufferPerColumn(pixels, 0,  segStart, column, ceilingColor); // fill buffer with ceiling color
            partiallyFillBufferPerColumn(pixels, segStart, segEnd, column, segmentColor); // fill buffer with segment
            partiallyFillBufferPerColumn(pixels, segEnd, height, column, floorColor); // fill buffer with floor color
        }
    }

    private void fillBufferPerColumnTextured(int[] pixels, double rayDist, int[] texColors, int column) {
        int textureSize = texColors.length;
        if(rayDist >= maxDist)
            fillBufferPerColumnFromArray(pixels, 0 , column, this.noSegmentArray);
        else {
            int virtualSegHeight = (int) (screenDistance/(rayDist + .00001));
            int segHeight = rayDist <= minDist ? screenDistance : Math.min(height,virtualSegHeight);
            int segStart = (height - segHeight)/2;
            int segEnd = segStart + segHeight;
            double stepSize =  ((double)textureSize/(double)virtualSegHeight);
            double texStart = (segStart - height/2. + virtualSegHeight/2.) * stepSize;
//            if(segHeight >= height && texStart > 479.9 && texStart < 480.1)
//            if(segHeight >= height)
//                System.out.println("debug");
            partiallyFillBufferPerColumn(pixels, 0,  segStart, column, ceilingColor); // fill buffer with ceiling color
            partiallyFillBufferPerColumnTextured(pixels, segStart, segEnd, column, texColors, stepSize, texStart);
            partiallyFillBufferPerColumn(pixels, segEnd, height, column, floorColor); // fill buffer with floor color
        }
    }

    private void partiallyFillBufferPerColumnTextured(int[] pixels, int start, int end, int column, int[] colors, double step, double texStart) {
        int texHeight = colors.length;
        for(int i = start; i < end; i++) {
            int bufferIndex = i * width + column; //transform index to position in pixelBuffer array
            int texIndex = (int)texStart & (texHeight - 1);
            texStart += step;
//            if(texIndex == 0 || texIndex == colors.length - 1)
//                System.out.println("debug");
            pixels[bufferIndex] = colors[texIndex];
        }
    }

    private void partiallyFillBufferPerColumn(int[] pixels, int start, int end, int column, int color) {
        for(int i = start; i < end; i++) {
            int bufferIndex = i * width + column; //transform index to position in pixelBuffer array
            pixels[bufferIndex] = color;
        }
    }

    private int[] createArrayFromTexture(RayResult rayResult, int segmentHeight) {
        int[][] textureArray = rayResult.getSegment().getTextureArray();
        Color[] textureColors = rayResult.getSegment().getTextureArrayColors();
        int relativeXIndex = getRelativeIndex( rayResult.getRelativeXPos(), textureArray[0].length);
        int textureHeight = textureArray.length;
        int[] result = new int[segmentHeight];
        for(int i = 0; i < segmentHeight; i++) {
            double relativeYPos = (double) i / segmentHeight;
            int relativeYIndex = getRelativeIndex(relativeYPos, textureHeight);
            int colorValueFromArray = textureArray[relativeYIndex][relativeXIndex];
            result[i] = colorToARGB(textureColors[colorValueFromArray], false);
        }
        return result;
    }

    private int getRelativeIndex(double relativePos, int length) {
        return (int) (relativePos * (double) length);
    }

    private int fillBufferPerColumnFromArray(int[] pixels, int start, int column, int[] source) {
        int end = start + source.length;
        for(int i = start; i < end; i++) {
            int index = i * width + column;
            if (index >= pixels.length)
                System.out.println("debug: index out of bounds");
            pixels[index] = source[i - start];
        }
        return end;
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.RAY_CASTING;
    }
}
