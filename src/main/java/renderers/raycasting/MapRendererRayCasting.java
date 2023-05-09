package renderers.raycasting;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import raycasting.RayCaster;
import raycasting.RayResult;
import renderers.interfaces.MapRenderer;
import renderers.utilities.RenderType;
import resources.map.GameMap;
import settings.Settings;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.stream.IntStream;

public class MapRendererRayCasting implements MapRenderer {

    private GameMap map;
    private RayCaster rayCaster;
    private PixelBuffer<IntBuffer> pixelBuffer;
    private WritableImage image;
    private ImageView imageView;

    private int ceilingColor;
    private int floorColor;
    private Double maxDist;
    private Double minDist;

    private int[] noSegmentArray;
    private HashMap<Color, Integer> colorMap;

    private int width;
    private int height;

    private double screenDistance;

    public MapRendererRayCasting(GameMap map, PixelBuffer pixelBuffer, ImageView imageView, RayCaster rayCaster) {
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
        this.screenDistance = Settings.SCREEN_DISTANCE;
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
        IntStream.range(0, rayResults.length)
                .forEach(i -> {
                    fillBufferPerColumn(pixels, rayResults[i], i);
                });
        pixelBuffer.updateBuffer(b -> null);
        imageView.setImage(image);

    }

    private void fillBufferPerColumn(int[] pixels, RayResult rayResult, int column) {
        double rayDist = rayResult.getDistance();
        if(rayDist >= maxDist)
            fillBufferPerColumnFromArray(pixels, 0 , column, this.noSegmentArray);
        else {
            rayDist *= Math.cos(Math.toRadians(map.getCurrentPlayerAngle() - rayResult.getAngle())); // remove fishbowl effect (but add reverse fishbowl effect?)
            int segmentColor = colorToARGB(rayResult.getSegment().getFillColor(), true, rayDist);
            int segmentHeight = rayDist <= minDist ? height : computeSegHeight(rayDist);
            if(segmentHeight % 2 != 0)
                segmentHeight++;
            int ceilingHeight = (height - segmentHeight)/2;
            if(segmentHeight > height || ceilingHeight > height)
                System.out.println("debug: height incorrect");
            int start = 0;
            start = fillBufferPerColumn(pixels, start,  ceilingHeight, column, ceilingColor); // fill buffer with ceiling color
            start = fillBufferPerColumn(pixels, start, segmentHeight, column, segmentColor); // fill buffer with segment
            fillBufferPerColumn(pixels, start, ceilingHeight, column, floorColor); // fill buffer with floor color
        }
    }

    private void fillBufferPerColumnTextured(int[] pixels, RayResult rayResult, int column) {
        double rayDist = rayResult.getDistance();
        if(rayDist >= maxDist)
            fillBufferPerColumnFromArray(pixels, 0 , column, this.noSegmentArray);
        else {
            int segmentHeight = rayDist <= minDist ? height : computeSegHeight(rayDist);
            if(segmentHeight % 2 != 0)
                segmentHeight++;
            int ceilingHeight = (height - segmentHeight)/2;
            int[] texturedSegment = createArrayFromTexture(rayResult, segmentHeight);
            if(segmentHeight > height || ceilingHeight > height)
                System.out.println("debug: height incorrect");
            int start = 0;
            start = fillBufferPerColumn(pixels, start,  ceilingHeight, column, ceilingColor); // fill buffer with ceiling color
            start = fillBufferPerColumnFromArray(pixels, start, column, texturedSegment); // fill buffer with segment
            fillBufferPerColumn(pixels, start, ceilingHeight, column, floorColor); // fill buffer with floor color
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

    private int fillBufferPerColumn(int[] pixels, int start, int length, int column, int color) {
        int end = start + length;
        for(int i = start; i < end; i++) {
            int index = i * width + column;
            if(index >= pixels.length)
                System.out.println("debug: index out of bounds");
            pixels[index] = color;
        }
        return end;
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

    private int computeSegHeight(Double rayDist) {
        if(rayDist >= maxDist)
            return 0;
        else if(rayDist <= minDist)
            return height;
        int result = (int) Math.min(height, (screenDistance/(rayDist + .00001)));
        if(result < 0 || result > height)
            System.out.println("debug: segment height calculation incorrect");
        return result;
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.RAY_CASTING;
    }
}
