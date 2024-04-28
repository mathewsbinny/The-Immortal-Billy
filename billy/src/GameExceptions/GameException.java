package GameExceptions;

import java.io.IOException;

/**
 * Class: GameException
 * @author Mathews Binny
 * @version 1.0
 * Course: ITEC 3860 Fall
 * Written: April 28, 2024
 *
 * Purpose: Custom exceptions for the game
 */

public class GameException extends IOException {
    public GameException(){}

    public GameException(String message){
        super(message);
    }

    public GameException(String message, Throwable throwable) {
        super(message, throwable);
    }

} //End InvalidInoutException class
