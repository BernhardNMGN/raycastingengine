package resources.textures;

import exceptions.TextureLoadingException;
import resources.segments.Wall;

import java.util.Optional;

/**
 * Stores all texture ID's by their map coördinates.
 */
public class TextureIdBuffer {

    private final int WALL = 0;
    private final int FLOOR = 1;
    private final int CEILING = 2;

    private String errorMessage = "The Segment at coordinates [{},{}] has no texture Id of type {}";

    private final String[][][] textureIds;

    public TextureIdBuffer(String[][][] textureIds) {
        this.textureIds = textureIds;
    }

    public String getWallTextureId(int x, int y) {
        return textureIds[x][y][WALL];
    }

    public String getFloorTextureId(int x, int y) {
        return textureIds[x][y][FLOOR];
    }

    public String getCeilingTextureId(int x, int y) {
        return textureIds[x][y][CEILING];
    }

}
