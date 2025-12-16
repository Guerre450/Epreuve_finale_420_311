package mv.sdd.exceptions;


/**
 * Exception lorsque la commande est deja en preparation
 */
public class ExceptionOrderIsProcessed extends Exception{
    public ExceptionOrderIsProcessed(String msg){
        super(msg);
    }
}
