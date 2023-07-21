package renderers.raycasting;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.WritableImage;
import raycasting.RayResult;
import renderers.interfaces.FloorAndCeilingRenderer;
import renderers.utilities.RenderType;
import resources.map.GameMap;
import resources.textures.TextureIdBuffer;
import resources.textures.TextureMap;
import settings.Settings;

import java.nio.IntBuffer;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Renders floor & ceiling with only default texture, currently not used
 */
public class FloorAndCeilingRendererRayCasting implements FloorAndCeilingRenderer {

    private final GameMap map;

    private final int horizontalMapSize;
    private final int verticalMapSize;

    private final TextureMap textureMap;

    private final TextureIdBuffer textureIdBuffer;
    private int screenCentre;

    private int screenDistance;

    private Map<Integer, Integer> distanceTable;

    private int drawstart;

    private int height;
    private int width;

    private int texSize;

    public FloorAndCeilingRendererRayCasting(GameMap map, TextureMap textureMap, TextureIdBuffer textureIdBuffer) {
        this.map = map;
        this.textureMap = textureMap;
        this.textureIdBuffer = textureIdBuffer;

        this.horizontalMapSize = map.getMap().get(0).size();
        this.verticalMapSize = map.getMap().size();

        this.width = Settings.HORIZONTAL_RESOLUTION;
        this.height = Settings.VERTICAL_RESOLUTION;
        this.screenCentre = height/2;
        this.screenDistance = Settings.SCREEN_DISTANCE.intValue();
        this.texSize = Settings.TEXTURE_SIZE;

        this.drawstart = (int) (screenCentre * 1.1);

        this.distanceTable = initializeDistanceTable();
    }

    private Map<Integer, Integer> initializeDistanceTable() {
        return IntStream.range(0, height)
                .boxed()
                .collect(Collectors.toMap(y -> y,
                        y -> {
                            double divisor = 2. * y - height;
                            if(divisor == 0.)
                                divisor = .000001;
                            return (int) (height/divisor);
                        }));
    }

    @Override
    public void draw() {

    }

    @Override
    public void draw(int[] pixels) {

    }

    @Override
    public void draw(RayResult[] rayResults, int[] pixels) {

        double playerDirX = map.getCurrentPlayerDir().getX();
        double playerDirY = map.getCurrentPlayerDir().getY();
        double cameraPlaneX = map.getCameraPlane().getX();
        double cameraPlaneY = map.getCameraPlane().getY();
        double posX = map.getCurrentPlayerCoords().getX();
        double posY = map.getCurrentPlayerCoords().getY();
        int texSize = Settings.TEXTURE_SIZE;

        double rayDirX0 = playerDirX - cameraPlaneX;
        double rayDirX1 = playerDirX + cameraPlaneX;
        double rayDirY0 = playerDirY - cameraPlaneY;
        double rayDirY1 = playerDirY + cameraPlaneY;


        int length = Settings.HORIZONTAL_RESOLUTION;
        int height = Settings.VERTICAL_RESOLUTION;
        int screenCentre = height/2;

        // draw from horizon until bottom of the screen
        for (int y = drawstart; y < height; y++) {
            int rowDistance = distanceTable.get(y);

            // calculate the real world step vector we have to add for each x (parallel to camera plane)
            // adding step by step avoids multiplications with a weight in the inner loop
            double floorStepX = rowDistance * (rayDirX1 - rayDirX0) / length;
            double floorStepY = rowDistance * (rayDirY1 - rayDirY0) / length;

            // real world coordinates of the leftmost column. This will be updated as we step to the right.
            double floorX = posX + rowDistance * rayDirX0;
            double floorY = posY + rowDistance * rayDirY0;

            for(int x = 0; x < length; ++x)
            {
                double weight = rowDistance / rayResults[x].getDistance();

                double currentFloorX = weight * floorX + (1.0 - weight) * posX;
                double currentFloorY = weight * floorY + (1.0 - weight) * posY;

                // the cell coord is simply got from the integer parts of floorX and floorY
                int cellX = (int)(floorX);
                int cellY = (int)(floorY);

                // get the texture coordinate from the fractional part
                int tx = (int)(texSize * (floorX - cellX)) & (texSize - 1);
                int ty = (int)(texSize * (floorY - cellY)) & (texSize - 1);

                int[][] floorColors;
                int[][] ceilingColors;
                if(cellX < 0 || cellX >= horizontalMapSize || cellY < 0 || cellY >= verticalMapSize) {
//                    System.out.println("debug");
                    floorColors = textureMap.get("default_floor").getColors();
                    ceilingColors = textureMap.get("default_ceiling").getColors();
                }
                else {
                    floorColors = textureMap.get(textureIdBuffer.getFloorTextureId(cellX, cellY)).getColors();
                    ceilingColors = textureMap.get(textureIdBuffer.getCeilingTextureId(cellX, cellY)).getColors();
                }

                //floor
                int index = y * length + x; //transform index to position in pixelBuffer array
                int color = floorColors[tx][ty];
                color = (color >> 1) & 8355711; // make a bit darker
                pixels[index] = color;

                //ceiling
                index = ((height - y) -1) * length + x;
                color = ceilingColors[tx][ty];
                color = (color >> 1) & 8355711; // make a bit darker
                pixels[index] = color;

                floorX += floorStepX;
                floorY += floorStepY;

//                // floor
//                color = texture[floorTexture][texWidth * ty + tx];
//                color = (color >> 1) & 8355711; // make a bit darker
//                buffer[y][x] = color;
//
//                //ceiling (symmetrical, at screenHeight - y - 1 instead of y)
//                color = texture[ceilingTexture][texWidth * ty + tx];
//                color = (color >> 1) & 8355711; // make a bit darker
//                buffer[screenHeight - y - 1][x] = color;
            }
        }
    }

