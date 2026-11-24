import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/**
 *
 * Represents a database that pulls Mii data from files.
 *
 * @author Eugene Zhang, zhan4713
 * @version Oct 22, 2025
 *
 */

public class MiiDatabase {
    private List<Mii> miiList;
    private List<String> gameMapping;

    public MiiDatabase() {
        this.miiList = null;
        this.gameMapping = null;
    }

    public boolean mapGames() {
        try {
            this.gameMapping = Files.readAllLines(Paths.get("GameMapping.txt"));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean readMiiData() {
        int miiNumber = 1;
        List<Mii> currentMiis = new ArrayList<>();
        try {
            while (Files.exists(Paths.get("Mii" + miiNumber + ".txt"))) {
                List<String> outputLines = Files.readAllLines(Paths.get("Mii" + miiNumber + ".txt"));
                String[] firstAndLastName = outputLines.remove(0).split(" ");
                int[] scores = new int[this.gameMapping.size()];

                for (String entry : outputLines) {
                    String[] sportAndScore = entry.split(": ");
                    scores[this.gameMapping.indexOf(sportAndScore[0])] = Integer.parseInt(sportAndScore[1]);
                }

                currentMiis.add(new Mii(firstAndLastName[0], firstAndLastName[1], scores));

                miiNumber++;
            }

            System.out.println("Size is " + currentMiis.size());
            this.miiList = currentMiis.size() > 0 ? currentMiis : null;
            return true;
        } catch (IndexOutOfBoundsException | IOException | NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getSportHighScores(String sport) throws BadDataException {
        int sportIndex = this.gameMapping.indexOf(sport);
        if (sportIndex == -1) {
            throw(new BadDataException("Sport " + sport + " does not exist!"));
        }

        Mii maxScorer = Collections.max(this.miiList, new Comparator<Mii>() {
            @Override
            public int compare(Mii mii1, Mii mii2) {
                return Integer.compare(mii1.getScores()[sportIndex], mii2.getScores()[sportIndex]);
            }
        });

        if (maxScorer.getScores()[sportIndex] == 0) {
            return sport + " was not played by any Mii!";
        }

        return String.format("The highest score for %s is %d by %s %s!", sport,
            maxScorer.getScores()[sportIndex], maxScorer.getFirstName(), maxScorer.getLastName());
    }

    public String getSportLowScores(String sport) throws BadDataException {
        int sportIndex = this.gameMapping.indexOf(sport);
        if (sportIndex == -1) {
            throw(new BadDataException("Sport " + sport + " does not exist!"));
        }

        List<Mii> filteredMiis = this.miiList.stream().filter(mii -> mii.getScores()[sportIndex] != 0)
            .collect(Collectors.toList());
        
        if (filteredMiis.isEmpty()) {
            return sport + " was not played by any Mii!";
        }

        Mii minScorer = Collections.min(filteredMiis, new Comparator<Mii>() {
            @Override
            public int compare(Mii mii1, Mii mii2) {
                return Integer.compare(mii1.getScores()[sportIndex], mii2.getScores()[sportIndex]);
            }
        });

        return String.format("The lowest score for %s is %d by %s %s!", sport,
            minScorer.getScores()[sportIndex], minScorer.getFirstName(), minScorer.getLastName());
    }

    public String getMiiHighScores(String firstName, String lastName) throws BadDataException {
        Optional<Mii> mii = miiList.stream().filter(
            miiCandidate -> miiCandidate.getFirstName().equals(firstName) && miiCandidate.getLastName().equals(lastName)
        ).findAny();

        if (mii.isEmpty()) {
            throw new BadDataException("Mii " + firstName + " " + lastName + " does not exist!");
        }

        List<Integer> scores = Arrays.stream(mii.get().getScores()).boxed().collect(Collectors.toList());

        int maxScore = Collections.max(scores);
        int index = scores.indexOf(maxScore);

        return firstName + " " + lastName + " scored the highest in "
            + this.gameMapping.get(index) + " with a total of " + maxScore + " points!";
    }
    
    public String getMiiLowScores(String firstName, String lastName) throws BadDataException {
        Optional<Mii> mii = miiList.stream().filter(
            miiCandidate -> miiCandidate.getFirstName().equals(firstName) && miiCandidate.getLastName().equals(lastName)
        ).findAny();

        if (mii.isEmpty()) {
            throw new BadDataException("Mii " + firstName + " " + lastName + " does not exist!");
        }

        List<Integer> scores = Arrays.stream(mii.get().getScores()).boxed().collect(Collectors.toList());
        List<Integer> filteredScores = scores.stream().filter(score -> score != 0).collect(Collectors.toList());

        int minScore = Collections.min(filteredScores);
        int index = scores.indexOf(minScore);

        return firstName + " " + lastName + " scored the lowest in "
            + this.gameMapping.get(index) + " with a total of " + minScore + " points!";

    }

    public List<Mii> getMiiList() {
        return this.miiList;
    }

    public List<String> getGameMapping() {
        return this.gameMapping;
    }
}
