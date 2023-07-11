package renderers.utilities;

import exceptions.TextureLoadingException;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import settings.Settings;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {

    private static Map<Color, Integer> colorMap= new HashMap<>();

    public static NodeList getElementsFromXml(String xmlFilePath, String tagName) throws Exception {
        File xmlFile = new File(xmlFilePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName(tagName);
        return nodeList;
    }

    public static Image loadImage(String path) throws Exception {
        Image image = new Image(path, Settings.TEXTURE_SIZE, Settings.TEXTURE_SIZE, false, true);
        while(image.getProgress() < 1.)
            System.out.println("loading image..."); //todo: make proper loading functionality
        if(image.isError())
            throw image.getException();
        return image;
    }

    public static int colorToARGB(Color color) {
        return colorToARGB(color, false, 0.);
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
    private static int toInt(double value) {
        return (int)(value * 255.);
    }
}
