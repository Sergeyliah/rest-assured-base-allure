package exception;

public class ScriptExecutionException extends RuntimeException{
    public ScriptExecutionException(String message, Throwable cause){
        super(message, cause);
    }
}