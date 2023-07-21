package resources.textures;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import renderers.utilities.ImageLoader;
import settings.Settings;

import java.util.HashMap;
import java.util.Map;

public class TextureMap {

    private final String fileName = "textures.xml";

    private final String elementName = "texture";

    private final String DEFAULT_KEY = "default_texture_id";
    private String path;

    /**
     * Stores all Textures by their ID
     */
    private Map<String, Texture> textures;

    public TextureMap(String xmlPath) {
        this.path = xmlPath;
        this.textures = new HashMap<>();
    }

    public boolean loadTextures() {
        boolean allLoaded = true;
        try {
            allLoaded = loadDefaultTexture();
            NodeList nodeList = ImageLoader.getElementsFromXml(path + fileName, elementName);
            for(int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String id = element.getElementsByTagName("id").item(0).getTextContent();
                    if(!textures.containsKey(id)) {
                        String path = element.getElementsByTagName("path").item(0).getTextContent();
                        Texture texture = new Texture(id, path);
                        if (!texture.loadImage())
                            allLoaded = false;
                        this.textures.put(id, texture);
                    }
                }
            }
        } catch (Exception e) {
            allLoaded = false;
            e.printStackTrace();
        } finally {
            return allLoaded;
        }
    }

    private boolean loadDefaultTexture() {
        Texture defaultTexture = new Texture(DEFAULT_KEY, Settings.DEFAULT_WALL_TEXTURE_PATH);
        boolean loaded = defaultTexture.loadImage();
        this.textures.put(DEFAULT_KEY, defaultTexture);
        return loaded;
    }

    public Texture get(String id) {
        return textures.getOrDefault(id, textures.get(DEFAULT_KEY));
    }

    public int getColor(String id, int x, int y) {
        return get(id).getColor(x, y);
    }

    public int[] getColorColumn(String id, int x) {
        return get(id).getColors()[x];
    }
}
