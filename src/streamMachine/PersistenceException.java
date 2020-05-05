package streamMachine;

public class PersistenceException extends Exception {
    public PersistenceException() {
        super();
    }

    public PersistenceException(String message) {
        super(message);
    }
    public PersistenceException(String message, Exception e) {
        super(message, e);
    }
}
