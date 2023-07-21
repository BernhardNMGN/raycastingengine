package settings;

import javafx.scene.paint.Color;
import renderers.cursortypes.CursorTypeCrossHair;

public class Settings {

    public static final Integer FRAMERATE = 50;
    public static Integer HORIZONTAL_RESOLUTION = 1920;
    public static Integer VERTICAL_RESOLUTION = 1080;


    public static final Double SEGMENT_SIZE = 1.;
    public static final Double PLAYER_SIZE = SEGMENT_SIZE/2.;

    public static final Double PLAYER_MOVEMENT_SPEED = SEGMENT_SIZE; // player movement speed in pixels/second
    public static final Double PLAYER_RUN_FACTOR = 3.;
    public static final Double PLAYER_MOVEMENT_SPEED_IN_ENGINE = PLAYER_MOVEMENT_SPEED/FRAMERATE;

    public static final Integer TEXTURE_SIZE_EXPONENT = 8;
    public static final Integer TEXTURE_SIZE = (int) Math.pow(2, TEXTURE_SIZE_EXPONENT); //needs to be a power of 2 for bitwise AND operator

    public static final Double DEFAULT_CURSOR_SIZE = 60.;
    public static final Color DEFAULT_CURSOR_COLOR = Color.RED;

    public static final Color DEFAULT_TEXTURE_COLOR = Color.BLACK;

    public static final String DEFAULT_WALL_TEXTURE_PATH = "/textures/default/default_wall.png";

    public static final String DEFAULT_WALL_TEXTURE_ID = "default_wall";
    public static final String DEFAULT_FLOOR_TEXTURE_ID = "default_floor";
    public static final String DEFAULT_CEILING_TEXTURE_ID = "default_ceiling";
    public static final Class DEFAULT_CURSOR_TYPE = CursorTypeCrossHair.class;

    public static final Double MAX_TURNING_SPEED = FRAMERATE/10000.; // maximum player turning speed in degrees/second

    public static final Color PRIMARY_PLAYER_COLOR = Color.GREEN;
    public static final String FILE_PATH_FOR_MAP = "./src/main/resources/maps/";

    public static final String FILE_PATH_FOR_TEXTURES = "src/main/resources/textures/";
    public static final Double DEFAULT_HUD_HEIGHT = 100.;
    public static final Double DEFAULT_HUD_BOX_HEIGHT = 80.;
    public static final Double DEFAULT_HUD_BOX_WIDTH = 200.;
    public static final Double DEFAULT_HUD_MARGIN_RATIO = .1;

    public static final Integer PLAYER_FOV = 90;
    public static final Double SCREEN_DISTANCE = (HORIZONTAL_RESOLUTION/2) / Math.tan(Math.toRadians(PLAYER_FOV/2.));
    public static final Double MAX_DRAW_DISTANCE = SEGMENT_SIZE * 10.; // maximum distance a segment can be away from the player and still get rendered
    public static final Double MIN_DRAW_DISTANCE = SEGMENT_SIZE/10.; // segments that are at or closer than this distance completely cover the screen

    public static final ImageQuality IMAGE_QUALITY = ImageQuality.HIGH;
}
