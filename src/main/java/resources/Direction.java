package resources;
import static resources.Axis.*;

public enum Direction {

    LEFT(HORIZONTAL,1),
    RIGHT(HORIZONTAL, -1),
    UP(VERTICAL, 1),
    DOWN(VERTICAL, -1);

    private Axis axis;
    private int dir;
    private Direction(Axis axis, int dir) {
        this.axis = axis;
        this.dir = dir;
    }

    public Axis getAxis() {
        return axis;
    }

    public int getDir() {
        return dir;
    }
}
