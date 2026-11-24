import java.io.Serializable;

/**
 *
 * An exception when importing a file with an invalid maze format.
 *
 * @author Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 12, 2025
 *
 */

public class InvalidMazeException extends Exception {
    public InvalidMazeException(String message) {
        super(message);
    }
}