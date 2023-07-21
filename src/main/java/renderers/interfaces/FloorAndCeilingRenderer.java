package renderers.interfaces;

import raycasting.RayResult;
import renderers.utilities.RenderType;

public interface FloorAndCeilingRenderer {
    void draw();

    void draw(int[] pixels);

    void draw(RayResult[] rayResults, int[] pixels);
    RenderType getRenderType();
}
