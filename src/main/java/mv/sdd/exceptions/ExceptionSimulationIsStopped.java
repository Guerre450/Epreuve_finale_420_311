package mv.sdd.exceptions;


/**
 * Exception lancé lorsque la simulation est arreté
 */
public class ExceptionSimulationIsStopped extends Exception{
    public ExceptionSimulationIsStopped(String message) {
        super(message);
    }
}
