package renderers.raycasting;

import javafx.scene.paint.Color;
import raycasting.RayResult;
import renderers.interfaces.WallRenderer;
import renderers.utilities.ColorUtilities;
import renderers.utilities.RenderType;
import resources.map.GameMap;
import resources.textures.TextureIdBuffer;
import resources.textures.TextureMap;
import settings.Settings;
import java.util.stream.IntStream;
import static renderers.utilities.ColorUtilities.*;

public class WallRendererRayCasting implements WallRenderer {
// Logger dependency not working..
//    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private GameMap map;
    private Double maxDist;
    private Double minDist;

    private TextureMap textureMap;
    private TextureIdBuffer textureIdBuffer;

    private int width;
    private int height;
    private int screenDistance;

    private double[] distanceTable;

    private int texSize;

    private int[][] floorColors = null;
    private int[][] ceilingColors = null;
    private int fX = 0;
    private int fY = 0;

    private final int black = colorToARGB(Color.BLACK);
    private int maxDistance = Settings.MAX_DRAW_DISTANCE.intValue();

    public WallRendererRayCasting(GameMap map,TextureMap textureMap, TextureIdBuffer textureIdBuffer) {
        this.map = map;

        this.textureIdBuffer = textureIdBuffer;
        this.maxDist = Settings.MAX_DRAW_DISTANCE;
        this.minDist = Settings.MIN_DRAW_DISTANCE;

        this.height = Settings.VERTICAL_RESOLUTION;
        this.width = Settings.HORIZONTAL_RESOLUTION;
        this.screenDistance = Settings.SCREEN_DISTANCE.intValue();

        this.textureMap = textureMap;
        this.texSize = Settings.TEXTURE_SIZE;
        this.distanceTable = initializeDistanceTable();
    }

    private double[] initializeDistanceTable() {
        double[] distances = new double[height];
        IntStream.range(0, height)
                .boxed()
                .forEach(y -> {
                            double divisor = 2. * y - height;
                            if(divisor == 0.)
                                divisor = .000001;
                            distances[y] = (screenDistance/divisor);
                        });
        return distances;
    }

    @Override
    public void draw(RayResult[] rayResults, int[] pixels) {
//        drawFloorAndCeiling(rayResults, pixels);
        double posX = map.getCurrentPlayerCoords().getX();
        double posY = map.getCurrentPlayerCoords().getY();
        IntStream.range(0, Settings.HORIZONTAL_RESOLUTION)
                .forEach(i -> {
                    int index = (int) (i * Settings.IMAGE_QUALITY.getScalingFactor());
                    RayResult rayResult = rayResults[index];
                    int[] colors = textureMap.getColorColumn(rayResult.getWallSegment().getWallTextureId(), rayResult.getTextureX());
                    fillBufferPerColumnTextured(pixels, rayResult, colors, i, posX, posY);
                });
    }

    @Override
    public void draw() {
    }

    @Override
    public void draw(int[] pixels) {
    }

    private void fillBufferPerColumnTextured(int[] pixels, RayResult rayResult, int[] texColors, int column, double posX, double posY) {
        int textureSize = texColors.length;
        double rayDist = rayResult.getDistance();
        int virtualSegHeight = (int) (screenDistance/(rayDist + .00001));
        int segHeight = rayDist <= minDist ? screenDistance : Math.min(height,virtualSegHeight);
        int segStart = (height - segHeight)/2;
        int segEnd = segStart + segHeight;
        if(rayDist <= maxDist) {
            double stepSize =  ((double)textureSize/(double)virtualSegHeight);
            double texStart = (segStart - height/2. + virtualSegHeight/2.) * stepSize;
            fillWallColumnTextured(pixels, segStart, segEnd, column, texColors, stepSize, texStart, rayDist);
            fillFloorAndCeilingColumnTextured(pixels, segStart, segEnd, rayResult, posX, posY, column);
        }
        else {
            fillWallColumnBlack(pixels, segStart, segEnd, column);
            fillFloorAndCeilingColumnTextured(pixels, segStart, segEnd, rayResult, posX, posY, column);
        }
    }

    private void fillWallColumnBlack(int[] pixels, int start, int end, int column) {
        int index = start * width + column; //transform index to position in pixelBuffer array
        for(int i = start; i < end; i++) {
            pixels[index] = black;
            index += width;
        }
    }

    private void fillFloorAndCeilingColumnTextured(int[] pixels,int segStart, int segEnd, RayResult rayResult, double posX, double posY, int column) {
        int floorIndex = segEnd * width + column;
        int ceilingIdex = segStart * width + column;
        double floorXWall = rayResult.getFloorXWall();
        double floorYWall = rayResult.getFloorYWall();
        double distWallInv = 1./rayResult.getDistance(); //small optimization trick that lets us do multiplication instead of division to compute weight
        int floorColor;
        int ceilingColor;
        for(int y = segEnd + 1; y < height; y++) {
            double distance = distanceTable[y];
            if(distance < maxDistance) {
                double weight = distance * distWallInv;

                double currentFloorX = weight * floorXWall + (1.0 - weight) * posX;
                double currentFloorY = weight * floorYWall + (1.0 - weight) * posY;

                int newfX = (int) currentFloorX;
                int newfY = (int) currentFloorY;

                // only fetch new texture if coordinates have changed, this increases performances by ~2.5 times!
                if (newfX != fX || newfY != fY) {
                    floorColors = textureMap.get(textureIdBuffer.getFloorTextureId(newfX, newfY)).getColors();
                    ceilingColors = textureMap.get(textureIdBuffer.getCeilingTextureId(newfX, newfY)).getColors();
                }

                int floorTexX = (int) (currentFloorX * texSize) % texSize;
                int floorTexY = (int) (currentFloorY * texSize) % texSize;

                floorColor = applyShading(floorColors[floorTexX][floorTexY], distance);
                ceilingColor = applyShading(ceilingColors[floorTexX][floorTexY], distance);

                fX = newfX;
                fY = newfY;
            }
            else {
                floorColor = black;
                ceilingColor = black;
            }

            //floor
            pixels[floorIndex] = floorColor;

            //ceiling (symmetrical!)
            pixels[ceilingIdex] = ceilingColor;

            floorIndex += width;
            ceilingIdex -= width;

        }
    }

    private void fillWallColumnTextured(int[] pixels, int start, int end, int column, int[] colors, double step, double texStart, double distance) {
        int texHeight = colors.length;
        int index = start * width + column; //transform index to position in pixelBuffer array
        for(int i = start; i < end; i++) {
            int texIndex = (int)texStart & (texHeight - 1);
            texStart += step;
            int color = applyShading(colors[texIndex], distance);
            pixels[index] = color;
            index += width;
        }
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.RAY_CASTING;
    }
}
