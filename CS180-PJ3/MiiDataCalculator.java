import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 *
 * Main calculator for using Mii data to generate a data report
 *
 * @author Eugene Zhang, zhan4713
 * @version Oct 22, 2025
 *
 */

public class MiiDataCalculator {
    public static void main(String[] args) {
        List<String> commandsLines = new ArrayList<>();
        List<String> outputLines = new ArrayList<>();
        MiiDatabase miidb = new MiiDatabase();
        
        System.out.println("1");

        try {
            commandsLines = Files.readAllLines(Paths.get("Commands.txt"));

            System.out.println("2a: " + commandsLines);
            if (!miidb.mapGames()) {
                return;
            } else if (!miidb.readMiiData()) {
                return;
            }
            System.out.println("2b: " + miidb.getMiiList() + " " + miidb.getGameMapping());
        } catch (IOException e) {
            commandsLines.add("IO Read Failure");
            e.printStackTrace();
        }

        System.out.println("3");

        for (String line : commandsLines) {
            try {
                String[] modeAndName = line.split(": ");
                switch (modeAndName[0]) {
                    case "Mii High Score":
                        String[] fullNameHighScore = modeAndName[1].split(" ");
                        outputLines.add(miidb.getMiiHighScores(fullNameHighScore[0], fullNameHighScore[1]));
                        break;
                    case "Mii Low Score":
                        String[] fullNameLowScore = modeAndName[1].split(" ");
                        outputLines.add(miidb.getMiiLowScores(fullNameLowScore[0], fullNameLowScore[1]));
                        break;
                    case "Sport High Score":
                        outputLines.add(miidb.getSportHighScores(modeAndName[1]));
                        break;
                    case "Sport Low Score":
                        outputLines.add(miidb.getSportLowScores(modeAndName[1]));
                        break;
                    case "All Data":
                        for (String sport : miidb.getGameMapping()) {
                            outputLines.add(miidb.getSportHighScores(sport));
                            outputLines.add(miidb.getSportLowScores(sport));
                        }
                        for (Mii mii : miidb.getMiiList()) {
                            outputLines.add(miidb.getMiiHighScores(mii.getFirstName(), mii.getLastName()));
                            outputLines.add(miidb.getMiiLowScores(mii.getFirstName(), mii.getLastName()));
                        }
                        break;
                    default:
                        outputLines.add("Failure");
                        break;
                }
                System.out.println("4");
            } catch (BadDataException e) {
                System.out.println("5");
                outputLines.add(e.getMessage());
            }
        }

        System.out.println("6: " + outputLines);

        try {
            Files.write(Paths.get("MiiDataReport.txt"), outputLines);
            System.out.println("DONE!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
