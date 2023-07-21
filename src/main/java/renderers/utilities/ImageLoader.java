package renderers.utilities;

import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
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

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    public static BufferedImage loadImage(String path) throws Exception {
        URL resource = ImageLoader.class.getResource(path);
        File imageFile = Paths.get(resource.toURI()).toFile();
        return ImageIO.read(imageFile);
    }
}
