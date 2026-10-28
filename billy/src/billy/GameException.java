package billy;

/**
 * Root exception for game-specific errors (bad input, missing data, etc.).
 */
public class GameException extends Exception {

    public GameException() {
        super();
    }

    public GameException(String message) {
        super(message);
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}