    private void drawFloorAndCeiling(RayResult[] rayResults, int[] pixels) {
        double dirX = map.getCurrentPlayerDir().getX();
        double dirY = map.getCurrentPlayerDir().getY();
        double planeX = map.getCameraPlane().getX();
        double planeY = map.getCameraPlane().getY();
        double posX = map.getCurrentPlayerCoords().getX();
        double posY = map.getCurrentPlayerCoords().getY();
        for(int y = height/2; y < height; y++) {
            // rayDir for leftmost ray (x = 0) and rightmost ray (x = w)
            double rayDirX0 = dirX - planeX;
            double rayDirY0 = dirY - planeY;
            double rayDirX1 = dirX + planeX;
            double rayDirY1 = dirY + planeY;

            // Current y position compared to the center of the screen (the horizon)
            int p = y - height / 2;

            // Vertical position of the camera.
            double posZ = 0.5 * screenDistance;

            // Horizontal distance from the camera to the floor for the current row.
            // 0.5 is the z position exactly in the middle between floor and ceiling.
            double rowDistance = posZ / p;

            // calculate the real world step vector we have to add for each x (parallel to camera plane)
            // adding step by step avoids multiplications with a weight in the inner loop
            double floorStepX = rowDistance * (rayDirX1 - rayDirX0) / width;
            double floorStepY = rowDistance * (rayDirY1 - rayDirY0) / width;

            // real world coordinates of the leftmost column. This will be updated as we step to the right.
            double floorX = posX + rowDistance * rayDirX0;
            double floorY = posY + rowDistance * rayDirY0;

            for(int x = 0; x < width; ++x)
            {
                // the cell coord is simply got from the integer parts of floorX and floorY
                int cellX = (int)(floorX);
                int cellY = (int)(floorY);

                // get the texture coordinate from the fractional part
                int tx = (int)(texSize * (floorX - cellX)) & (texSize - 1);
                int ty = (int)(texSize * (floorY - cellY)) & (texSize - 1);

                floorX += floorStepX;
                floorY += floorStepY;

                //floor
//                String floorTexId = textureIdBuffer.getFloorTextureId(cellX, cellY);
                int floorColor = textureMap.getColor("default_floor", tx, ty);
//            floorColor = (floorColor >> 1) & 8355711; //make floor darker
                pixels[y*width+x] = floorColor;

                //ceiling (symmetrical!)
//                String ceilingTexId = textureIdBuffer.getCeilingTextureId(cellX, cellY);
                int ceilingColor = textureMap.getColor("default_ceiling", tx, ty);
                pixels[((height - y)-1)*width+x] = ceilingColor;
            }
        }
    }

    @Override
    public RenderType getRenderType() {
        return null;
    }
}
