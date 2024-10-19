package exception;

public class PropertyFileLoadingException extends RuntimeException{
    public PropertyFileLoadingException(String message){
        super(message);
    }

    public PropertyFileLoadingException(String message, Throwable cause){
            super(message, cause);
        }
}
