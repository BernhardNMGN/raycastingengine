package exceptions;

public class TextureLoadingException extends Exception{
    public TextureLoadingException(Exception exception) {
        super(exception.getMessage());
    }
    public TextureLoadingException(String message) {
        super(message);
    }
}
