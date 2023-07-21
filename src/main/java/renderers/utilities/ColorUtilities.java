package renderers.utilities;

import javafx.scene.paint.Color;
import settings.Settings;

import java.util.HashMap;

public class ColorUtilities {

    private static int maxDistance = Settings.MAX_DRAW_DISTANCE.intValue();

    private static HashMap<Color, Integer> colorMap = new HashMap<>();

    private static int black = colorToARGB(Color.BLACK);

    public static int colorToARGB(Color color, boolean applyShading) {
        return colorToARGB(color, applyShading, 0.);
    }

    public static int colorToARGB(Color color, boolean applyShading, double rayDist) {
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

    public static int colorToARGB(Color color) {
        if (colorMap.containsKey(color))
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

    public static int applyShading(int color, double distance) {
        if (distance < 0) {
            return color;
        }
        if (distance >= maxDistance) {
            return black;
        }
        double amount = (maxDistance - distance) / maxDistance;
        int a = 0xFF & (color >> 24);
        int r = (int) ((0xFF & (color >> 16)) * amount);
        int g = (int) ((0xFF & (color >> 8)) * amount);
        int b = (int) ((0xFF & color) * amount);
        return (a << 24) + (r << 16) + (g << 8) + b;
    }

    private static int toInt(double value) {
        return (int)(value * 255.);
    }

    public static int getBlack() {
        return black;
    }
}
