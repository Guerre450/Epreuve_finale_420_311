package mv.sdd.exceptions;

/**
 * Exception lorsque le client n'existe pas
 */
public class ExceptionClientDoesNotExist extends Exception{
    public ExceptionClientDoesNotExist(String message) {
        super(message);
    }
}
