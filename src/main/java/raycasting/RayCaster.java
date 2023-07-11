package raycasting;

import javafx.geometry.Point2D;
import org.apache.commons.geometry.euclidean.twod.Vector2D;
import resources.Axis;
import resources.Direction;
import resources.map.GameMap;
import resources.segments.Segment;
import resources.segments.Wall;
import settings.Settings;
import java.util.List;
public class RayCaster {

    private GameMap map;
    private List<List<Segment>> segments;

    public RayCaster(GameMap map) {
        this.map = map;
        this.segments = map.getMap();
    }

    public RayResult[] run() {
        double posX = map.getCurrentPlayerCoords().getX();
        double posY = map.getCurrentPlayerCoords().getY();
        Vector2D dir = map.getCurrentPlayerDir();
        Vector2D plane = dir.orthogonal().multiply(Math.tan(Settings.PLAYER_FOV/2.));
        int nrOfRays = (int) (Settings.HORIZONTAL_RESOLUTION * Settings.IMAGE_QUALITY.getScalingFactor());
        RayResult[] rayResults = new RayResult[nrOfRays];
        for(int x = 0; x < nrOfRays; x++) {
            double cameraX = 2. * (((double) x) / ((double) nrOfRays)) - 1.;
            double rayDirX = dir.getX() + plane.getX() * cameraX;
            double rayDirY = dir.getY() + plane.getY() * cameraX;
            rayResults[x] = castRay(posX, posY, rayDirX, rayDirY);
        }
        return rayResults;
    }

    public RayResult castRay(double posX, double posY, double rayDirX, double rayDirY) {
        int[] mapCoords = {(int) posX, (int) posY};
        double[] deltaDistances = {(rayDirX == 0.) ? 1e30 : Math.abs(1. / rayDirX), (rayDirY == 0.) ? 1e30 : Math.abs(1. / rayDirY)};
        double[] sideDistances = {(rayDirX < 0 ? posX - mapCoords[0] : mapCoords[0] + 1. - posX) * deltaDistances[0], (rayDirY < 0 ? posY - mapCoords[1] : mapCoords[1] + 1. - posY) * deltaDistances[1]};
        int[] steps = {(int) Math.signum(rayDirX), (int) Math.signum(rayDirY)};

        boolean hit = false; //was there a wall hit?
        int i = 0; //was the wall hit at a side or at top/bottom and do we thus update X or Y?
        Segment nextSeg = segments.get(mapCoords[1]).get(mapCoords[0]);

        while(!hit) {
            i = sideDistances[0] < sideDistances[1] ? 0 : 1; //jump to next map square, either in x-direction, or in y-direction
            sideDistances[i] += deltaDistances[i];
            mapCoords[i] += steps[i];
            nextSeg = segments.get(mapCoords[1]).get(mapCoords[0]);//Check if ray has hit a wall
            if (nextSeg instanceof Wall)
                hit = true;
        }
        //todo: fix!!!
        double distance = sideDistances[i] - deltaDistances[i];
//        Direction wallSide = computeWallDirection(i, rayDirX, rayDirY);
        double wallX = computeWallXCoordinate(i, posX, posY, distance, rayDirX, rayDirY); //relative x-coordinate of where the wall was hit
        int textureX = computeTextureXIndex(i, wallX, distance, rayDirX, rayDirY); //wallX scaled & transformed to get x-coordinate for texture
        return new RayResult(distance, nextSeg, textureX);
    }

    private int computeTextureXIndex(int side, double wallX, double distance, double rayDirX, double rayDirY) {
        int texWidth = Settings.TEXTURE_SIZE;
        int texX = (int) (wallX * (double) texWidth);
        if((side == 0 && rayDirX > 0) || (side == 1 && rayDirY < 0))
            texX = texWidth - texX - 1;
        return texX;
    }

    private Direction computeWallDirection(int i, double rayDirX, double rayDirY) {
        Axis axis = Axis.values()[i];
        if(axis.equals(Axis.HORIZONTAL)) return rayDirX > 0 ? Direction.LEFT : Direction.RIGHT;
        else return rayDirY < 0 ? Direction.UP : Direction.DOWN;
    }

    private double computeWallXCoordinate(int side, double posX, double posY, double distance, double rayDirX, double rayDirY) {
        double wallX = side == 0 ? posY + distance * rayDirY : posX + distance * rayDirX;
        return wallX - Math.floor(wallX);
    }
}
