/**
 *
 * Represents a Mii on the Wii console.
 *
 * @author Eugene Zhang, zhan4713
 * @version Oct 22, 2025
 *
 */

public class Mii {
    private String firstName;
    private String lastName;
    private int[] scores;

    public Mii(String firstName, String lastName, int[] scores) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.scores = scores;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }
    
    public int[] getScores() {
        return this.scores;
    }

    public void setScores(int[] scores) {
        this.scores = scores;
    }
}
